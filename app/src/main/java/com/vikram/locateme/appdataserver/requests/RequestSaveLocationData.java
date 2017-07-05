package com.vikram.locateme.appdataserver.requests;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.IAppDataAPIs;
import com.vikram.locateme.appdataserver.RetroResponse;
import com.vikram.locateme.appdataserver.requestmodel.SaveLocationRequestObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by M1032130 on 7/3/2017.
 */

public class RequestSaveLocationData<T> {

    private final AppDataClient.OnDataReceived mCallBack;
    private String mCacheKey = getClass().getSimpleName();
    private final SaveLocationRequestObject mSaveLocationRequestObject;
    IAppDataAPIs mClient;
    AppDataClient mService;

    public RequestSaveLocationData(SaveLocationRequestObject saveLocationRequestObject, AppDataClient.OnDataReceived<T> callback) {
        mClient = AppDataClient.getClient();
        mService = AppDataClient.getService();

        this.mSaveLocationRequestObject = saveLocationRequestObject;

        mCacheKey += this.mSaveLocationRequestObject.getToken();
        mCacheKey += this.mSaveLocationRequestObject.getLatlon();

        mCallBack = callback;
    }

    public void callService() {
        Call<RetroResponse> call = mClient.requestSaveLocationData(mSaveLocationRequestObject);

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