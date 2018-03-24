package edu.mga.knight_rider.models;

/**
 * Created by omega on 022, Mar, 22.
 */

public class Trip {
    private int id;
    private int driverId;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Trip(int id, int driverId, String description) {
        this.id = id;
        this.driverId = driverId;
        this.description = description;
    }
}
