package com.vikram.locateme.ui.history;

import com.vikram.locateme.appdataserver.RetroResponse;

/**
 * Created by M1032130 on 7/8/2017.
 */

public class LocationHistoryResponse  extends RetroResponse {
    private LocationHistory [] data;

    public LocationHistory[] getData() {
        return data;
    }

    public void setData(LocationHistory[] data) {
        this.data = data;
    }
}
