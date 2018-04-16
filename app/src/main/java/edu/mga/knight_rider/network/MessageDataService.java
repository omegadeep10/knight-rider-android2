package edu.mga.knight_rider.network;

import java.util.ArrayList;

import edu.mga.knight_rider.models.Message;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface MessageDataService {
    @GET("messages/{tripid}")
    Call<ArrayList<Message>> getMessages(@Header("X-Authorization") String token, @Path("tripid") Integer tripid);
}
