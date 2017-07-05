package com.vikram.locateme.ui.main.contact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.RetroResponse;
import com.vikram.locateme.appdataserver.requestmodel.CheckUserRequestObject;
import com.vikram.locateme.appdataserver.requestmodel.LocationRequestObject;
import com.vikram.locateme.appdataserver.requests.RequestCheckContactExist;
import com.vikram.locateme.appdataserver.requests.RequestLocationAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by M1032130 on 6/29/2017.
 */

public class ContactModel implements IContactModel {

    private Context mContext;
    private List<Contact> mListContact;
    private OnContactsReceivedListener listener;

    @Override
    public void getContacts(Context context, OnContactsReceivedListener listener) {
        mContext = context;
        mListContact = new ArrayList<>();
        this.listener = listener;
        new AsyncContacts().execute();
    }

    @Override
    public void checkContactExistence(String contact, String authToken, final OnContactExistenceListener listener) {
        new RequestCheckContactExist(new CheckUserRequestObject(null, contact, authToken), new AppDataClient
                .OnDataReceived<RetroResponse>() {

            @Override
            public void onDataSuccess(RetroResponse checkUserResponseModel) {
                Log.d("ContactModel", "onDataSuccess " + checkUserResponseModel.getStatusMessage());
                listener.onContactExist(checkUserResponseModel.getStatusMessage());
            }

            @Override
            public void onDataFailure(String error) {
                Log.d("ContactModel", "onDataFailure " + error.toString());
                listener.onContactNotExist(error);
            }
        }).callService();
    }

    @Override
    public void requestLocationAccess(String contact, String authToken, final OnLocationAccessRequestListener listener) {
        new RequestLocationAccess(new LocationRequestObject(authToken, contact), new AppDataClient
                .OnDataReceived<RetroResponse>() {

            @Override
            public void onDataSuccess(RetroResponse response) {
                Log.d("requestLocationAccess", "onDataSuccess " + response.getStatusMessage());
                listener.onLocationAccessRequestSuccess(response.getMessage());
            }

            @Override
            public void onDataFailure(String error) {
                Log.d("requestLocationAccess", "onDataFailure " + error.toString());
                listener.onLocationAccessRequestError(error);
            }
        }).callService();
    }

    private class AsyncContacts extends AsyncTask<Void, Void, List<Contact>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected List<Contact> doInBackground(Void... params) {

            return displayContacts();
        }

        @Override
        protected void onPostExecute(List<Contact> result) {
            super.onPostExecute(result);
            listener.onContactsReceived(result);
        }

    }

    private List<Contact> displayContacts() {
        ContentResolver cr = mContext.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.d("CONTACTS", "Name: " + name + ", Phone No: " + phoneNo);
                        mListContact.add(new Contact(name, phoneNo));
                    }
                    pCur.close();
                }
            }
        }
        return mListContact;
    }
}
