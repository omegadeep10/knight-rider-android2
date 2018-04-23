package edu.mga.knight_rider.models.requests;

public class MessageBody {
    private String comment;
    private String tripId;
    private String userId;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MessageBody(String comment, String tripId, String userId) {

        this.comment = comment;
        this.tripId = tripId;
        this.userId = userId;
    }
}
