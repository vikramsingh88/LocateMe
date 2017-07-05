package com.vikram.locateme.ui.main.approve;

import java.util.List;

/**
 * Created by M1032130 on 7/1/2017.
 */

public interface IApproveView {
    void showProgress();
    void hideProgress();
    void onApproveContactReceived(List<ApproveContact> listApprove);
    void onApproveContactError(String error);
    void onConnected(String response);
    void onConnectionError(String response);
}
