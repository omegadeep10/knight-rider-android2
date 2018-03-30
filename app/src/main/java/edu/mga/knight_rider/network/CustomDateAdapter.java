package edu.mga.knight_rider.network;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.util.Date;

/**
 * Created by omega on 025, Mar, 25.
 *
 * This is Date adapter so that GSON can parse the Unix time dates
 */

public class CustomDateAdapter extends TypeAdapter<Date> {

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null)
            out.nullValue();
        else
            out.value(value.getTime() / 1000);
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in != null)
            return new Date(in.nextLong() * 1000);
        else
            return null;
    }
}
