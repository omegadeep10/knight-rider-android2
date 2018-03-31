package edu.mga.knight_rider.adapters;

import android.content.Context;
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

    public TripAdapter(Context context, ArrayList<Trip> rideList) {
        this.context = context;
        this.rideList = rideList;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trip trip = rideList.get(position);
        SimpleDateFormat dt = new SimpleDateFormat("MMM dd YYYY - hh:mm a");
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

        public ViewHolder(View itemView) {
            super(itemView);
            cardTitle = (TextView) itemView.findViewById(R.id.cardTitle);
            departureTime = (TextView) itemView.findViewById(R.id.departureTime);
            meetingLocation = (TextView) itemView.findViewById(R.id.meetingLocation);
            dropoffLocation = (TextView) itemView.findViewById(R.id.dropoffLocation);
            picsWrapper = (LinearLayout) itemView.findViewById(R.id.pics_wrapper);
        }
    }
}
