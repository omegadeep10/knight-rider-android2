package edu.mga.knight_rider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import edu.mga.knight_rider.models.requests.MessageBody;
import com.auth0.android.jwt.JWT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.mga.knight_rider.adapters.ChatAdapter;
import edu.mga.knight_rider.adapters.TripAdapter;
import edu.mga.knight_rider.models.Message;
import edu.mga.knight_rider.models.Trip;
import edu.mga.knight_rider.network.MessageDataService;
import edu.mga.knight_rider.network.RetrofitInstance;
import edu.mga.knight_rider.network.TripDataService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends BaseActivity {
    private SharedPreferences prefs;
    private SwipeRefreshLayout swipeLayout;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TripDataService tripService;
    private MessageDataService messageService;
    private ArrayList<Message> messageList = new ArrayList<Message>();
    private int tripId;
    private EditText messageInput;
    private RelativeLayout progressBarWrapper;
    private Button sendMessageButton;
    final long now = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Messages");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle bundle = getIntent().getExtras();
        tripId = bundle.getInt("tripId");
        super.onCreateDrawer();

        prefs = this.getSharedPreferences("edu.mga.knightrider", Context.MODE_PRIVATE);
        tripService = RetrofitInstance.getRetrofitInstance().create(TripDataService.class); // Instantiate our service
        messageService = RetrofitInstance.getRetrofitInstance().create(MessageDataService.class);

        //Initialize and create our toolbar and hamburger menu icon
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);


        // Initialize RecyclerView and setup adapter
        recyclerView = (RecyclerView) findViewById(R.id.messages_container);
        adapter = new ChatAdapter(this, messageList);

        layoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setDistanceToTriggerSync(200);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        messageInput = findViewById(R.id.message_input);
        progressBarWrapper = findViewById(R.id.progressBarWrapper);
        sendMessageButton = findViewById(R.id.sendButton);

        refreshData();
    }

    private void refreshData() {
        Call<Trip> call = tripService.getTrip("Bearer " + prefs.getString("knight-rider-token",null), tripId); // Fill our request template

        // Make request and setup callback to handle response
        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                generateMessageList(response.body());
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Unable to load ride information.", Toast.LENGTH_SHORT).show();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    public void sendMessage(View view) {
        String message = messageInput.getText().toString();

        if (message.isEmpty()) {
            Toast.makeText(this, "Empty message", Toast.LENGTH_SHORT).show();
        } else {
            sendMessageButton.setVisibility(View.GONE);
            progressBarWrapper.setVisibility(View.VISIBLE);
            Integer userId = Integer.parseInt(prefs.getString("knight-rider-userid", null));

            MessageBody body = new MessageBody(message, tripId, userId);
            Call<ResponseBody> call = messageService.createMessage("Bearer " + prefs.getString("knight-rider-token", null), tripId, userId, body);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    sendMessageButton.setVisibility(View.VISIBLE);
                    progressBarWrapper.setVisibility(View.GONE);
                    refreshData();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(ChatActivity.this, "Unidentified error occurred", Toast.LENGTH_SHORT).show();
                    sendMessageButton.setVisibility(View.VISIBLE);
                    progressBarWrapper.setVisibility(View.GONE);
                    refreshData();
                }
            });
        }
    }

    private void generateMessageList(Trip ride) {
        setTitle(ride.getOriginCity() + " to " + ride.getDestCity());
        Collections.sort(ride.getMessages(), new Comparator<Message>() {
            @Override
            public int compare(Message message, Message m1) {
                long diff1 = Math.abs(message.getLogDate().getTime() - now);
                long diff2 = Math.abs(m1.getLogDate().getTime() - now);
                return Long.compare(diff1, diff2);
            }
        });

        messageList.clear();

        for (Message m:ride.getMessages()) {
            messageList.add(m);
        }

        adapter.notifyDataSetChanged();
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
