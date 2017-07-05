package com.vikram.locateme.appdataserver.requests;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.IAppDataAPIs;
import com.vikram.locateme.appdataserver.RetroResponse;
import com.vikram.locateme.appdataserver.requestmodel.LoginRequestObject;
import com.vikram.locateme.ui.login.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by M1032130 on 6/28/2017.
 */

public class RequestLogin<T> {

    private final AppDataClient.OnDataReceived mCallBack;
    private String mCacheKey = getClass().getSimpleName();
    private final LoginRequestObject mLoginRequestObject;
    IAppDataAPIs mClient;
    AppDataClient mService;

    public RequestLogin(LoginRequestObject loginRequestObject, AppDataClient.OnDataReceived<T> callback) {
        mClient = AppDataClient.getClient();
        mService = AppDataClient.getService();

        this.mLoginRequestObject = loginRequestObject;

        mCacheKey += this.mLoginRequestObject.getUserName();
        mCacheKey += this.mLoginRequestObject.getPassword();
        mCacheKey += this.mLoginRequestObject.getDeviceId();

        mCallBack = callback;
    }

    public void callService() {
        Call<LoginResponse> call = mClient.authenticate(mLoginRequestObject);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call,
                                   Response<LoginResponse> response) {
                mService.onResponse(response, mCallBack, mCacheKey);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                mCallBack.onDataFailure(t.getMessage());
            }
        });
    }

}