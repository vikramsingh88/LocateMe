package com.vikram.locateme.firbase;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager mSharedPrefManager;
    private Context mContext;

    public static final String TOKEN_KEY = "token";
    public static final String FCM_SHARED_PREF = "fcmsharedpref";

    private SharedPrefManager(Context context) {
        this.mContext = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mSharedPrefManager == null) {
            mSharedPrefManager = new SharedPrefManager(context);
        }
        return mSharedPrefManager;
    }

    public boolean storeToken(String token) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FCM_SHARED_PREF,mContext.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(TOKEN_KEY, token);
        edit.apply();
        return true;
    }

    public String getToken() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FCM_SHARED_PREF,mContext.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_KEY, null);
    }
}
