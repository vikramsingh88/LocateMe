package com.vikram.locateme.ui.main.contact;

import android.content.Context;

import java.util.List;

/**
 * Created by M1032130 on 6/29/2017.
 */

public interface IContactModel {
    interface OnContactsReceivedListener {
        void onContactsReceived(List<Contact> contacts);
    }
    interface OnContactExistenceListener {
        void onContactExist(String response);
        void onContactNotExist(String response);
    }
    interface OnLocationAccessRequestListener {
        void onLocationAccessRequestSuccess(String response);
        void onLocationAccessRequestError(String response);
    }

    void getContacts(Context context, OnContactsReceivedListener listener);
    void checkContactExistence(String contact, String authToken, OnContactExistenceListener listener);
    void requestLocationAccess(String contact, String authToken, OnLocationAccessRequestListener listener);
}
