package com.vikram.locateme.ui.login;

/**
 * Created by M1032130 on 6/19/2017.
 */

public interface ILoginView {
    void showProgressBar();
    void hideProgressBar();
    void showUserNameEmptyFieldError();
    void showPasswordEmptyFieldError();
    void showLoginFailedError(String response);
    void onLoginSuccess(String response);
    String getUserName();
    String getPassword();
    String getDeviceId();
}
