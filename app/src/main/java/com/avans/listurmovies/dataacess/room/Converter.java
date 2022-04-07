package com.avans.listurmovies.dataacess.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class Converter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static int[] fromString(String value) {
        Type arrayType = new TypeToken<int[]>() {
        }.getType();
        return new Gson().fromJson(value, arrayType);
    }

    @TypeConverter
    public static String fromIntArray(int[] array) {
        Gson gson = new Gson();
        String json = gson.toJson(array);
        return json;
    }
}
