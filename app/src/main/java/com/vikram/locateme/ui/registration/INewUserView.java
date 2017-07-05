package com.vikram.locateme.ui.registration;

/**
 * Created by M1032130 on 6/21/2017.
 */

public interface INewUserView {
    void showProgress();
    void hideProgress();
    void showUserNameEmptyError();
    void showPasswordEmptyError();
    void showConfirmPasswordEmptyError();
    void showPasswordMismatchError();
    void showLocationEmptyError();
    void showContactEmptyError();
    void onRegistrationSuccess(String response);
    void onRegistrationFailed(String response);
    void onLocationAvailable(String location);
    void onLatLon(double lat, double lon);
    void onNoLatLon();
    String getUserName();
    String getPassword();
    String getConfirmPassword();
    String getLocation();
    String getContact();
    String getCountryCode();
    String getDeviceId();
}
