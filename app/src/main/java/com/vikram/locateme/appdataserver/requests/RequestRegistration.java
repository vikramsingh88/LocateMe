package com.vikram.locateme.appdataserver.requests;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.IAppDataAPIs;
import com.vikram.locateme.appdataserver.requestmodel.RegistrationRequestObject;
import com.vikram.locateme.ui.registration.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by M1032130 on 6/29/2017.
 */

public class RequestRegistration<T> {

    private final AppDataClient.OnDataReceived mCallBack;
    private String mCacheKey = getClass().getSimpleName();
    private final RegistrationRequestObject mRegistrationRequestObject;
    IAppDataAPIs mClient;
    AppDataClient mService;

    public RequestRegistration(RegistrationRequestObject registrationRequestObject, AppDataClient.OnDataReceived<T> callback) {
        mClient = AppDataClient.getClient();
        mService = AppDataClient.getService();

        this.mRegistrationRequestObject = registrationRequestObject;

        mCacheKey += this.mRegistrationRequestObject.getUserName();
        mCacheKey += this.mRegistrationRequestObject.getPassword();
        mCacheKey += this.mRegistrationRequestObject.getContact();
        mCacheKey += this.mRegistrationRequestObject.getLocation();
        mCacheKey += this.mRegistrationRequestObject.getDeviceId();

        mCallBack = callback;
    }

    public void callService() {
        Call<User> call = mClient.registration(mRegistrationRequestObject);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call,
                                   Response<User> response) {
                mService.onResponse(response, mCallBack, mCacheKey);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mCallBack.onDataFailure(t.getMessage());
            }
        });
    }

}