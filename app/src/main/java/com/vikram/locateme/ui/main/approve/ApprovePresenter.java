package com.vikram.locateme.ui.main.approve;

import java.util.List;

/**
 * Created by M1032130 on 7/1/2017.
 */

public class ApprovePresenter implements IApprovePresenter, IApproveModel.OnApproveContactReceivedListener, IApproveModel.OnConnectionListener {
    IApproveView approveView;
    IApproveModel approveModel;

    public ApprovePresenter(IApproveView approveView) {
        this.approveView = approveView;
        approveModel = new ApproveModel();
    }

    @Override
    public void getApproveContacts(String authToken) {
        approveModel.getApproveContacts(authToken, this);
    }

    @Override
    public void connect(String approve, String authToken) {
        approveModel.connect(approve, authToken, this);
    }

    @Override
    public void onDestroy() {
        approveView = null;
    }

    @Override
    public void onApproveContactReceived(List<ApproveContact> listApprove) {
        if (approveView != null) {
            approveView.onApproveContactReceived(listApprove);
        }
    }

    @Override
    public void onApproveContactError(String error) {
        if (approveView != null) {
            approveView.onApproveContactError(error);
        }
    }

    @Override
    public void onConnection(String response) {
        if (approveView != null) {
            approveView.onConnected(response);
        }
    }

    @Override
    public void onConnectionError(String error) {
        if (approveView != null) {
            approveView.onConnectionError(error);
        }
    }
}
