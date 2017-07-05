package com.vikram.locateme.ui.main.pending;

import com.vikram.locateme.appdataserver.RetroResponse;

/**
 * Created by M1032130 on 6/30/2017.
 */

public class PendingResponse extends RetroResponse {
    private PendingContact [] data;

    public PendingContact[] getData() {
        return data;
    }

    public void setData(PendingContact[] data) {
        this.data = data;
    }
}
