package com.vikram.locateme.ui.main.pending;

import android.util.Log;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.requestmodel.PendingRequestObject;
import com.vikram.locateme.appdataserver.requests.RequestPendingContacts;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by M1032130 on 6/30/2017.
 */

public class PendingModel implements IPendingModel{

    @Override
    public void getPendingContacts(String authToken, final OnPendingContactReceivedListener listener) {
        new RequestPendingContacts(new PendingRequestObject(authToken), new AppDataClient
                .OnDataReceived<PendingResponse>() {

            @Override
            public void onDataSuccess(PendingResponse response) {
                Log.d("getPendingContacts", "onDataSuccess " + response.getStatusMessage());
                listener.onPendingContactReceived(new ArrayList<>(Arrays.asList(response.getData())));
            }

            @Override
            public void onDataFailure(String error) {
                Log.d("getPendingContacts", "onDataFailure " + error.toString());
                listener.onPendingContactError(error);
            }
        }).callService();
    }
}
