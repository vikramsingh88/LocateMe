package com.vikram.locateme.ui.settings;

/**
 * Created by M1032130 on 7/6/2017.
 */

public class SettingsPresenter implements ISettingsPresenter, ISettingsModel.LocationVisibilityListener {
    ISettingsView settingsView;
    ISettingsModel settingsModel;

    public SettingsPresenter(ISettingsView settingsView) {
        this.settingsView = settingsView;
        settingsModel = new SettingsModule();
    }

    @Override
    public void updateLocationVisibility() {
        settingsView.showProgress();
        settingsModel.updateLocationVisibility(settingsView.getAuthToken(), settingsView.getVisibility(), this);
    }

    @Override
    public void onDestroy() {
        settingsView = null;
    }

    @Override
    public void locationVisibilityUpdated() {
        if (settingsView != null) {
            settingsView.hideProgress();
            settingsView.locationVisibilityUpdated();
        }
    }

    @Override
    public void locationVisibilityUpdateError(String error) {
        if (settingsView != null) {
            settingsView.hideProgress();
            settingsView.locationVisibilityUpdateError(error);
        }
    }
}
