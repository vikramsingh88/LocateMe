package com.vikram.locateme.ui.settings;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vikram.locateme.R;
import com.vikram.locateme.services.LocationService;
import com.vikram.locateme.utils.Constants;
import com.vikram.locateme.utils.DialogHelper;

public class SettingActivity extends AppCompatActivity implements ISettingsView {
    private Toolbar mToolbar;
    private SeekBar mSeekBar;
    private TextView mTextViewInterval;
    private CheckBox mCheckBoxVisibility;
    private CheckBox mCheckBoxLocationService;
    private SettingPreference mSettingPreference;
    private String authToken;
    private Dialog mDialog;
    private ISettingsPresenter settingsPresenter;
    private LocationManager mLocationManager;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE );

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.PROVIDER_CHANGED);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean statusOfGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (statusOfGPS) {
                    Intent intent2 = new Intent(SettingActivity.this, LocationService.class);
                    intent2.putExtra("authToken", authToken);
                    startService(intent2);
                } else {
                    stopService(new Intent(SettingActivity.this, LocationService.class));
                }
                mCheckBoxLocationService.setChecked(statusOfGPS);
            }
        };
        registerReceiver(receiver, filter);

        settingsPresenter = new SettingsPresenter(this);

        Intent intent = getIntent();
        authToken = intent.getStringExtra("authToken");

        mSettingPreference = SettingPreference.getInstance(this);

        mCheckBoxLocationService= (CheckBox) findViewById(R.id.checkBox_location_service);
        boolean statusOfGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        mCheckBoxLocationService.setChecked(statusOfGPS);

        mCheckBoxLocationService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        mCheckBoxVisibility = (CheckBox) findViewById(R.id.checkBox_hide_me);
        if (mSettingPreference.getLocationVisibility() != null && mSettingPreference.getLocationVisibility().equals("close")) {
            mCheckBoxVisibility.setChecked(true);
        } else {
            mCheckBoxVisibility.setChecked(false);
        }

        mSeekBar = (SeekBar)findViewById(R.id.seekBar);
        mTextViewInterval = (TextView)findViewById(R.id.txt_lbl_location_update_interval);

        mTextViewInterval.setText(getString(R.string.location_update_interval)+ " - "+mSettingPreference.getLocationInterval() +" Minutes");
        mSeekBar.setProgress((int)mSettingPreference.getLocationInterval());

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                mTextViewInterval.setText(getString(R.string.location_update_interval)+ " - "+progress +" Minutes");
                mSettingPreference.setLocationInterval(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                stopService(new Intent(SettingActivity.this, LocationService.class));
                Intent intent = new Intent(SettingActivity.this, LocationService.class);
                intent.putExtra("authToken", authToken);
                startService(intent);
            }
        });

        mCheckBoxVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsPresenter.updateLocationVisibility();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean statusOfGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        mCheckBoxLocationService.setChecked(statusOfGPS);
    }

    @Override
    public void onDestroy() {
        settingsPresenter.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        mDialog = DialogHelper.showProgressDialog(this, getString(R.string.updating_visibility));
        mDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void locationVisibilityUpdated() {
        if (mCheckBoxVisibility.isChecked()) {
            mSettingPreference.setLocationVisibility("close");
        } else {
            mSettingPreference.setLocationVisibility("open");
        }
    }

    @Override
    public void locationVisibilityUpdateError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getAuthToken() {
        return authToken;
    }

    @Override
    public String getVisibility() {
        if (mCheckBoxVisibility.isChecked()) {
            return "close";
        } else {
            return "open";
        }
    }
}
