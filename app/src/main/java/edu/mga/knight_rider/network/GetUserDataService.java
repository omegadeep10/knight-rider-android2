package edu.mga.knight_rider.network;

import java.util.ArrayList;

import edu.mga.knight_rider.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface GetUserDataService {
    @GET("users/{userid}")
    Call<User> getUser(@Header("X-Authorization") String token, @Path("userid") String userid);
}
