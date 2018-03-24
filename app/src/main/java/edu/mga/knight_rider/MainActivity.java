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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.mga.knight_rider.adapters.RideAdapter;
import edu.mga.knight_rider.models.Trip;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout sidebar;
    private SharedPreferences prefs;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RideAdapter adapter;
    private List<Trip> rideList;

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
            populateRideInformation();
        }

        Log.d("TOKEN: ", git prefs.getString("knight-rider-token", null));

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RideAdapter(this, rideList);
        recyclerView.setAdapter(adapter);

        //Initialize and create our toolbar and hamburger menu icon
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        sidebar = findViewById(R.id.drawer_layout);
    }

    private void populateRideInformation() {
        Ion.with(this.getBaseContext())
                .load("http://knightrider.mgaitec.net:8080/ridesharing/trips")
                .setHeader("X-Authorization", "Bearer " + prefs.getString("knight-rider-token", null))
                .asJsonArray()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonArray>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonArray> result) {
                        if (e != null) Toast.makeText(getBaseContext(), "Unable to access server. Try again later.", Toast.LENGTH_LONG).show();
                        if (result.getHeaders().code() != 200) {
                            Toast.makeText(getBaseContext(), result.toString(), Toast.LENGTH_LONG).show();
                            return;
                        }

                        JsonArray allRides = result.getResult();
                        for (int i = 0; i < allRides.size(); i++) {
                            JsonObject ride = allRides.get(i).getAsJsonObject();
                            rideList.add(new Trip(ride.get("id").getAsInt(), ride.get("driverId").getAsInt(), ride.get("originAddress").getAsString() ));
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
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
