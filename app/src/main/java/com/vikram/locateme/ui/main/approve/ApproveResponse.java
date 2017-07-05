package com.vikram.locateme.ui.main.approve;

import com.vikram.locateme.appdataserver.RetroResponse;

/**
 * Created by M1032130 on 7/1/2017.
 */

public class ApproveResponse extends RetroResponse {
    private ApproveContact [] data;

    public ApproveContact[] getData() {
        return data;
    }

    public void setData(ApproveContact[] data) {
        this.data = data;
    }
}
