package com.vikram.locateme.appdataserver.requestmodel;

/**
 * Created by M1032130 on 6/30/2017.
 */

public class PendingRequestObject {
    private String token;

    public PendingRequestObject(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
