package com.vikram.locateme.ui.main.approve;

import android.util.Log;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.RetroResponse;
import com.vikram.locateme.appdataserver.requestmodel.ApproveRequestObject;
import com.vikram.locateme.appdataserver.requestmodel.ConnectRequestObject;
import com.vikram.locateme.appdataserver.requests.RequestApproveContacts;
import com.vikram.locateme.appdataserver.requests.RequestConnection;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by M1032130 on 7/1/2017.
 */

public class ApproveModel implements IApproveModel {

    @Override
    public void getApproveContacts(String authToken, final OnApproveContactReceivedListener listener) {
        new RequestApproveContacts(new ApproveRequestObject(authToken), new AppDataClient
                .OnDataReceived<ApproveResponse>() {

            @Override
            public void onDataSuccess(ApproveResponse response) {
                Log.d("getPendingContacts", "onDataSuccess " + response.getStatusMessage());
                listener.onApproveContactReceived(new ArrayList<>(Arrays.asList(response.getData())));
            }

            @Override
            public void onDataFailure(String error) {
                Log.d("getPendingContacts", "onDataFailure " + error.toString());
                listener.onApproveContactError(error);
            }
        }).callService();
    }

    @Override
    public void connect(String approve, String authToken, final OnConnectionListener listener) {
        new RequestConnection(new ConnectRequestObject(approve, authToken), new AppDataClient
                .OnDataReceived<RetroResponse>() {

            @Override
            public void onDataSuccess(RetroResponse response) {
                Log.d("connect", "onDataSuccess " + response.getStatusMessage());
                listener.onConnection(response.getMessage());
            }

            @Override
            public void onDataFailure(String error) {
                Log.d("connect", "onDataFailure " + error.toString());
                listener.onConnectionError(error);
            }
        }).callService();
    }
}
