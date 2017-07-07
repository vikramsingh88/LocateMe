package com.vikram.locateme.ui.settings;

/**
 * Created by M1032130 on 7/6/2017.
 */

public interface ISettingsView {
    void showProgress();
    void hideProgress();
    void locationVisibilityUpdated();
    void locationVisibilityUpdateError(String error);
    String getAuthToken();
    String getVisibility();
}
