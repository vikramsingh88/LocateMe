package com.vikram.locateme.ui.settings;

import android.util.Log;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.RetroResponse;
import com.vikram.locateme.appdataserver.requestmodel.LocationVisibilityRequestObject;
import com.vikram.locateme.appdataserver.requests.RequestLocationVisibility;

/**
 * Created by M1032130 on 7/6/2017.
 */

public class SettingsModule implements ISettingsModel{
    @Override
    public void updateLocationVisibility(String token, String visibility, final LocationVisibilityListener listener) {
        new RequestLocationVisibility(new LocationVisibilityRequestObject(token, visibility), new AppDataClient
                .OnDataReceived<RetroResponse>() {

            @Override
            public void onDataSuccess(RetroResponse response) {
                Log.d("updateVisibility", "onDataSuccess " + response.getStatusMessage());
                listener.locationVisibilityUpdated();
            }

            @Override
            public void onDataFailure(String error) {
                Log.d("updateVisibility", "onDataFailure " + error.toString());
                listener.locationVisibilityUpdateError(error);
            }
        }).callService();
    }
}
