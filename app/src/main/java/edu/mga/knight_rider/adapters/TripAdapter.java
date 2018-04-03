package edu.mga.knight_rider.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.mga.knight_rider.MainActivity;
import edu.mga.knight_rider.R;
import edu.mga.knight_rider.models.Passenger;
import edu.mga.knight_rider.models.Trip;

/**
 * Created by omega on 022, Mar, 22.
 */

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Trip> rideList;
    private SharedPreferences prefs;

    public TripAdapter(Context context, ArrayList<Trip> rideList) {
        this.context = context;
        this.rideList = rideList;
        this.prefs = context.getSharedPreferences("edu.mga.knightrider", Context.MODE_PRIVATE); // Need prefs to get user-id, which is used to determine if to show leave vs delete/edit
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        generateButtons(holder, position); // Generates MESSAGES LEAVE EDIT buttons as necessary
        Trip trip = rideList.get(position);
        SimpleDateFormat dt = new SimpleDateFormat("MMM dd YYYY - hh:mm a");
        dt.setTimeZone(TimeZone.getDefault());
        holder.picsWrapper.removeAllViews();

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height, height);
        params.setMargins(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()), 0);


        CircleImageView driverPic = new CircleImageView(context);
        driverPic.setCircleBackgroundColor(context.getResources().getColor(R.color.colorComponentGrayBG));
        driverPic.setBorderColor(context.getResources().getColor(R.color.colorPrimary));
        driverPic.setBorderWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()));
        driverPic.setLayoutParams(params);

        Glide.with(context)
            .load(trip.getDriver().getProfilePicture())
            .into(driverPic);

        holder.picsWrapper.addView(driverPic);

        for (Passenger p: trip.getPassengers()) {
            CircleImageView passengerPic = new CircleImageView(context);
            passengerPic.setCircleBackgroundColor(context.getResources().getColor(R.color.colorComponentGrayBG));
            passengerPic.setLayoutParams(params);

            Glide.with(context)
                .load(p.getProfilePicture())
                .into(passengerPic);

            holder.picsWrapper.addView(passengerPic);
        }

        holder.cardTitle.setText(trip.getOriginCity() + " to " + trip.getDestCity());
        holder.departureTime.setText(dt.format(trip.getDepartureTime()));
        holder.meetingLocation.setText(trip.getMeetingLocation());
        holder.dropoffLocation.setText(trip.getMeetingLocation());
    }

    @Override
    public int getItemCount() {
        return rideList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cardTitle;
        public TextView departureTime;
        public TextView meetingLocation;
        public TextView dropoffLocation;
        public LinearLayout picsWrapper;
        public LinearLayout actionsWrapper;

        public ViewHolder(View itemView) {
            super(itemView);
            cardTitle = (TextView) itemView.findViewById(R.id.cardTitle);
            departureTime = (TextView) itemView.findViewById(R.id.departureTime);
            meetingLocation = (TextView) itemView.findViewById(R.id.meetingLocation);
            dropoffLocation = (TextView) itemView.findViewById(R.id.dropoffLocation);
            picsWrapper = (LinearLayout) itemView.findViewById(R.id.pics_wrapper);
            actionsWrapper = (LinearLayout) itemView.findViewById(R.id.actions_wrapper);
        }
    }

    private void generateButtons(ViewHolder holder, int cardPos) {
        int userId = Integer.parseInt(prefs.getString("knight-rider-userid", "-1"));
        Trip currentTrip = rideList.get(cardPos);
        holder.actionsWrapper.removeAllViews();

        // Init shared params object
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // Add messages btn to every card
        TextView messages = new TextView(context);
        messages.setText("MESSAGES");
        messages.setTextSize(10);
        messages.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        messages.setTypeface(null, Typeface.BOLD);
        messages.setLayoutParams(params);
        messages.setPadding(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()), 0);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof MainActivity){
                    ((MainActivity) context).getMessages();
                }
            }
        });

        holder.actionsWrapper.addView(messages);

        if (currentTrip.getDriverId() == userId) {
            // SHOW DELETE, EDIT
            TextView delete = new TextView(context);
            delete.setText("DELETE");
            delete.setTextSize(10);
            delete.setTextColor(Color.parseColor("#A9A9A9"));
            delete.setTypeface(null, Typeface.BOLD);
            delete.setLayoutParams(params);
            delete.setPadding(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()), 0);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(context instanceof MainActivity){
                        ((MainActivity) context).deleteTrip();
                    }
                }
            });

            holder.actionsWrapper.addView(delete);

            // ONLY SHOW EDIT IF TRIP ISN'T COMPLETED - Doesn't make sense to edit a completed trip.
            if (!currentTrip.getCompleted()) {
                TextView edit = new TextView(context);
                edit.setText("EDIT");
                edit.setTextSize(10);
                edit.setTextColor(Color.parseColor("#A9A9A9"));
                edit.setTypeface(null, Typeface.BOLD);
                edit.setLayoutParams(params);
                edit.setPadding(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()), 0);
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(context instanceof MainActivity){
                            ((MainActivity) context).editTrip();
                        }
                    }
                });

                holder.actionsWrapper.addView(edit);
            }

        } else {
            // SHOW LEAVE
            TextView leave = new TextView(context);
            leave.setText("LEAVE");
            leave.setTextSize(10);
            leave.setTextColor(Color.parseColor("#A9A9A9"));
            leave.setTypeface(null, Typeface.BOLD);
            leave.setLayoutParams(params);
            leave.setPadding(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()), 0);
            leave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(context instanceof MainActivity){
                        ((MainActivity) context).leaveTrip();
                    }
                }
            });

            holder.actionsWrapper.addView(leave);
        }
    }
}
