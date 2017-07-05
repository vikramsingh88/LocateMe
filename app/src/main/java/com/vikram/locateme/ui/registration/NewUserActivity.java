package com.vikram.locateme.ui.registration;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;
import com.vikram.locateme.R;
import com.vikram.locateme.firbase.SharedPrefManager;
import com.vikram.locateme.services.LocationService;
import com.vikram.locateme.utils.Constants;
import com.vikram.locateme.utils.DialogHelper;
import com.vikram.locateme.utils.PermissionCallback;
import com.vikram.locateme.utils.PermissionHelper;

import java.util.HashMap;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class NewUserActivity extends AppCompatActivity implements INewUserView, PermissionCallback {
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private EditText mEditTextUserName;
    private EditText mEditTextPassword;
    private EditText mEditTextConfirmPassword;
    private EditText mEditTextLocation;
    private EditText mEditTextContact;
    private Button mButtonSignUp;
    private Dialog mDialog;
    private CountryCodePicker mCountryCodePicker;

    private INewUserPresenter newUserPresenter;
    private PermissionHelper mPermissionHelper;

    private String mCountryCode;

    private static final String TAG = "NewUserActivity";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000*2;
    private static final float LOCATION_DISTANCE = 10f;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            Log.d(TAG, "onLocationChanged: " + location.getLatitude());
            Log.d(TAG, "onLocationChanged: " + location.getLongitude());
            newUserPresenter.currentLocation(location.getLatitude(), location.getLongitude());
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    NewUserActivity.LocationListener[] mLocationListeners = new NewUserActivity.LocationListener[]{
            new NewUserActivity.LocationListener(LocationManager.GPS_PROVIDER),
            new NewUserActivity.LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mPermissionHelper = new PermissionHelper(this);

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Typeface tf = Typeface.createFromAsset(getAssets(), Constants.FONT_NAME);
        mCollapsingToolbar.setCollapsedTitleTypeface(tf);
        mCollapsingToolbar.setExpandedTitleTypeface(tf);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEditTextUserName = (EditText) findViewById(R.id.edit_user_name);
        mEditTextPassword = (EditText) findViewById(R.id.edit_password);
        mEditTextPassword.setTransformationMethod(new PasswordTransformationMethod());
        mEditTextConfirmPassword = (EditText) findViewById(R.id.edit_password2);
        mEditTextConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
        mEditTextLocation = (EditText) findViewById(R.id.edit_location);
        mEditTextContact = (EditText) findViewById(R.id.edit_contact);
        mButtonSignUp = (Button) findViewById(R.id.btn_sign_up);
        mCountryCodePicker   = (CountryCodePicker) findViewById(R.id.ccp);
        mCountryCodePicker.setTypeFace(tf);

        mCountryCode = mCountryCodePicker.getDefaultCountryCode();

        mCountryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                mCountryCode = mCountryCodePicker.getSelectedCountryCode();
            }
        });

        newUserPresenter = new NewUserPresenter(this);

        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUserPresenter.registration();
            }
        });

        getCurrentLocation();
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            String[] permissions = { ACCESS_FINE_LOCATION};

            if (mPermissionHelper.isPermissionGranted(ACCESS_FINE_LOCATION)) {
                //startService();
            } else {
                mPermissionHelper.requestPermissions(permissions, this);
            }
        } else {
            //startService();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        newUserPresenter.onDestroy();

        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        mDialog = DialogHelper.showProgressDialog(this, getString(R.string.reg_process));
        mDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void showUserNameEmptyError() {
        mEditTextUserName.setError(getString(R.string.user_name_required));
    }

    @Override
    public void showPasswordEmptyError() {
        mEditTextPassword.setError(getString(R.string.password_required));
    }

    @Override
    public void showConfirmPasswordEmptyError() {
        mEditTextConfirmPassword.setError(getString(R.string.password_required));
    }

    @Override
    public void showPasswordMismatchError() {
        mEditTextConfirmPassword.setError(getString(R.string.confirm_password_error));
    }

    @Override
    public void showLocationEmptyError() {
        mEditTextLocation.setError(getString(R.string.location_required));
    }

    @Override
    public void showContactEmptyError() {
        mEditTextContact.setError(getString(R.string.contact_required));
    }

    @Override
    public void onRegistrationSuccess(String response) {
        AlertDialog.Builder builder = DialogHelper.showSuccessDialog(this, response);
        builder.setPositiveButton("Navigate to login", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    public void onRegistrationFailed(String response) {
        AlertDialog.Builder builder = DialogHelper.showErrorDialog(this, response);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.create().show();
    }

    @Override
    public void onLocationAvailable(String location) {
        mEditTextLocation.setText(location);
    }

    @Override
    public void onLatLon(double lat, double lon) {

    }

    @Override
    public void onNoLatLon() {

    }

    @Override
    public String getUserName() {
        return mEditTextUserName.getText().toString();
    }

    @Override
    public String getPassword() {
        return mEditTextPassword.getText().toString();
    }

    @Override
    public String getConfirmPassword() {
        return mEditTextConfirmPassword.getText().toString();
    }

    @Override
    public String getLocation() {
        return mEditTextLocation.getText().toString();
    }

    @Override
    public String getContact() {
        return mEditTextContact.getText().toString();
    }

    @Override
    public String getCountryCode() {
        return mCountryCode;
    }

    @Override
    public String getDeviceId() {
        return SharedPrefManager.getInstance(this).getToken();
    }

    @Override
    public void onResponseReceived(HashMap<String, PermissionHelper.PermissionGrant> mapPermissionGrants) {
        PermissionHelper.PermissionGrant permissionGrant2 = mapPermissionGrants.get(ACCESS_FINE_LOCATION);

        if (permissionGrant2 != null) {
            switch (permissionGrant2) {
                case GRANTED:
                    //permission has been granted
                    //startService();
                    break;
                case DENIED:
                    //permission has been denied
                    Log.d("permissions ", "denied");
                    break;
                case NEVERSHOW:
                    //permission has been denied and never show has been selected. Open permission settings of the app.
                    Log.d("permissions ", "Denied with Never show");
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionHelper.onRequestPermissionsResult(permissions, grantResults);
    }
}
