package com.vikram.locateme.ui.main.contact;

import java.util.List;

/**
 * Created by M1032130 on 6/29/2017.
 */

public interface IContactView {
    void listContact(List<Contact> contacts);
    void onContactExist(String response);
    void onContactNotExist(String response);
    void onLocationAccessRequestSuccess(String response);
    void onLocationAccessRequestError(String response);
}
