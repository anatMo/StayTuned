package com.example.staytuned.activities;

import android.app.Application;
import android.content.res.Resources;

import com.example.staytuned.adapters.Event_Adapter;
import com.example.staytuned.utils.MyFireBaseAuthontication;
import com.example.staytuned.utils.MyFirebaseDB;
import com.example.staytuned.utils.MyGoogleMapsPlaces;

public class App extends Application {
    private static App mInstance;
    private static Resources res;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        res = getResources();
        MyFireBaseAuthontication.initHelper(this);
        MyGoogleMapsPlaces.initHelper(this);
        MyFirebaseDB.initHelper(this);
        Event_Adapter.initHelper(this);
    }

    public static App getInstance() {
        return mInstance;
    }

    public static Resources getRes() {
        return res;
    }

}