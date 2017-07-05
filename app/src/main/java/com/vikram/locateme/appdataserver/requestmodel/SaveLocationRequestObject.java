package com.vikram.locateme.appdataserver.requestmodel;

/**
 * Created by M1032130 on 7/3/2017.
 */

public class SaveLocationRequestObject {
    private String token;
    private String latlon;
    private String address;

    public SaveLocationRequestObject(String token, String latlon, String address) {
        this.token = token;
        this.latlon = latlon;
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLatlon() {
        return latlon;
    }

    public void setLatlon(String latlon) {
        this.latlon = latlon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
