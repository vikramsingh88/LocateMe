package com.vikram.locateme.ui.main.connected;

import java.util.List;

/**
 * Created by M1032130 on 7/1/2017.
 */

public interface IConnectedModel {
    interface OnConnectedContactReceivedListener {
        void onConnectedContactReceived(List<ConnectedContact> listConnected);
        void onConnectedContactError(String error);
    }

    void getConnectedContacts(String authToken, OnConnectedContactReceivedListener listener);
}
