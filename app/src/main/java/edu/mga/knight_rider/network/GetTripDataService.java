package edu.mga.knight_rider.network;

import java.util.ArrayList;

import edu.mga.knight_rider.models.Trip;
import edu.mga.knight_rider.models.TripList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GetTripDataService {
    @GET("trips")
    Call<ArrayList<Trip>> getTripData(@Header("X-Authorization") String token);
}
