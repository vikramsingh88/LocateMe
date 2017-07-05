package com.vikram.locateme.appdataserver.requestmodel;

/**
 * Created by M1032130 on 6/29/2017.
 */

public class RegistrationRequestObject {
    private String userName;
    private String password;
    private String location;
    private String contact;
    private String deviceId;

    public RegistrationRequestObject(String userName, String password, String location, String contact, String deviceId) {
        this.userName = userName;
        this.password = password;
        this.location = location;
        this.contact = contact;
        this.deviceId = deviceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
