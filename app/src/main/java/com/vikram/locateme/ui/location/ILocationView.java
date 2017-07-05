package com.vikram.locateme.ui.location;

import java.util.List;

/**
 * Created by M1032130 on 7/3/2017.
 */

public interface ILocationView {
    void showProgress();
    void hideProgress();
    void receivedLocation(List<Location> locations);
    void noLocation(String error);
    void locationShared(String response);
}
