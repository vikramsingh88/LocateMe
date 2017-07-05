package com.vikram.locateme.ui.main.pending;

import com.vikram.locateme.ui.main.contact.Contact;

import java.util.List;

/**
 * Created by M1032130 on 6/30/2017.
 */

public class PendingPresenter implements IPendingPresenter, IPendingModel.OnPendingContactReceivedListener {
    IPendingView pendingView;
    IPendingModel pendingModel;

    public PendingPresenter(IPendingView pendingView) {
        this.pendingView = pendingView;
        pendingModel = new PendingModel();
    }

    @Override
    public void getPendingContacts(String authToken) {

        pendingModel.getPendingContacts(authToken, this);
    }

    @Override
    public void onDestroy() {
        pendingView = null;
    }

    @Override
    public void onPendingContactReceived(List<PendingContact> listPending) {
        if (pendingView != null) {
            pendingView.onPendingContactReceived(listPending);
        }
    }

    @Override
    public void onPendingContactError(String error) {
        if (pendingView != null) {
            pendingView.onPendingContactError(error);
        }
    }
}
