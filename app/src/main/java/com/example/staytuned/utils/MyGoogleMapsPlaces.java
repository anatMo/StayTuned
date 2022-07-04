package com.example.staytuned.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.libraries.places.api.Places;

import java.util.Locale;

public class MyGoogleMapsPlaces {
    private Context context;
    private MyGoogleMapsPlaces(Context context) {
        this.context = context;
    }

    private static MyGoogleMapsPlaces me;

    public static MyGoogleMapsPlaces getMe() {
        return me;
    }

    public static MyGoogleMapsPlaces initHelper(Context context) {
        Log.d("pttt", "INITMyGoogleMapsPlaces");
        if (me == null) {
            me = new MyGoogleMapsPlaces(context);
        }
        return me;
    }

    public void initPlaces(){
        if (!Places.isInitialized()) {
            ApplicationInfo app = null;
            try {
                app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                Bundle bundle = app.metaData;

                Places.initialize(context, bundle.getString("com.google.android.geo.API_KEY"), Locale.US);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

}
