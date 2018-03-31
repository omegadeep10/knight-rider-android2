package edu.mga.knight_rider.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by omega on 024, Mar, 24.
 */

public class RetrofitInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://knightrider.mgaitec.net:8080/ridesharing/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new CustomDateAdapter())
                .create();

            retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        }
        return retrofit;
    }
}


