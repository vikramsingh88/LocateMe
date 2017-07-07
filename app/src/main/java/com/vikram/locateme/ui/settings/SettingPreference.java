package com.vikram.locateme.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.vikram.locateme.firbase.SharedPrefManager;

/**
 * Created by M1032130 on 7/6/2017.
 */

public class SettingPreference {

    private static SettingPreference mSettingPreference;
    private Context mContext;

    public static final String LOCATION_UPDATE_INTERVAL = "location_update_interval";
    public static final String LOCATION_VISIBILITY = "location_visibility";
    public static final String SETTINGS_SHARED_PREF = "setting-pref";

    private SettingPreference(Context context) {
        this.mContext = context;
    }

    public static synchronized SettingPreference getInstance(Context context) {
        if (mSettingPreference == null) {
            mSettingPreference = new SettingPreference(context);
        }
        return mSettingPreference;
    }

    public boolean setLocationInterval(long interval) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SETTINGS_SHARED_PREF, mContext.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong(LOCATION_UPDATE_INTERVAL, interval);
        edit.apply();
        return true;
    }

    public long getLocationInterval() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SETTINGS_SHARED_PREF, mContext.MODE_PRIVATE);
        return sharedPreferences.getLong(LOCATION_UPDATE_INTERVAL, 10);
    }

    public boolean setLocationVisibility(String visibility) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SETTINGS_SHARED_PREF, mContext.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(LOCATION_VISIBILITY, visibility);
        edit.apply();
        return true;
    }

    public String getLocationVisibility() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SETTINGS_SHARED_PREF, mContext.MODE_PRIVATE);
        return sharedPreferences.getString(LOCATION_VISIBILITY, null);
    }
}
