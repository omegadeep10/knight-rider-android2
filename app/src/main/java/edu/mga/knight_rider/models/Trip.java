package edu.mga.knight_rider.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by omega on 022, Mar, 22.
 */

public class Trip {
    private int id;
    private int carId;
    private int driverId;
    private String originAddress;
    private String originCity;
    private String originState;
    private String originZip;
    private Double originLatitude;
    private Double originLongitude;
    private String destName;
    private String destAddress;
    private String destCity;
    private String destState;
    private String destZip;
    private Double destLatitude;
    private Double destLongitude;
    private Date departureTime;
    private String meetingLocation;
    private Double meetingLatitude;
    private Double meetingLongitude;
    private Integer availableSeats;
    private Integer remainingSeats;
    private Double currentLatitude;
    private Double currentLongitude;
    private Boolean completed;
    private Date completedTimestamp;
    private Driver driver;
    private ArrayList<Passenger> passengers;

    public Trip(int id, int carId, int driverId, String originAddress, String originCity, String originState, String originZip, Double originLatitude, Double originLongitude, String destName, String destAddress, String destCity, String destState, String destZip, Double destLatitude, Double destLongitude, Date departureTime, String meetingLocation, Double meetingLatitude, Double meetingLongitude, Integer availableSeats, Integer remainingSeats, Double currentLatitude, Double currentLongitude, Boolean completed, Date completedTimestamp, Driver driver, ArrayList<Passenger> passengers) {
        this.id = id;
        this.carId = carId;
        this.driverId = driverId;
        this.originAddress = originAddress;
        this.originCity = originCity;
        this.originState = originState;
        this.originZip = originZip;
        this.originLatitude = originLatitude;
        this.originLongitude = originLongitude;
        this.destName = destName;
        this.destAddress = destAddress;
        this.destCity = destCity;
        this.destState = destState;
        this.destZip = destZip;
        this.destLatitude = destLatitude;
        this.destLongitude = destLongitude;
        this.departureTime = departureTime;
        this.meetingLocation = meetingLocation;
        this.meetingLatitude = meetingLatitude;
        this.meetingLongitude = meetingLongitude;
        this.availableSeats = availableSeats;
        this.remainingSeats = remainingSeats;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.completed = completed;
        this.completedTimestamp = completedTimestamp;
        this.driver = driver;
        this.passengers = passengers;
    }

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
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

    public String getOriginState() {
        return originState;
    }

    public void setOriginState(String originState) {
        this.originState = originState;
    }

    public String getOriginZip() {
        return originZip;
    }

    public void setOriginZip(String originZip) {
        this.originZip = originZip;
    }

    public Double getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLatitude(Double originLatitude) {
        this.originLatitude = originLatitude;
    }

    public Double getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLongitude(Double originLongitude) {
        this.originLongitude = originLongitude;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    public String getDestCity() {
        return destCity;
    }

    public void setDestCity(String destCity) {
        this.destCity = destCity;
    }

    public String getDestState() {
        return destState;
    }

    public void setDestState(String destState) {
        this.destState = destState;
    }

    public String getDestZip() {
        return destZip;
    }

    public void setDestZip(String destZip) {
        this.destZip = destZip;
    }

    public Double getDestLatitude() {
        return destLatitude;
    }

    public void setDestLatitude(Double destLatitude) {
        this.destLatitude = destLatitude;
    }

    public Double getDestLongitude() {
        return destLongitude;
    }

    public void setDestLongitude(Double destLongitude) {
        this.destLongitude = destLongitude;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public Double getMeetingLatitude() {
        return meetingLatitude;
    }

    public void setMeetingLatitude(Double meetingLatitude) {
        this.meetingLatitude = meetingLatitude;
    }

    public Double getMeetingLongitude() {
        return meetingLongitude;
    }

    public void setMeetingLongitude(Double meetingLongitude) {
        this.meetingLongitude = meetingLongitude;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Integer getRemainingSeats() {
        return remainingSeats;
    }

    public void setRemainingSeats(Integer remainingSeats) {
        this.remainingSeats = remainingSeats;
    }

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Date getCompletedTimestamp() {
        return completedTimestamp;
    }

    public void setCompletedTimestamp(Date completedTimestamp) {
        this.completedTimestamp = completedTimestamp;
    }
}
