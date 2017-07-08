package com.vikram.locateme.ui.history;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.vikram.locateme.R;
import com.vikram.locateme.utils.DialogHelper;

import java.util.List;

public class HistoryActivity extends AppCompatActivity implements IHistoryView {
    private Toolbar mToolbar;
    private IHistoryPresenter historyPresenter;
    private Dialog mDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private HistoryListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        Intent intent = getIntent();
        String authToken = intent.getStringExtra("authToken");
        String contact = intent.getStringExtra("contact");

        historyPresenter = new HistoryPresenter(this);
        historyPresenter.getLocations(authToken, contact);
    }

    @Override
    public void showProgress() {
        mDialog = DialogHelper.showProgressDialog(this, getString(R.string.location_history));
        mDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void receivedLocations(List<LocationHistory> historyList) {
        mAdapter = new HistoryListAdapter(this, historyList, new HistoryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final LocationHistory item) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void error(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
