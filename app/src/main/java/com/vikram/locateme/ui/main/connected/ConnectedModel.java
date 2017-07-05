package com.vikram.locateme.ui.main.connected;

import android.util.Log;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.requestmodel.ConnectedRequestObject;
import com.vikram.locateme.appdataserver.requests.RequestConnectedContacts;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by M1032130 on 7/1/2017.
 */

public class ConnectedModel implements IConnectedModel {
    @Override
    public void getConnectedContacts(String authToken, final OnConnectedContactReceivedListener listener) {
        new RequestConnectedContacts(new ConnectedRequestObject(authToken), new AppDataClient
                .OnDataReceived<ConnectedResponse>() {

            @Override
            public void onDataSuccess(ConnectedResponse response) {
                Log.d("getConnectedContacts", "onDataSuccess " + response.getStatusMessage());
                listener.onConnectedContactReceived(new ArrayList<>(Arrays.asList(response.getData())));
            }

            @Override
            public void onDataFailure(String error) {
                Log.d("getConnectedContacts", "onDataFailure " + error.toString());
                listener.onConnectedContactError(error);
            }
        }).callService();
    }
}
