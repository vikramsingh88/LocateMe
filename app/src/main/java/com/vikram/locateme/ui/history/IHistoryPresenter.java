package com.vikram.locateme.ui.history;

/**
 * Created by M1032130 on 7/8/2017.
 */

public interface IHistoryPresenter {
    void onDestroy();
    void getLocations(String token, String contact);
}
