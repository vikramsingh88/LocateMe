package com.vikram.locateme.appdataserver.requests;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.IAppDataAPIs;
import com.vikram.locateme.appdataserver.RetroResponse;
import com.vikram.locateme.appdataserver.requestmodel.LocationVisibilityRequestObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by M1032130 on 7/7/2017.
 */

public class RequestLocationVisibility<T> {

    private final AppDataClient.OnDataReceived mCallBack;
    private String mCacheKey = getClass().getSimpleName();
    private final LocationVisibilityRequestObject mLocationVisibilityRequestObject;
    IAppDataAPIs mClient;
    AppDataClient mService;

    public RequestLocationVisibility(LocationVisibilityRequestObject locationVisibilityRequestObject, AppDataClient.OnDataReceived<T> callback) {
        mClient = AppDataClient.getClient();
        mService = AppDataClient.getService();

        this.mLocationVisibilityRequestObject = locationVisibilityRequestObject;

        mCacheKey += this.mLocationVisibilityRequestObject.getToken();
        mCacheKey += this.mLocationVisibilityRequestObject.getVisibility();

        mCallBack = callback;
    }

    public void callService() {
        Call<RetroResponse> call = mClient.requestHideLocation(mLocationVisibilityRequestObject);

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