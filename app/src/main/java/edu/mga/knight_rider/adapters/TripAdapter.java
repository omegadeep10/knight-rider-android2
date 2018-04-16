package edu.mga.knight_rider.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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
        SimpleDateFormat dt = new SimpleDateFormat("MMM dd YYYY - hh:mm a", Locale.US);
        SimpleDateFormat dt2 = new SimpleDateFormat("hh:mm a", Locale.US);
        dt2.setTimeZone(TimeZone.getDefault());
        dt.setTimeZone(TimeZone.getDefault());
        holder.picsWrapper.removeAllViews();

        Date currentDate = new Date();

        holder.bannerWrapper.setVisibility(View.GONE);
        if ((trip.getDepartureTime().getTime() - currentDate.getTime()) > 0 && (trip.getDepartureTime().getTime() - currentDate.getTime()) < TimeUnit.DAYS.toMillis(1)) {
            holder.bannerWrapper.setVisibility(View.VISIBLE);
            holder.bannerText.setText("Meet at the " + trip.getMeetingLocation() + " at " + dt2.format(trip.getDepartureTime()));
        }

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(height, height);
        params.setMargins(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()), 0);


        CircleImageView driverPic = new CircleImageView(context);
        driverPic.setCircleBackgroundColor(getColorWrapper(context, R.color.colorComponentGrayBG));
        driverPic.setBorderColor(getColorWrapper(context, R.color.colorPrimary));
        driverPic.setBorderWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()));
        driverPic.setLayoutParams(params);

        Glide.with(context)
            .load(trip.getDriver().getProfilePicture())
            .into(driverPic);

        holder.picsWrapper.addView(driverPic);

        for (Passenger p: trip.getPassengers()) {
            CircleImageView passengerPic = new CircleImageView(context);
            passengerPic.setCircleBackgroundColor(getColorWrapper(context, R.color.colorComponentGrayBG));
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
        public RelativeLayout bannerWrapper;
        public TextView bannerText;

        public ViewHolder(View itemView) {
            super(itemView);
            cardTitle = (TextView) itemView.findViewById(R.id.cardTitle);
            departureTime = (TextView) itemView.findViewById(R.id.departureTime);
            meetingLocation = (TextView) itemView.findViewById(R.id.meetingLocation);
            dropoffLocation = (TextView) itemView.findViewById(R.id.dropoffLocation);
            picsWrapper = (LinearLayout) itemView.findViewById(R.id.pics_wrapper);
            actionsWrapper = (LinearLayout) itemView.findViewById(R.id.actions_wrapper);
            bannerWrapper = (RelativeLayout) itemView.findViewById(R.id.banner_wrapper);
            bannerText = (TextView) itemView.findViewById(R.id.banner_text);
        }
    }

    private void generateButtons(ViewHolder holder, int cardPos) {
        int userId = Integer.parseInt(prefs.getString("knight-rider-userid", "-1"));
        final Trip currentTrip = rideList.get(cardPos);
        holder.actionsWrapper.removeAllViews();

        // Init shared params object
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // Add messages btn to every card
        TextView messages = new TextView(context);
        messages.setText("MESSAGES");
        messages.setTextSize(12);
        messages.setTextColor(getColorWrapper(context, R.color.colorPrimary));
        messages.setTypeface(null, Typeface.BOLD);
        messages.setLayoutParams(params);
        messages.setPadding(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()), 0);
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof MainActivity){
                    ((MainActivity) context).getMessages(currentTrip.getId());
                }
            }
        });

        holder.actionsWrapper.addView(messages);

        if (currentTrip.getDriverId() == userId) {
            // SHOW DELETE, EDIT
            TextView delete = new TextView(context);
            delete.setText("DELETE");
            delete.setTextSize(12);
            delete.setTextColor(Color.parseColor("#A9A9A9"));
            delete.setTypeface(null, Typeface.BOLD);
            delete.setLayoutParams(params);
            delete.setPadding(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()), 0);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(context instanceof MainActivity){
                        ((MainActivity) context).deleteTrip(currentTrip.getId());
                    }
                }
            });

            holder.actionsWrapper.addView(delete);

            // ONLY SHOW EDIT IF TRIP ISN'T COMPLETED - Doesn't make sense to edit a completed trip.
            if (!currentTrip.getCompleted()) {
                TextView edit = new TextView(context);
                edit.setText("EDIT");
                edit.setTextSize(12);
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
            leave.setTextSize(12);
            leave.setTextColor(Color.parseColor("#A9A9A9"));
            leave.setTypeface(null, Typeface.BOLD);
            leave.setLayoutParams(params);
            leave.setPadding(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()), 0);
            leave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(context instanceof MainActivity){
                        ((MainActivity) context).leaveTrip(currentTrip.getId());
                    }
                }
            });

            holder.actionsWrapper.addView(leave);
        }
    }


    // Wrapper since apparently getColor was deprecated on getResources starting v23 or something. This is a wrapper function that calls the correct one based on SDK version
    @SuppressWarnings("deprecation")
    public static int getColorWrapper(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }
}
