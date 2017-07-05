package com.vikram.locateme.appdataserver.requestmodel;

/**
 * Created by steebanjoseph on 05/06/17.
 */
public class CheckUserRequestObject {

    private String userName;
    private String contact;
    private String token;

    public CheckUserRequestObject(String userName, String contact, String authToken) {
        this.userName = userName;
        this.contact = contact;
        this.token = authToken;
    }

    public String getAuthToken() {
        return token;
    }

    public void setAuthToken(String authToken) {
        this.token = authToken;
    }

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
}
