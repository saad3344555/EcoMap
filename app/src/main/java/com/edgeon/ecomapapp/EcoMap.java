package com.edgeon.ecomapapp;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;

import io.fabric.sdk.android.Fabric;

public class EcoMap extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        FacebookSdk.sdkInitialize(getApplicationContext());

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}