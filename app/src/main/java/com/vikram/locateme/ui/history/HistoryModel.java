package com.vikram.locateme.ui.history;

import android.util.Log;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.requestmodel.GetLocationRequestObject;
import com.vikram.locateme.appdataserver.requests.RequestLocationHistory;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by M1032130 on 7/8/2017.
 */

public class HistoryModel implements IHistoryModel {
    @Override
    public void getLocationHistory(String token, String contact, final OnLocationHistoryListener listener) {

        new RequestLocationHistory<>(new GetLocationRequestObject(token, contact), new AppDataClient
                .OnDataReceived<LocationHistoryResponse>() {

            @Override
            public void onDataSuccess(LocationHistoryResponse response) {
                Log.d("getLocationHistory", "onDataSuccess " + response.getStatusMessage());
                listener.receivedLocations(new ArrayList<>(Arrays.asList(response.getData())));
            }

            @Override
            public void onDataFailure(String error) {
                Log.d("getLocationHistory", "onDataFailure " + error.toString());
                listener.error(error);
            }
        }).callService();
    }
}
