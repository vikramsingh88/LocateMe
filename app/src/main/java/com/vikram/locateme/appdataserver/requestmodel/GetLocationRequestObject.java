package com.vikram.locateme.appdataserver.requestmodel;

/**
 * Created by M1032130 on 7/3/2017.
 */

public class GetLocationRequestObject {
    private String token;
    private String contact;

    public GetLocationRequestObject(String token, String contact) {
        this.token = token;
        this.contact = contact;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
