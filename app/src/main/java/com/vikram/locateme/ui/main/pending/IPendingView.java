package com.vikram.locateme.ui.main.pending;

import java.util.List;

/**
 * Created by M1032130 on 6/30/2017.
 */

public interface IPendingView {
    void showProgress();
    void hideProgress();
    void onPendingContactReceived(List<PendingContact> listPending);
    void onPendingContactError(String error);
}
