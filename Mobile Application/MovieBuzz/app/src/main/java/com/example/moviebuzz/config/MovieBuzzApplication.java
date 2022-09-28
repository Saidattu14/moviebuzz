package com.example.moviebuzz.config;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import dagger.hilt.android.HiltAndroidApp;
import leakcanary.AppWatcher;
import leakcanary.LeakCanary;

@HiltAndroidApp
public class MovieBuzzApplication extends Application {

    private static MovieBuzzApplication instance;
    private static Context appContext;
    public static MovieBuzzApplication getInstance()
    {
        return instance;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context mAppContext) {
        this.appContext = mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.Config config = LeakCanary.getConfig().newBuilder()
                .retainedVisibleThreshold(1)
                .build();
        LeakCanary.setConfig(config);
        instance = this;
        this.setAppContext(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

}
