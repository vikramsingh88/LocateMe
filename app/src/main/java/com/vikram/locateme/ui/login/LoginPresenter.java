package com.vikram.locateme.ui.login;

import android.content.Context;
import android.util.Log;

import com.vikram.locateme.firbase.SharedPrefManager;

/**
 * Created by M1032130 on 6/19/2017.
 */

public class LoginPresenter implements ILoginPresenter, ILoginModel.OnLoginRequestFinishedListener {
    ILoginView loginView;
    ILoginModel loginModel;

    public LoginPresenter(ILoginView view) {
        this.loginView = view;
        loginModel = new LoginModel();
    }

    @Override
    public void authenticate() {
        if (loginView != null) {
            if (loginView.getUserName().equals("")) {
                loginView.showUserNameEmptyFieldError();
                return;
            }
            if (loginView.getPassword().equals("")) {
                loginView.showPasswordEmptyFieldError();
                return;
            }
            loginView.showProgressBar();
            Log.d("LoginModel", SharedPrefManager.getInstance((Context) loginView).getToken());
            loginModel.authenticate(loginView.getUserName(), loginView.getPassword(), loginView.getDeviceId(), this);
        }
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onLoginSuccess(String response) {
        if (loginView != null) {
            loginView.hideProgressBar();
            loginView.onLoginSuccess(response);
        }
    }

    @Override
    public void onLoginFailed(String response) {
        if (loginView != null) {
            loginView.hideProgressBar();
            loginView.showLoginFailedError(response);
        }
    }
}
