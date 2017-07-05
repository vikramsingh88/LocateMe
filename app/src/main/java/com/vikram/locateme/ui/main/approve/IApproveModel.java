package com.vikram.locateme.ui.main.approve;

import java.util.List;

/**
 * Created by M1032130 on 7/1/2017.
 */

public interface IApproveModel {
    interface OnApproveContactReceivedListener {
        void onApproveContactReceived(List<ApproveContact> listApprove);
        void onApproveContactError(String error);
    }

    interface OnConnectionListener {
        void onConnection(String response);
        void onConnectionError(String error);
    }

    void getApproveContacts(String authToken, OnApproveContactReceivedListener OnConnectionListener);
    void connect(String approve, String authToken, OnConnectionListener listener);
}
