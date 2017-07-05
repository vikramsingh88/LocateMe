package com.vikram.locateme.appdataserver.requests;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.IAppDataAPIs;
import com.vikram.locateme.appdataserver.requestmodel.GetLocationRequestObject;
import com.vikram.locateme.ui.location.LocationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by M1032130 on 7/3/2017.
 */

public class RequestGetLocationData<T> {

    private final AppDataClient.OnDataReceived mCallBack;
    private String mCacheKey = getClass().getSimpleName();
    private final GetLocationRequestObject mGetLocationRequestObject;
    IAppDataAPIs mClient;
    AppDataClient mService;

    public RequestGetLocationData(GetLocationRequestObject getLocationRequestObject, AppDataClient.OnDataReceived<T> callback) {
        mClient = AppDataClient.getClient();
        mService = AppDataClient.getService();

        this.mGetLocationRequestObject = getLocationRequestObject;

        mCacheKey += this.mGetLocationRequestObject.getToken();
        mCacheKey += this.mGetLocationRequestObject.getContact();

        mCallBack = callback;
    }

    public void callService() {
        Call<LocationResponse> call = mClient.requestGetLocationData(mGetLocationRequestObject);

        call.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call,
                                   Response<LocationResponse> response) {
                mService.onResponse(response, mCallBack, mCacheKey);
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                mCallBack.onDataFailure(t.getMessage());
            }
        });
    }

}