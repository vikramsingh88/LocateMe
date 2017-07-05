package com.vikram.locateme.ui.main.connected;

/**
 * Created by M1032130 on 7/1/2017.
 */

public interface IConnectedPresenter {
    void getConnectedContacts(String authToken);
    void onDestroy();
}
