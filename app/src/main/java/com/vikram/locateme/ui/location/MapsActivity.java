package com.vikram.locateme.ui.location;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vikram.locateme.R;
import com.vikram.locateme.services.BoundLocationService;
import com.vikram.locateme.utils.DialogHelper;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, ILocationView {

    private GoogleMap mMap;
    private Toolbar mToolbar;
    private Dialog mDialog;
    private String authToken;
    private String contact;
    private String latLon;
    private ILocationPresenter locationPresenter;

    BoundLocationService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        locationPresenter = new LocationPresenter(this);
        Intent intent = getIntent();
        authToken = intent.getStringExtra("authToken");
        contact = intent.getStringExtra("contact");
        latLon = intent.getStringExtra("latLon");
        if (authToken != null && !authToken.equals("")) {
            locationPresenter.getLocation(authToken, contact);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BoundLocationService.LocalBinder binder = (BoundLocationService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, BoundLocationService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (latLon!= null && !latLon.equals("")) {
            Location loc = new Location();
            loc.setLatlon(latLon);
            List<Location> list = new ArrayList<>();
            list.add(loc);
            receivedLocation(list);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationPresenter.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_share:
                //get current location
                if (mBound) {
                    String latLon = mService.getCurrentLocation();
                    locationPresenter.shareLocation(authToken, contact, latLon);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showProgress() {
        mDialog = DialogHelper.showProgressDialog(this, getString(R.string.fetch_location));
        mDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void receivedLocation(List<Location> locations) {
        for (Location location : locations) {
            LatLng loc = new LatLng(Double.valueOf(location.getLatlon().split(":")[0]), Double.valueOf(location.getLatlon().split(":")[1]));
            mMap.addMarker(new MarkerOptions().position(loc));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 18.0f));
        }
    }

    @Override
    public void noLocation(String error) {
        AlertDialog.Builder builder = DialogHelper.showErrorDialog(this, error);
        if (builder != null) {
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void locationShared(String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
    }
}
