package com.vikram.locateme.ui.location;

import android.util.Log;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.RetroResponse;
import com.vikram.locateme.appdataserver.requestmodel.GetLocationRequestObject;
import com.vikram.locateme.appdataserver.requestmodel.ShareLocationRequestObject;
import com.vikram.locateme.appdataserver.requests.RequestGetLocationData;
import com.vikram.locateme.appdataserver.requests.RequestShareLocation;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by M1032130 on 7/3/2017.
 */

public class LocationModel implements ILocationModel {
    @Override
    public void getLocation(String token, String contact, final OnLocationReceivedListener listener) {
        new RequestGetLocationData(new GetLocationRequestObject(token, contact), new AppDataClient
                .OnDataReceived<LocationResponse>() {

            @Override
            public void onDataSuccess(LocationResponse response) {
                Log.d("getConnectedContacts", "onDataSuccess " + response.getStatusMessage());
                listener.onLocation(new ArrayList<>(Arrays.asList(response.getData())));
            }

            @Override
            public void onDataFailure(String error) {
                Log.d("getConnectedContacts", "onDataFailure " + error.toString());
                listener.noLocation(error);
            }
        }).callService();
    }

    @Override
    public void shareLocation(String token, String contact, String location, final OnLocationShareListener listener) {
        new RequestShareLocation(new ShareLocationRequestObject(token, contact, location), new AppDataClient
                .OnDataReceived<RetroResponse>() {

            @Override
            public void onDataSuccess(RetroResponse response) {
                Log.d("shareLocation", "onDataSuccess " + response.getStatusMessage());
                listener.onLocationShare(response.getMessage());
            }

            @Override
            public void onDataFailure(String error) {
                Log.d("shareLocation", "onDataFailure " + error.toString());
                listener.onLocationShare(error);
            }
        }).callService();
    }
}
