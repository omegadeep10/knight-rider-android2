package edu.mga.knight_rider.models;

/**
 * Created by omega on 022, Mar, 22.
 */

public class Trip {
    private int id;
    private String originAddress;
    private String originCity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginAddress() {
        return originAddress;
    }

    public void setOriginAddress(String originAddress) {
        this.originAddress = originAddress;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public Trip(int id, String originAddress, String originCity) {


        this.id = id;
        this.originAddress = originAddress;
        this.originCity = originCity;
    }
}
