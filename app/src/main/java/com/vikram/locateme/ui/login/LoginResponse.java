package com.vikram.locateme.ui.login;

import com.vikram.locateme.appdataserver.RetroResponse;

/**
 * Created by M1032130 on 6/29/2017.
 */

public class LoginResponse extends RetroResponse {
    private String data;

    public String getToken() {
        return data;
    }

    public void setToken(String token) {
        this.data = token;
    }
}
