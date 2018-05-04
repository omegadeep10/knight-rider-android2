package edu.mga.knight_rider.network;

import java.util.ArrayList;

import edu.mga.knight_rider.models.Message;
import edu.mga.knight_rider.models.requests.MessageBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageDataService {
    @GET("messages/{tripid}")
    Call<ArrayList<Message>> getMessages(@Header("X-Authorization") String token, @Path("tripid") Integer tripid);

    @POST("messages/{tripid}/{userid}")
    Call<ResponseBody> createMessage(@Header("X-Authorization") String token, @Path("tripid") Integer tripid, @Path("userid") Integer userid, @Body MessageBody message);
}
