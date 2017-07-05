package com.vikram.locateme.appdataserver.requestmodel;

/**
 * Created by M1032130 on 7/1/2017.
 */

public class ConnectedRequestObject {
    private String token;

    public ConnectedRequestObject(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
