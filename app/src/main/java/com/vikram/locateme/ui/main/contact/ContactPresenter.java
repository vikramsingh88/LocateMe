package com.vikram.locateme.ui.main.contact;

import android.content.Context;

import java.util.List;

/**
 * Created by M1032130 on 6/29/2017.
 */

public class ContactPresenter implements IContactPresenter, IContactModel.OnContactsReceivedListener,IContactModel.OnContactExistenceListener,
                                            IContactModel.OnLocationAccessRequestListener {
    private IContactView contactView;
    private IContactModel contactModel;

    public ContactPresenter(IContactView contactView) {
        this.contactView = contactView;
        contactModel = new ContactModel();
    }
    @Override
    public void getContacts(Context context) {
        contactModel.getContacts(context, this);
    }

    @Override
    public void checkContactExistence(String contact, String authToken) {
        contactModel.checkContactExistence(contact, authToken, this);
    }

    @Override
    public void requestLocationAccess(String contact, String authToken) {
        contactModel.requestLocationAccess(contact, authToken, this);
    }

    @Override
    public void onDestroy() {
        contactView = null;
    }

    @Override
    public void onContactsReceived(List<Contact> contact) {
        if (contactView != null) {
            contactView.listContact(contact);
        }
    }

    @Override
    public void onContactExist(String response) {
        if (contactView != null) {
            contactView.onContactExist(response);
        }
    }

    @Override
    public void onContactNotExist(String response) {
        if (contactView != null) {
            contactView.onContactNotExist(response);
        }
    }

    @Override
    public void onLocationAccessRequestSuccess(String response) {
        if (contactView != null) {
            contactView.onLocationAccessRequestSuccess(response);
        }
    }

    @Override
    public void onLocationAccessRequestError(String response) {
        if (contactView != null) {
            contactView.onLocationAccessRequestError(response);
        }
    }
}
