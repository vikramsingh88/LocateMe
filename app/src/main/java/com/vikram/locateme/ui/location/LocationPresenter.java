package com.vikram.locateme.ui.location;

import java.util.List;

/**
 * Created by M1032130 on 7/3/2017.
 */

public class LocationPresenter implements ILocationPresenter, ILocationModel.OnLocationReceivedListener, ILocationModel.OnLocationShareListener {
    private ILocationView locationView;
    private ILocationModel locationModel;

    public LocationPresenter(ILocationView locationView) {
        this.locationView = locationView;
        locationModel = new LocationModel();
    }

    @Override
    public void onLocation(List<Location> location) {
        if (locationView != null) {
            locationView.hideProgress();
            locationView.receivedLocation(location);
        }
    }

    @Override
    public void noLocation(String error) {
        if (locationView != null) {
            locationView.hideProgress();
            locationView.noLocation(error);
        }
    }

    @Override
    public void getLocation(String token, String contact) {
        if (locationView != null) {
            locationView.showProgress();
            locationModel.getLocation(token, contact, this);
        }
    }

    @Override
    public void shareLocation(String token, String contact, String location) {
        locationModel.shareLocation(token, contact, location, this);
    }

    @Override
    public void onDestroy() {
        locationView = null;
    }

    @Override
    public void onLocationShare(String response) {
        if (locationView != null) {
            locationView.locationShared(response);
        }
    }
}
