package com.vikram.locateme.appdataserver.requestmodel;

/**
 * Created by M1032130 on 7/7/2017.
 */

public class LocationVisibilityRequestObject {
    private String token;
    private String visibility;

    public LocationVisibilityRequestObject(String token, String visibility) {
        this.token = token;
        this.visibility = visibility;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
