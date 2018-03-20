package edu.mga.knight_rider;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout sidebar;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = this.getSharedPreferences("edu.mga.knightrider", Context.MODE_PRIVATE);

        if (!prefs.contains("knight-rider-token") || !prefs.contains("knight-rider-userid")) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        //Initialize and create our toolbar and hamburger menu icon
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        sidebar = findViewById(R.id.drawer_layout);
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
