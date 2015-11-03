package org.wearableapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class App extends Application {

    private static Application application;
    private static SharedPreferences sharedPreferences;
    private static final String USER_FILE = "user_data";

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        sharedPreferences = getSharedPreferences(USER_FILE, MODE_PRIVATE);
    }

    public static Context getContext() {
        return application.getApplicationContext();
    }

    public static SharedPreferences getPreferences() {
        return sharedPreferences;
    }
}
