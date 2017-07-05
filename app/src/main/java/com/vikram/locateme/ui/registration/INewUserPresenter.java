package com.vikram.locateme.ui.registration;

/**
 * Created by M1032130 on 6/21/2017.
 */

public interface INewUserPresenter {
    void registration();
    void currentLocation(double lat, double lon);
    void getCurrentLatLon();
    void onDestroy();
}
