package com.vikram.locateme.ui.history;

import java.util.List;

/**
 * Created by M1032130 on 7/8/2017.
 */

public interface IHistoryView {
    void showProgress();
    void hideProgress();
    void receivedLocations(List<LocationHistory> historyList);
    void error(String error);
}
