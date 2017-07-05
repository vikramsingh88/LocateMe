package com.vikram.locateme.ui.location;

import java.util.List;

/**
 * Created by M1032130 on 7/3/2017.
 */

public interface ILocationModel {
    interface OnLocationReceivedListener {
        void onLocation(List<Location> location);
        void noLocation(String error);
    }
    interface OnLocationShareListener {
        void onLocationShare(String response);
    }

    void getLocation(String token, String contact, OnLocationReceivedListener listener);
    void shareLocation(String token, String contact, String location, OnLocationShareListener listener);
}
