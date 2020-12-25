package com.iamretailer;

import android.app.Application;
import android.content.Context;


import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.DatabaseManager;
import com.iamretailer.Common.LocaleHelper;
import com.iamretailer.Common.MyNotificationOpenedHandler;
import com.iamretailer.Common.MyNotificationReceivedHandler;
import com.onesignal.OneSignal;

import io.fabric.sdk.android.Fabric;


public class AppClass extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        context = getApplicationContext();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        DatabaseManager.initializeInstance(new DBController(getApplicationContext()));
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                .setNotificationReceivedHandler(new MyNotificationReceivedHandler())
                .init();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}