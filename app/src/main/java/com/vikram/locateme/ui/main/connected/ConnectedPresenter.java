package com.vikram.locateme.ui.main.connected;

import java.util.List;

/**
 * Created by M1032130 on 7/1/2017.
 */

public class ConnectedPresenter implements IConnectedPresenter, IConnectedModel.OnConnectedContactReceivedListener {
    IConnectedView connectedView;
    IConnectedModel connectedModel;

    public ConnectedPresenter(IConnectedView connectedView) {
        this.connectedView = connectedView;
        connectedModel = new ConnectedModel();
    }

    @Override
    public void getConnectedContacts(String authToken) {
        connectedModel.getConnectedContacts(authToken, this);
    }

    @Override
    public void onDestroy() {
        connectedView = null;
    }

    @Override
    public void onConnectedContactReceived(List<ConnectedContact> listConnected) {
        if (connectedView != null) {
            connectedView.onConnectedContactReceived(listConnected);
        }
    }

    @Override
    public void onConnectedContactError(String error) {
        if (connectedView != null) {
            connectedView.onConnectedContactError(error);
        }
    }
}
