package edu.mga.knight_rider.network;

import java.util.ArrayList;

import edu.mga.knight_rider.models.Trip;
import edu.mga.knight_rider.models.requests.JoinTripBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TripDataService {
    @GET("trips")
    Call<ArrayList<Trip>> getAllTrips(@Header("X-Authorization") String token);

    @GET("trips/{tripid}")
    Call<Trip> getTrip(@Header("X-Authorization") String token, @Path("tripid") int tripid);

    @GET("users/{userid}/trips")
    Call<ArrayList<Trip>> getUserTrips(@Header("X-Authorization") String token, @Path("userid") String userid);

    @DELETE("trips/{tripid}")
    Call<ResponseBody> deleteTrip(@Header("X-Authorization") String token, @Path("tripid") int tripid);

    @DELETE("passengers/{tripid}/{userid}")
    Call<ResponseBody> leaveTrip(@Header("X-Authorization") String token, @Path("tripid") int tripid, @Path("userid") String userid);

    @POST("passengers/{tripid}/{userid}")
    Call<ResponseBody> joinTrip(@Header("X-Authorization") String token, @Path("tripid") Integer tripid, @Path("userid") Integer userid, @Body JoinTripBody tripBody);
}
