package edu.mga.knight_rider.models.requests;

public class MessageBody {
    private String comment;
    private Integer tripId;
    private Integer userId;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getTripId() {
        return tripId;
    }

    public void setTripId(Integer tripId) {
        this.tripId = tripId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public MessageBody(String comment, Integer tripId, Integer userId) {

        this.comment = comment;
        this.tripId = tripId;
        this.userId = userId;
    }
}
