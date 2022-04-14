package com.mcmaintank.geocache.data.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class GeocacheInfoShp {
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String PREFERENCES_FILE = "PREFERENCES_FILE_GEOCACHE";

    public static String getLatitude(Context context) {
        return getUserPreference(context).getString(LATITUDE, "");
    }

    @SuppressLint("CommitPrefEdits")
    public static void setLatitude(Context context, String latitude) {
        getUserPreference(context).edit().putString(LATITUDE, latitude).apply();
    }

    public static String getLongitude(Context context) {
        return getUserPreference(context).getString(LONGITUDE, "");
    }

    @SuppressLint("CommitPrefEdits")
    public static void setLongitude(Context context, String longitude) {
        getUserPreference(context).edit().putString(LONGITUDE, longitude).apply();
    }
    /**
     *重点在这里，调用context的getSharedPreferences方法即可获取shp，第一个参数是shp对应的xml文件名，第二个参数是模式。
     */
    private static SharedPreferences getUserPreference(Context context) {
        return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }
}
