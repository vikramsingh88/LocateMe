package com.vikram.locateme.appdataserver.requests;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.IAppDataAPIs;
import com.vikram.locateme.appdataserver.RetroResponse;
import com.vikram.locateme.appdataserver.requestmodel.ConnectRequestObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by M1032130 on 7/1/2017.
 */

public class RequestConnection<T> {

    private final AppDataClient.OnDataReceived mCallBack;
    private String mCacheKey = getClass().getSimpleName();
    private final ConnectRequestObject mConnectRequestObject;
    IAppDataAPIs mClient;
    AppDataClient mService;

    public RequestConnection(ConnectRequestObject connectRequestObject, AppDataClient.OnDataReceived<T> callback) {
        mClient = AppDataClient.getClient();
        mService = AppDataClient.getService();

        this.mConnectRequestObject = connectRequestObject;

        mCacheKey += this.mConnectRequestObject.getToken();
        mCacheKey += this.mConnectRequestObject.getApprove();

        mCallBack = callback;
    }

    public void callService() {
        Call<RetroResponse> call = mClient.requestConnect(mConnectRequestObject);

        call.enqueue(new Callback<RetroResponse>() {
            @Override
            public void onResponse(Call<RetroResponse> call,
                                   Response<RetroResponse> response) {
                mService.onResponse(response, mCallBack, mCacheKey);
            }

            @Override
            public void onFailure(Call<RetroResponse> call, Throwable t) {
                mCallBack.onDataFailure(t.getMessage());
            }
        });
    }

}