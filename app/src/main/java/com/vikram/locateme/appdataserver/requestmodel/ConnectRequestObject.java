package com.vikram.locateme.appdataserver.requestmodel;

/**
 * Created by M1032130 on 7/1/2017.
 */

public class ConnectRequestObject {
    private String approve;
    private String token;

    public ConnectRequestObject(String approve, String token) {
        this.approve = approve;
        this.token = token;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
