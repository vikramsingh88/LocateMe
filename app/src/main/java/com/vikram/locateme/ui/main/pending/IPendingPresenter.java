package com.vikram.locateme.ui.main.pending;

/**
 * Created by M1032130 on 6/30/2017.
 */

public interface IPendingPresenter {
    void getPendingContacts(String authToken);
    void onDestroy();
}
