package com.vikram.locateme.appdataserver.requests;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.IAppDataAPIs;
import com.vikram.locateme.appdataserver.requestmodel.PendingRequestObject;
import com.vikram.locateme.ui.main.pending.PendingResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by M1032130 on 6/30/2017.
 */

public class RequestPendingContacts<T> {

    private final AppDataClient.OnDataReceived mCallBack;
    private String mCacheKey = getClass().getSimpleName();
    private final PendingRequestObject mPendingRequestObject;
    IAppDataAPIs mClient;
    AppDataClient mService;

    public RequestPendingContacts(PendingRequestObject pendingRequestObject, AppDataClient.OnDataReceived<T> callback) {
        mClient = AppDataClient.getClient();
        mService = AppDataClient.getService();

        this.mPendingRequestObject = pendingRequestObject;

        mCacheKey += this.mPendingRequestObject.getToken();

        mCallBack = callback;
    }

    public void callService() {
        Call<PendingResponse> call = mClient.requestPendingContacts(mPendingRequestObject);

        call.enqueue(new Callback<PendingResponse>() {
            @Override
            public void onResponse(Call<PendingResponse> call,
                                   Response<PendingResponse> response) {
                mService.onResponse(response, mCallBack, mCacheKey);
            }

            @Override
            public void onFailure(Call<PendingResponse> call, Throwable t) {
                mCallBack.onDataFailure(t.getMessage());
            }
        });
    }

}