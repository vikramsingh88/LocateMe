package com.vikram.locateme.ui.main.approve;

/**
 * Created by M1032130 on 7/1/2017.
 */

public interface IApprovePresenter {
    void getApproveContacts(String authToken);
    void connect(String approve, String authToken);
    void onDestroy();
}
