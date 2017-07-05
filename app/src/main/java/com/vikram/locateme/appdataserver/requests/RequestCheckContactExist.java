package com.vikram.locateme.appdataserver.requests;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.IAppDataAPIs;
import com.vikram.locateme.appdataserver.RetroResponse;
import com.vikram.locateme.appdataserver.requestmodel.CheckUserRequestObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by M1032130 on 6/29/2017.
 */

public class RequestCheckContactExist<T> {

    private final AppDataClient.OnDataReceived mCallBack;
    private String mCacheKey = getClass().getSimpleName();
    private final CheckUserRequestObject mCheckUserRequestObject;
    IAppDataAPIs mClient;
    AppDataClient mService;

    public RequestCheckContactExist(CheckUserRequestObject checkUserRequestObject, AppDataClient.OnDataReceived<T> callback) {
        mClient = AppDataClient.getClient();
        mService = AppDataClient.getService();

        this.mCheckUserRequestObject = checkUserRequestObject;

        mCacheKey += this.mCheckUserRequestObject.getContact();
        mCacheKey += this.mCheckUserRequestObject.getAuthToken();

        mCallBack = callback;
    }

    public void callService() {
        Call<RetroResponse> call = mClient.checkIfContactExist(mCheckUserRequestObject);

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