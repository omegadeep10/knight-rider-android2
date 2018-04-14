package edu.mga.knight_rider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.mga.knight_rider.models.Trip;

public class ChatActivity extends AppCompatActivity {
    Trip currentTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle bundle = getIntent().getExtras();
        int tripId = bundle.getInt("tripId");
    }
}
