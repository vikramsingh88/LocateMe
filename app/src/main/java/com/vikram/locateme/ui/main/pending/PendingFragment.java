package com.vikram.locateme.ui.main.pending;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vikram.locateme.R;
import com.vikram.locateme.ui.main.contact.Contact;
import com.vikram.locateme.utils.Constants;
import com.vikram.locateme.utils.DialogHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by M1032130 on 6/20/2017.
 */

public class PendingFragment extends Fragment implements IPendingView {
    IPendingPresenter pendingPresenter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PendingListAdapter mAdapter;
    private String authToken;
    private String clickedContact;
    private BroadcastReceiver receiver;

    public PendingFragment() {
        pendingPresenter = new PendingPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.PENDING_INTENT);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                pendingPresenter.getPendingContacts(authToken);
            }
        };
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending, container, false);
        authToken = this.getArguments().getString("authToken");

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        pendingPresenter.getPendingContacts(authToken);
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
        pendingPresenter.getPendingContacts(authToken);
    }

    @Override
    public void onPendingContactReceived(List<PendingContact> listPending) {
        List<PendingContact> myList = new CopyOnWriteArrayList<>();
        myList.addAll(listPending);
        for (PendingContact pending : myList) {
            if (pending.getPending().equals("")) {
                myList.remove(pending);
            }
        }
        mAdapter = new PendingListAdapter(getActivity(), myList, new PendingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final PendingContact item) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onPendingContactError(String error) {
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