package edu.mga.knight_rider;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import edu.mga.knight_rider.adapters.TripAdapter;
import edu.mga.knight_rider.models.Trip;
import edu.mga.knight_rider.models.requests.JoinTripBody;
import edu.mga.knight_rider.network.TripDataService;
import edu.mga.knight_rider.network.RetrofitInstance;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindRideActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SharedPreferences prefs;
    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Trip> rideList;
    private ArrayList<Trip> shownRideList = new ArrayList<Trip>();
    private SwipeRefreshLayout swipeLayout;
    private TabLayout tabLayout;
    private TripDataService tripService;
    private NavigationView navView;
    private Spinner locationStart;
    private Spinner locationEnd;
    private String locationStartValue = "";
    private String locationEndValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Find Rides");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ride);
        super.onCreateDrawer();

        locationStart = (Spinner) findViewById(R.id.location_start_spinner);
        ArrayAdapter<CharSequence> l_start_adapter = ArrayAdapter.createFromResource(this, R.array.find_ride_choices_array, android.R.layout.simple_spinner_item);
        l_start_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationStart.setAdapter(l_start_adapter);

        locationEnd = (Spinner) findViewById(R.id.location_end_spinner);
        ArrayAdapter<CharSequence> l_end_adapter = ArrayAdapter.createFromResource(this, R.array.find_ride_choices_array, android.R.layout.simple_spinner_item);
        l_end_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationEnd.setAdapter(l_end_adapter);

        spinnerEventBinder(); // setup our spinner event listeners/logic

        tripService = RetrofitInstance.getRetrofitInstance().create(TripDataService.class); // Instantiate our service

        // Initialize RecyclerView and setup adapter
        recyclerView = (RecyclerView) findViewById(R.id.rides_container);
        adapter = new TripAdapter(this, shownRideList);

        //Init NavigationView
        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        layoutManager = new LinearLayoutManager(FindRideActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        prefs = this.getSharedPreferences("edu.mga.knightrider", Context.MODE_PRIVATE);

        if (!(prefs.contains("knight-rider-token") && prefs.contains("knight-rider-userid"))) {
            startActivity(new Intent(FindRideActivity.this, LoginActivity.class));
        }
        else if (new JWT(prefs.getString("knight-rider-token", null).replace("\"", "")).isExpired(0)) {
            startActivity(new Intent(FindRideActivity.this, LoginActivity.class));
        }
        else {
            Call<ArrayList<Trip>> call = tripService.getUserTrips("Bearer " + prefs.getString("knight-rider-token",null), prefs.getString("knight-rider-userid", null)); // Fill our request template

            // Make request and setup callback to handle response
            call.enqueue(new Callback<ArrayList<Trip>>() {
                @Override
                public void onResponse(Call<ArrayList<Trip>> call, Response<ArrayList<Trip>> response) {
                    generateTripList(response.body());
                }

                @Override
                public void onFailure(Call<ArrayList<Trip>> call, Throwable t) {
                    Toast.makeText(FindRideActivity.this, "Unable to load ride information.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //Initialize and create our toolbar and hamburger menu icon
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setDistanceToTriggerSync(200);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        Call<ArrayList<Trip>> call = tripService.getUserTrips("Bearer " + prefs.getString("knight-rider-token",null), prefs.getString("knight-rider-userid", null)); // Fill our request template

        // Make request and setup callback to handle response
        call.enqueue(new Callback<ArrayList<Trip>>() {
            @Override
            public void onResponse(Call<ArrayList<Trip>> call, Response<ArrayList<Trip>> response) {
                generateTripList(response.body());
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<Trip>> call, Throwable t) {
                Toast.makeText(FindRideActivity.this, "Unable to load ride information.", Toast.LENGTH_SHORT).show();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    final long now = System.currentTimeMillis();
    /*Method to generate List of employees using RecyclerView with custom adapter*/
    private void generateTripList(ArrayList<Trip> rides) {
        Collections.sort(rides, new Comparator<Trip>() {
            @Override
            public int compare(Trip trip, Trip t1) {
                long diff1 = Math.abs(trip.getDepartureTime().getTime() - now);
                long diff2 = Math.abs(t1.getDepartureTime().getTime() - now);
                return Long.compare(diff1, diff2);
            }
        });

        rideList = rides;
        shownRideList.clear();
        shownRideList.addAll(rideList);
        adapter.notifyDataSetChanged();
    }


    public void getMessages(int tripId) {
        Intent intent = new Intent(FindRideActivity.this, ChatActivity.class);
        intent.putExtra("tripId", tripId);
        startActivity(intent);
    }

    public void editTrip() {
        /*To-do: Jump to the activity that allows users to edit existing rides*/
    }

    public void leaveTrip(final int tripId) {
        final String userId = prefs.getString("knight-rider-userid", null);

        Snackbar snackbar = Snackbar.make(this.recyclerView, "Are you sure you want to leave this ride?", Snackbar.LENGTH_INDEFINITE).setAction("YES", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = tripService.leaveTrip("Bearer " + prefs.getString("knight-rider-token",null), tripId, userId);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() > 204) {
                            Toast.makeText(FindRideActivity.this, "Failed to leave the ride.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FindRideActivity.this, "Successfully left ride.", Toast.LENGTH_SHORT).show();
                            searchRides(locationStartValue, locationEndValue);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(FindRideActivity.this, "Unknown error occurred. Failed to leave the ride.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        snackbar.show();
    }

    public void deleteTrip(final int tripId) {
        Snackbar snackbar = Snackbar.make(this.recyclerView, "Delete the ride with the id " + tripId + "?", Snackbar.LENGTH_INDEFINITE).setAction("YES", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = tripService.deleteTrip("Bearer " + prefs.getString("knight-rider-token",null), tripId);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() > 204) {
                            Toast.makeText(FindRideActivity.this, "Failed to delete the ride.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(FindRideActivity.this, "Ride successfully deleted.", Toast.LENGTH_SHORT).show();
                            searchRides(locationStartValue, locationEndValue);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(FindRideActivity.this, "Unknown error occurred. Failed to delete the ride.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        snackbar.show();
    }

    public void joinTrip(final int tripId) {
        int userId = Integer.parseInt(prefs.getString("knight-rider-userid", "9999999999"));
        JoinTripBody body = new JoinTripBody(userId, tripId, 0.0, 0.0, "INVALID ADDRESS - DEPRECATED FIELD");

        Call<ResponseBody> call = tripService.joinTrip("Bearer " + prefs.getString("knight-rider-token", null), tripId, userId, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(FindRideActivity.this, "Successfully joined trip!", Toast.LENGTH_SHORT).show();
                    searchRides(locationStartValue, locationEndValue);
                } else {
                    Toast.makeText(FindRideActivity.this, "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FindRideActivity.this, "An unknown error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void spinnerEventBinder() {
        locationStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] locations = getResources().getStringArray(R.array.find_ride_choices_array);
                locationStartValue = locations[i];
                searchRides(locationStartValue, locationEndValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        locationEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] locations = getResources().getStringArray(R.array.find_ride_choices_array);
                locationEndValue = locations[i];
                searchRides(locationStartValue, locationEndValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void searchRides(final String location1, final String location2) {
        if (location1.isEmpty() || location2.isEmpty()) {
            Log.d("RIDE_SEARCH", "Empty locations, doing nothing");
            return;
        }

        Call<ArrayList<Trip>> call = tripService.getAllTrips("Bearer " + prefs.getString("knight-rider-token",null)); // Fill our request template

        // Make request and setup callback to handle response
        call.enqueue(new Callback<ArrayList<Trip>>() {
            @Override
            public void onResponse(Call<ArrayList<Trip>> call, Response<ArrayList<Trip>> response) {
                ArrayList<Trip> trips = new ArrayList<>();
                for (Trip t:response.body()) {
                    if (t.getOriginCity().equals(location1) && t.getDestCity().equals(location2)) {
                        trips.add(t);
                    }
                }
                generateTripList(trips);
            }

            @Override
            public void onFailure(Call<ArrayList<Trip>> call, Throwable t) {
                Toast.makeText(FindRideActivity.this, "Unable to load ride information.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Override to enable opening sidebar through hamburger menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                super.sidebar.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        return super.onOptionsItemSelected(item);
    }
}
