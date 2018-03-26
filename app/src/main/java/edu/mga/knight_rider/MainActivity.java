package edu.mga.knight_rider;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Toast;
import java.util.ArrayList;

import edu.mga.knight_rider.adapters.TripAdapter;
import edu.mga.knight_rider.models.Trip;
import edu.mga.knight_rider.models.TripList;
import edu.mga.knight_rider.network.GetTripDataService;
import edu.mga.knight_rider.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout sidebar;
    private SharedPreferences prefs;
    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private ArrayList<Trip> rideList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rides_container);
        rideList = new ArrayList<>();
        prefs = this.getSharedPreferences("edu.mga.knightrider", Context.MODE_PRIVATE);

        if (!(prefs.contains("knight-rider-token") && prefs.contains("knight-rider-userid"))) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {
            GetTripDataService service = RetrofitInstance.getRetrofitInstance().create(GetTripDataService.class); // Instantiate our service
            Call<ArrayList<Trip>> call = service.getTripData("Bearer " + prefs.getString("knight-rider-token", null)); // Fill our request template

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

        sidebar = findViewById(R.id.drawer_layout);
    }

    /*Method to generate List of employees using RecyclerView with custom adapter*/
    private void generateTripList(ArrayList<Trip> rides) {
        recyclerView = (RecyclerView) findViewById(R.id.rides_container);
        adapter = new TripAdapter(this, rides);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    // Override to enable opening sidebar through hamburger menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                sidebar.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
