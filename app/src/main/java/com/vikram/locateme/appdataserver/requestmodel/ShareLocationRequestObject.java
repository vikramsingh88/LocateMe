package com.vikram.locateme.appdataserver.requestmodel;

/**
 * Created by M1032130 on 7/5/2017.
 */

public class ShareLocationRequestObject {
    private String token;
    private String contact;
    private String location;

    public ShareLocationRequestObject(String token, String contact, String location) {
        this.token = token;
        this.contact = contact;
        this.location = location;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
