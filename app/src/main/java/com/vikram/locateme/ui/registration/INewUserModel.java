package com.vikram.locateme.ui.registration;

/**
 * Created by M1032130 on 6/21/2017.
 */

public interface INewUserModel {

    interface OnRegistrationFinishedListener {
        void onRegistrationSuccess(String response);
        void onRegistrationFailed(String response);
    }

    interface OnLocationListener {
        void onLocationAvailable(String location);
        void onLocationError(String message);
    }

    interface OnUserCheckFinishedListener {
        void onUserNameExist();
        void onNewUser();
    }

    interface OnLatLonListener {
        void onLatLon(double lat, double lon);
        void onNoLatLon();
    }

    void checkUserNameAvailability(String userName, OnUserCheckFinishedListener listener);
    void registration(User user, OnRegistrationFinishedListener listener);
    void getLocation(double lat, double lon, OnLocationListener listener);
    void getCurrentLatLon(OnLatLonListener listener);
}
