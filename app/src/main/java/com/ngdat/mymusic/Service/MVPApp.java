package com.ngdat.mymusic.Service;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class MVPApp extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context mContextApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mContextApp = this;
    }

    public static Context getmContextApp() {
        return mContextApp;
    }
}