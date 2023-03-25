package hu.bme.aut.bkknotifier.feature.stop.data;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public static ArrayList<Alert> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Alert>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Alert> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}