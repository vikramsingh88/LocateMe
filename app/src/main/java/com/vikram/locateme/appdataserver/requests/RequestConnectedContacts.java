package com.vikram.locateme.appdataserver.requests;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.IAppDataAPIs;
import com.vikram.locateme.appdataserver.requestmodel.ConnectedRequestObject;
import com.vikram.locateme.ui.main.connected.ConnectedResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by M1032130 on 7/1/2017.
 */

public class RequestConnectedContacts<T> {

    private final AppDataClient.OnDataReceived mCallBack;
    private String mCacheKey = getClass().getSimpleName();
    private final ConnectedRequestObject mConnectedRequestObject;
    IAppDataAPIs mClient;
    AppDataClient mService;

    public RequestConnectedContacts(ConnectedRequestObject connectedRequestObject, AppDataClient.OnDataReceived<T> callback) {
        mClient = AppDataClient.getClient();
        mService = AppDataClient.getService();

        this.mConnectedRequestObject = connectedRequestObject;

        mCacheKey += this.mConnectedRequestObject.getToken();

        mCallBack = callback;
    }

    public void callService() {
        Call<ConnectedResponse> call = mClient.requestConnectedContacts(mConnectedRequestObject);

        call.enqueue(new Callback<ConnectedResponse>() {
            @Override
            public void onResponse(Call<ConnectedResponse> call,
                                   Response<ConnectedResponse> response) {
                mService.onResponse(response, mCallBack, mCacheKey);
            }

            @Override
            public void onFailure(Call<ConnectedResponse> call, Throwable t) {
                mCallBack.onDataFailure(t.getMessage());
            }
        });
    }

}