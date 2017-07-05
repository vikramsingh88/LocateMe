package com.vikram.locateme.ui.login;

/**
 * Created by M1032130 on 6/19/2017.
 */

public interface ILoginModel {
    interface OnLoginRequestFinishedListener {
        void onLoginSuccess(String response);
        void onLoginFailed(String response);
    }

    void authenticate(String userName, String password, String deviceId, OnLoginRequestFinishedListener listener);
}
