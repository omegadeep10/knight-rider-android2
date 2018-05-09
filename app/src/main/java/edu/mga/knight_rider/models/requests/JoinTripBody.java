package edu.mga.knight_rider.models.requests;

public class JoinTripBody {
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public JoinTripBody(int userId, int tripId, Double latitude, Double longitude, String address) {

        this.userId = userId;
        this.tripId = tripId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    private int tripId;
    private Double latitude;
    private Double longitude;
    private String address;
}
