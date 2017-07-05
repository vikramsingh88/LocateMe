package com.vikram.locateme.appdataserver.requests;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.IAppDataAPIs;
import com.vikram.locateme.appdataserver.RetroResponse;
import com.vikram.locateme.appdataserver.requestmodel.LocationRequestObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by M1032130 on 6/30/2017.
 */

public class RequestLocationAccess<T> {

    private final AppDataClient.OnDataReceived mCallBack;
    private String mCacheKey = getClass().getSimpleName();
    private final LocationRequestObject mLocationRequestObject;
    IAppDataAPIs mClient;
    AppDataClient mService;

    public RequestLocationAccess(LocationRequestObject locationRequestObject, AppDataClient.OnDataReceived<T> callback) {
        mClient = AppDataClient.getClient();
        mService = AppDataClient.getService();

        this.mLocationRequestObject = locationRequestObject;

        mCacheKey += this.mLocationRequestObject.getContact();
        mCacheKey += this.mLocationRequestObject.getToken();
        mCallBack = callback;
    }

    public void callService() {
        Call<RetroResponse> call = mClient.requestLocationAccess(mLocationRequestObject);

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