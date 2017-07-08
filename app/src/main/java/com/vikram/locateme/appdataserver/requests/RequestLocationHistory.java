package com.vikram.locateme.appdataserver.requests;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.IAppDataAPIs;
import com.vikram.locateme.appdataserver.requestmodel.GetLocationRequestObject;
import com.vikram.locateme.ui.history.LocationHistoryResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by M1032130 on 7/8/2017.
 */

public class RequestLocationHistory<T> {

    private final AppDataClient.OnDataReceived mCallBack;
    private String mCacheKey = getClass().getSimpleName();
    private final GetLocationRequestObject mGetLocationRequestObject;
    IAppDataAPIs mClient;
    AppDataClient mService;

    public RequestLocationHistory(GetLocationRequestObject getLocationRequestObject, AppDataClient.OnDataReceived<T> callback) {
        mClient = AppDataClient.getClient();
        mService = AppDataClient.getService();

        this.mGetLocationRequestObject = getLocationRequestObject;

        mCacheKey += this.mGetLocationRequestObject.getToken();
        mCacheKey += this.mGetLocationRequestObject.getContact();

        mCallBack = callback;
    }

    public void callService() {
        Call<LocationHistoryResponse> call = mClient.requestLocationHistory(mGetLocationRequestObject);

        call.enqueue(new Callback<LocationHistoryResponse>() {
            @Override
            public void onResponse(Call<LocationHistoryResponse> call,
                                   Response<LocationHistoryResponse> response) {
                mService.onResponse(response, mCallBack, mCacheKey);
            }

            @Override
            public void onFailure(Call<LocationHistoryResponse> call, Throwable t) {
                mCallBack.onDataFailure(t.getMessage());
            }
        });
    }

}