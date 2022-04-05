package com.example.geocache.data.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.geocache.data.LoginDataSource;

public class UserInfoShp {
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_PASSWORD = "USER_PASSWORD";
    private static final String PREFERENCES_FILE = "PREFERENCES_FILE_USER";

    public static String getUserName(Context context) {
        return getUserPreference(context).getString(USER_NAME, "");
    }

    @SuppressLint("CommitPrefEdits")
    public static void setUserName(Context context, String userName) {
        getUserPreference(context).edit().putString(USER_NAME, userName).apply();
    }

    public static String getUserPassword(Context context) {
        return getUserPreference(context).getString(USER_PASSWORD, "");
    }

    @SuppressLint("CommitPrefEdits")
    public static void setUserPassword(Context context, String userPassword) {
        getUserPreference(context).edit().putString(USER_PASSWORD, userPassword).apply();
    }
    /**
     *重点在这里，调用context的getSharedPreferences方法即可获取shp，第一个参数是shp对应的xml文件名，第二个参数是模式。
     */
    private static SharedPreferences getUserPreference(Context context) {
        return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }
}