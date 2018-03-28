package edu.mga.knight_rider.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.mga.knight_rider.MainActivity;
import edu.mga.knight_rider.R;
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
        SimpleDateFormat dt = new SimpleDateFormat("MMM d - h:m a");

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

        public ViewHolder(View itemView) {
            super(itemView);
            cardTitle = (TextView) itemView.findViewById(R.id.cardTitle);
            departureTime = (TextView) itemView.findViewById(R.id.departureTime);
            meetingLocation = (TextView) itemView.findViewById(R.id.meetingLocation);
            dropoffLocation = (TextView) itemView.findViewById(R.id.dropoffLocation);
        }
    }
}
