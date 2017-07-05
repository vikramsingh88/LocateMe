package com.vikram.locateme.ui.main.connected;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vikram.locateme.R;
import com.vikram.locateme.ui.location.MapsActivity;
import com.vikram.locateme.utils.Constants;
import com.vikram.locateme.utils.DialogHelper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by M1032130 on 6/20/2017.
 */

public class ConnectedFragment extends Fragment implements IConnectedView {
    IConnectedPresenter connectedPresenter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ConnectedListAdapter mAdapter;
    private String authToken;
    private BroadcastReceiver receiver;

    public ConnectedFragment() {
        connectedPresenter = new ConnectedPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.CONNECTED_INTENT);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                connectedPresenter.getConnectedContacts(authToken);
            }
        };
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connected, container, false);
        authToken = this.getArguments().getString("authToken");

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        connectedPresenter.getConnectedContacts(authToken);
        return view;
    }

    @Override
    public void onDestroy() {
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    public void updateContact() {
        connectedPresenter.getConnectedContacts(authToken);
    }

    @Override
    public void onConnectedContactReceived(List<ConnectedContact> listConnectedContact) {
        List<ConnectedContact> myList = new CopyOnWriteArrayList<ConnectedContact>();
        myList.addAll(listConnectedContact);
        for (ConnectedContact connected : myList) {
            if (connected.getConnected().equals("")) {
                myList.remove(connected);
            }
        }
        mAdapter = new ConnectedListAdapter(getActivity(), myList, new ConnectedListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final ConnectedContact item) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("authToken", authToken);
                intent.putExtra("contact", item.getConnected());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onConnectedContactError(String error) {
        AlertDialog.Builder builder = DialogHelper.showErrorDialog(getActivity(), error);
        if (builder != null) {
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.create().show();
        }
    }
}