package com.vikram.locateme.appdataserver.requests;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.IAppDataAPIs;
import com.vikram.locateme.appdataserver.RetroResponse;
import com.vikram.locateme.appdataserver.requestmodel.ShareLocationRequestObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by M1032130 on 7/5/2017.
 */

public class RequestShareLocation<T> {

    private final AppDataClient.OnDataReceived mCallBack;
    private String mCacheKey = getClass().getSimpleName();
    private final ShareLocationRequestObject mShareLocationRequestObject;
    IAppDataAPIs mClient;
    AppDataClient mService;

    public RequestShareLocation(ShareLocationRequestObject shareLocationRequestObject, AppDataClient.OnDataReceived<T> callback) {
        mClient = AppDataClient.getClient();
        mService = AppDataClient.getService();

        this.mShareLocationRequestObject = shareLocationRequestObject;

        mCacheKey += this.mShareLocationRequestObject.getToken();
        mCacheKey += this.mShareLocationRequestObject.getContact();
        mCacheKey += this.mShareLocationRequestObject.getLocation();

        mCallBack = callback;
    }

    public void callService() {
        Call<RetroResponse> call = mClient.requestShareLocation(mShareLocationRequestObject);

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