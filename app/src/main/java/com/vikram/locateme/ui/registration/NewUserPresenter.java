package com.vikram.locateme.ui.registration;

import android.content.Context;

/**
 * Created by M1032130 on 6/21/2017.
 */

public class NewUserPresenter implements INewUserPresenter, INewUserModel.OnRegistrationFinishedListener, INewUserModel.OnLocationListener,
                                            INewUserModel.OnUserCheckFinishedListener, INewUserModel.OnLatLonListener {

    INewUserView userView;
    INewUserModel userModel;

    public NewUserPresenter(Context view) {
        this.userView = (INewUserView)view;
        userModel = new NewUserModel(view);
    }

    @Override
    public void registration() {
        if (userView != null) {
            if (userView.getUserName().equals("")) {
                userView.showUserNameEmptyError();
                return;
            }
            if (userView.getPassword().equals("")) {
                userView.showPasswordEmptyError();
                return;
            }
            if (userView.getConfirmPassword().equals("")) {
                userView.showConfirmPasswordEmptyError();
                return;
            }
            if (!userView.getPassword().equals(userView.getConfirmPassword())) {
                userView.showPasswordMismatchError();
                return;
            }
            if (userView.getLocation().equals("")) {
                userView.showLocationEmptyError();
                return;
            }
            if (userView.getContact().equals("")) {
                userView.showContactEmptyError();
                return;
            }
            if (userView.getContact().length() < 10) {
                userView.showContactEmptyError();
                return;
            }
            userView.showProgress();
            userModel.checkUserNameAvailability(userView.getUserName(), this);
        }
    }

    @Override
    public void currentLocation(double lat , double lon) {
        userModel.getLocation(lat, lon, this);
    }

    @Override
    public void getCurrentLatLon() {
        userModel.getCurrentLatLon(this);
    }

    @Override
    public void onDestroy() {
        userView = null;
    }

    @Override
    public void onLocationAvailable(String location) {
        if (userView != null) {
            userView.onLocationAvailable(location);
        }
    }

    @Override
    public void onLocationError(String message) {

    }

    @Override
    public void onRegistrationSuccess(String response) {
        if (userView != null) {
            userView.hideProgress();
            userView.onRegistrationSuccess(response);
        }
    }

    @Override
    public void onRegistrationFailed(String response) {
        if (userView != null) {
            userView.hideProgress();
            userView.onRegistrationFailed(response);
        }
    }

    @Override
    public void onUserNameExist() {

    }

    @Override
    public void onNewUser() {
        userView.showProgress();
        userModel.registration(new User(userView.getUserName(),
        userView.getPassword(), userView.getLocation(), userView.getCountryCode()+userView.getContact(),userView.getDeviceId()),this);
    }

    @Override
    public void onLatLon(double lat, double lon) {
        if (userView != null) {
            userView.onLatLon(lat, lon);
        }
    }

    @Override
    public void onNoLatLon() {
        if (userView != null) {
            userView.onNoLatLon();
        }
    }
}
