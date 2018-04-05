package edu.mga.knight_rider;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.mga.knight_rider.adapters.TripAdapter;
import edu.mga.knight_rider.models.Trip;
import edu.mga.knight_rider.network.GetTripDataService;
import edu.mga.knight_rider.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    private SharedPreferences prefs;
    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private ArrayList<Trip> rideList;
    private ArrayList<Trip> shownRideList = new ArrayList<Trip>();
    private SwipeRefreshLayout swipeLayout;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("Completed")) {
                    showUpcomingRides(false);
                }
                else if (tab.getText().toString().equals("Upcoming")) {
                    showUpcomingRides(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Initialize RecyclerView and setup adapter
        recyclerView = (RecyclerView) findViewById(R.id.rides_container);
        adapter = new TripAdapter(this, shownRideList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        prefs = this.getSharedPreferences("edu.mga.knightrider", Context.MODE_PRIVATE);

        if (!(prefs.contains("knight-rider-token") && prefs.contains("knight-rider-userid"))) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        else if (new JWT(prefs.getString("knight-rider-token", null).replace("\"", "")).isExpired(0)) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        else {
            GetTripDataService service = RetrofitInstance.getRetrofitInstance().create(GetTripDataService.class); // Instantiate our service
            Call<ArrayList<Trip>> call = service.getUserTrips("Bearer " + prefs.getString("knight-rider-token",null), prefs.getString("knight-rider-userid", null)); // Fill our request template

            // Make request and setup callback to handle response
            call.enqueue(new Callback<ArrayList<Trip>>() {
                @Override
                public void onResponse(Call<ArrayList<Trip>> call, Response<ArrayList<Trip>> response) {
                    generateTripList(response.body());
                }

                @Override
                public void onFailure(Call<ArrayList<Trip>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Unable to load ride information.", Toast.LENGTH_SHORT).show();
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
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        GetTripDataService service = RetrofitInstance.getRetrofitInstance().create(GetTripDataService.class); // Instantiate our service
        Call<ArrayList<Trip>> call = service.getUserTrips("Bearer " + prefs.getString("knight-rider-token",null), prefs.getString("knight-rider-userid", null)); // Fill our request template

        // Make request and setup callback to handle response
        call.enqueue(new Callback<ArrayList<Trip>>() {
            @Override
            public void onResponse(Call<ArrayList<Trip>> call, Response<ArrayList<Trip>> response) {
                generateTripList(response.body());
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<Trip>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Unable to load ride information.", Toast.LENGTH_SHORT).show();
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
        int tabIndex = tabLayout.getSelectedTabPosition();

        if (tabIndex == 0) {
            showUpcomingRides(true);
        } else {
            showUpcomingRides(false);
        }
    }


    private void showUpcomingRides(boolean b) {
        if (b) {
            shownRideList.clear();

            for (Trip t:rideList) {
                if (!t.getCompleted()) {
                    shownRideList.add(t);
                }
            }

            adapter.notifyDataSetChanged();
        } else {
            shownRideList.clear();

            for (Trip t:rideList) {
                if (t.getCompleted()) {
                    shownRideList.add(t);
                }
            }

            adapter.notifyDataSetChanged();
        }
    }

    public void getMessages() {
        /*To-do: Jump to activity that allows users to view messages*/
    }

    public void editTrip() {
        /*To-do: Jump to the activity that allows users to edit existing rides*/
    }

    public void leaveTrip() {
        /* To-do: Implement leave ride functionality - account for permissions */
    }

    public void deleteTrip() {
        /* To-do: Implement delete ride functionality */
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
}
