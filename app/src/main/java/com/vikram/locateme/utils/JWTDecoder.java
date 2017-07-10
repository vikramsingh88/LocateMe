package com.vikram.locateme.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by M1032130 on 7/9/2017.
 */

public class JWTDecoder {

    public static String decodedJWT(String JWTEncoded) throws Exception {
        String json = "";
        try {
            String[] split = JWTEncoded.split("\\.");
            json = getJson(split[1]);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return json;
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
