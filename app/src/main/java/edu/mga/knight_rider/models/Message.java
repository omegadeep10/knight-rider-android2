package edu.mga.knight_rider.models;

import java.util.Date;

public class Message {
    private int id;
    private Date logDate;
    private String comment;
    private int tripId;
    private int userId;
    private String firstName;
    private String lastName;
    private String profilePicture;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Message(int id, Date logDate, String comment, int tripId, int userId, String firstName, String lastName, String profilePicture) {

        this.id = id;
        this.logDate = logDate;
        this.comment = comment;
        this.tripId = tripId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
    }
}
