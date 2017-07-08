package com.vikram.locateme.ui.history;

import java.util.List;

/**
 * Created by M1032130 on 7/8/2017.
 */

public class HistoryPresenter implements IHistoryPresenter, IHistoryModel.OnLocationHistoryListener {
    private IHistoryView historyView;
    private IHistoryModel historyModel;

    public HistoryPresenter(IHistoryView historyView) {
        this.historyView = historyView;
        historyModel = new HistoryModel();
    }

    @Override
    public void onDestroy() {
        historyView = null;
    }

    @Override
    public void getLocations(String token, String contact) {
        if (historyView != null) {
            historyView.showProgress();
            historyModel.getLocationHistory(token, contact, this);
        }
    }

    @Override
    public void receivedLocations(List<LocationHistory> historyList) {
        if (historyView != null) {
            historyView.hideProgress();
            historyView.receivedLocations(historyList);
        }
    }

    @Override
    public void error(String error) {
        if (historyView != null) {
            historyView.hideProgress();
            historyView.error(error);
        }
    }
}
