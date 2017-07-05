package com.vikram.locateme.ui.location;

/**
 * Created by M1032130 on 7/3/2017.
 */

public interface ILocationPresenter {
    void getLocation(String token, String contact);
    void shareLocation(String token, String contact, String location);
    void onDestroy();
}
