package com.vikram.locateme.ui.settings;

/**
 * Created by M1032130 on 7/6/2017.
 */

public interface ISettingsModel {
    interface LocationVisibilityListener {
        void locationVisibilityUpdated();
        void locationVisibilityUpdateError(String error);
    }

    void updateLocationVisibility(String token, String visibility, LocationVisibilityListener listener);
}
