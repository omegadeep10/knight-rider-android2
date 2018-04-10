package edu.mga.knight_rider.network;

import java.util.ArrayList;

import edu.mga.knight_rider.models.Trip;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface TripDataService {
    @GET("trips")
    Call<ArrayList<Trip>> getAllTrips(@Header("X-Authorization") String token);

    @GET("users/{userid}/trips")
    Call<ArrayList<Trip>> getUserTrips(@Header("X-Authorization") String token, @Path("userid") String userid);

    @DELETE("trips/{tripid}")
    Call<ResponseBody> deleteTrip(@Header("X-Authorization") String token, @Path("tripid") int tripid);
}
