package edu.mga.knight_rider.models;

import java.util.ArrayList;

/**
 * Created by omega on 024, Mar, 24.
 */

public class TripList {
    private ArrayList<Trip> triplist;

    public ArrayList<Trip> getTripList() {
        return triplist;
    }
    public TripList(ArrayList<Trip> triplist) {
        this.triplist = triplist;
    }
}
