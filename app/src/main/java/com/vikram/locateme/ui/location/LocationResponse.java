package com.vikram.locateme.ui.location;

import com.vikram.locateme.appdataserver.RetroResponse;

/**
 * Created by M1032130 on 7/3/2017.
 */

public class LocationResponse extends RetroResponse{
    private Location [] data;

    public Location[] getData() {
        return data;
    }

    public void setData(Location[] data) {
        this.data = data;
    }
}
