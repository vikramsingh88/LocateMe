package com.vikram.locateme.ui.main.connected;

import java.util.List;

/**
 * Created by M1032130 on 7/1/2017.
 */

public interface IConnectedView {
    void showProgress();
    void hideProgress();
    void onConnectedContactReceived(List<ConnectedContact> listConnectedContact);
    void onConnectedContactError(String error);
}
