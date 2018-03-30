package edu.mga.knight_rider.network;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.mga.knight_rider.models.Trip;
import edu.mga.knight_rider.models.TripList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface GetTripDataService {
    @GET("trips")
    Call<ArrayList<Trip>> getAllTrips(@Header("X-Authorization") String token);

    @GET("users/{userid}/trips")
    Call<ArrayList<Trip>> getUserTrips(@Header("X-Authorization") String token, @Path("userid") String userid);
}
