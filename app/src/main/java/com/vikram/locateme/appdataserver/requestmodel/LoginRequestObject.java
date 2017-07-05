package com.vikram.locateme.appdataserver.requestmodel;

/**
 * Created by M1032130 on 6/28/2017.
 */

public class LoginRequestObject {
    private String userName;
    private String password;
    private String deviceId;

    public LoginRequestObject(String userName, String password, String deviceId) {
        this.userName = userName;
        this.password = password;
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
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
}
