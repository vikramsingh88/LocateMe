package com.vikram.locateme.utils;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by M1032130 on 6/21/2017.
 */

public class LocateApplication extends Application {
    private static LocateApplication mLocateApplication;
    @Override
    public void onCreate() {
        super.onCreate();

        mLocateApplication = this;
    }

    public static LocateApplication getAppContext() {
        return mLocateApplication;
    }
}
