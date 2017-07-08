package com.vikram.locateme.ui.history;

import com.vikram.locateme.appdataserver.RetroResponse;

/**
 * Created by M1032130 on 7/8/2017.
 */

public class LocationHistory {
    private String userName;
    private String contact;
    private String latlon;
    private String address;
    private String timeStamp;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
