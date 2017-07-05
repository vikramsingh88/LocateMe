package com.vikram.locateme.ui.main.approve;

import android.app.Activity;
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
import com.vikram.locateme.ui.main.IOnConnectedUpdated;
import com.vikram.locateme.utils.Constants;
import com.vikram.locateme.utils.DialogHelper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by M1032130 on 6/20/2017.
 */

public class ApproveFragment extends Fragment implements IApproveView {
    IApprovePresenter approvePresenter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ApproveListAdapter mAdapter;
    private String authToken;
    private String clickedContact;
    private IOnConnectedUpdated onConnectedUpdated;
    private BroadcastReceiver receiver;

    public ApproveFragment() {
        approvePresenter = new ApprovePresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.APPROVE_INTENT);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                approvePresenter.getApproveContacts(authToken);
            }
        };
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onConnectedUpdated = (IOnConnectedUpdated) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement IOnConnectedUpdated");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onConnectedUpdated = null;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approve, container, false);
        authToken = this.getArguments().getString("authToken");

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        approvePresenter.getApproveContacts(authToken);
        return view;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onApproveContactReceived(List<ApproveContact> listApprove) {
        List<ApproveContact> myList = new CopyOnWriteArrayList<>();
        myList.addAll(listApprove);
        for (ApproveContact approve : myList) {
            if (approve.getApprove().equals("")) {
                myList.remove(approve);
            }
        }
        mAdapter = new ApproveListAdapter(getActivity(), myList, new ApproveListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final ApproveContact item) {
                AlertDialog.Builder builder = DialogHelper.showConfirmationDialog(getActivity(), "Do you want to share your location to connected contact");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        approvePresenter.connect(item.getApprove(), authToken);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.create().show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onApproveContactError(String error) {
        AlertDialog.Builder builder = DialogHelper.showErrorDialog(getActivity(), error);
        if (builder != null) {
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.create().show();
        }
    }

    @Override
    public void onConnected(String response) {
        AlertDialog.Builder builder = DialogHelper.showSuccessDialog(getActivity(), response);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                approvePresenter.getApproveContacts(authToken);
                if (onConnectedUpdated != null) {
                    onConnectedUpdated.onConnectedUpdated();
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onConnectionError(String response) {
        AlertDialog.Builder builder = DialogHelper.showErrorDialog(getActivity(), response);
        if (builder != null) {
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.create().show();
        }
    }
}