package com.vikram.locateme.ui.login;

import android.util.Log;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.RetroResponse;
import com.vikram.locateme.appdataserver.requestmodel.LoginRequestObject;
import com.vikram.locateme.appdataserver.requests.RequestLogin;

/**
 * Created by M1032130 on 6/19/2017.
 */

public class LoginModel implements ILoginModel{
    @Override
    public void authenticate(String userName, String password, String deviceId, final OnLoginRequestFinishedListener listener) {
        new RequestLogin(new LoginRequestObject(userName, password, deviceId), new AppDataClient
                .OnDataReceived<LoginResponse>() {

            @Override
            public void onDataSuccess(LoginResponse loginResponse) {
                Log.d("authenticate", "onDataSuccess " + loginResponse.getStatusMessage());
                listener.onLoginSuccess(loginResponse.getToken());
            }

            @Override
            public void onDataFailure(String error) {
                Log.d("authenticate", "onDataFailure " + error.toString());
                listener.onLoginFailed(error);
            }
        }).callService();
    }
}
