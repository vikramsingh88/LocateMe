package com.vikram.locateme.ui.main.connected;

import com.vikram.locateme.appdataserver.RetroResponse;

/**
 * Created by M1032130 on 7/1/2017.
 */

public class ConnectedResponse extends RetroResponse{
    private ConnectedContact [] data;

    public ConnectedContact[] getData() {
        return data;
    }

    public void setData(ConnectedContact[] data) {
        this.data = data;
    }
}
