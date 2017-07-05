package com.vikram.locateme.appdataserver.requests;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.IAppDataAPIs;
import com.vikram.locateme.appdataserver.requestmodel.ApproveRequestObject;
import com.vikram.locateme.ui.main.approve.ApproveResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by M1032130 on 7/1/2017.
 */

public class RequestApproveContacts<T> {

    private final AppDataClient.OnDataReceived mCallBack;
    private String mCacheKey = getClass().getSimpleName();
    private final ApproveRequestObject mApproveRequestObject;
    IAppDataAPIs mClient;
    AppDataClient mService;

    public RequestApproveContacts(ApproveRequestObject approveRequestObject, AppDataClient.OnDataReceived<T> callback) {
        mClient = AppDataClient.getClient();
        mService = AppDataClient.getService();

        this.mApproveRequestObject = approveRequestObject;

        mCacheKey += this.mApproveRequestObject.getToken();

        mCallBack = callback;
    }

    public void callService() {
        Call<ApproveResponse> call = mClient.requestApproveContacts(mApproveRequestObject);

        call.enqueue(new Callback<ApproveResponse>() {
            @Override
            public void onResponse(Call<ApproveResponse> call,
                                   Response<ApproveResponse> response) {
                mService.onResponse(response, mCallBack, mCacheKey);
            }

            @Override
            public void onFailure(Call<ApproveResponse> call, Throwable t) {
                mCallBack.onDataFailure(t.getMessage());
            }
        });
    }

}