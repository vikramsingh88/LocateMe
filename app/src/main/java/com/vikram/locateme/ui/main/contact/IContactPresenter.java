package com.vikram.locateme.ui.main.contact;

import android.content.Context;

/**
 * Created by M1032130 on 6/29/2017.
 */

public interface IContactPresenter {
    void getContacts(Context context);
    void checkContactExistence(String contact, String authToken);
    void requestLocationAccess(String contact, String authToken);
    void onDestroy();
}
