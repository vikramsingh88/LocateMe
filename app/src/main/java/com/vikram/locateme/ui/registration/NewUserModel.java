package com.vikram.locateme.ui.registration;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.vikram.locateme.appdataserver.AppDataClient;
import com.vikram.locateme.appdataserver.RetroResponse;
import com.vikram.locateme.appdataserver.requestmodel.CheckUserRequestObject;
import com.vikram.locateme.appdataserver.requestmodel.RegistrationRequestObject;
import com.vikram.locateme.appdataserver.requests.RequestCheckUserNameExists;
import com.vikram.locateme.appdataserver.requests.RequestRegistration;
import com.vikram.locateme.services.GeocodeAddressIntentService;
import com.vikram.locateme.utils.AddressResultReceiver;
import com.vikram.locateme.utils.Constants;
import com.vikram.locateme.utils.LocateApplication;

/**
 * Created by M1032130 on 6/21/2017.
 */

public class NewUserModel implements INewUserModel,AddressResultReceiver.Receiver {
    private AddressResultReceiver mReceiver;
    private Context mContext;
    private OnLocationListener mListener;
    int fetchType = Constants.USE_ADDRESS_LOCATION;

    public NewUserModel(Context context) {
        mContext = context;
    }

    @Override
    public void checkUserNameAvailability(String userName, final OnUserCheckFinishedListener listener) {
        new RequestCheckUserNameExists(new CheckUserRequestObject(userName, null, null), new AppDataClient
                .OnDataReceived<RetroResponse>() {

            @Override
            public void onDataSuccess(RetroResponse checkUserResponseModel) {
                Log.d("RequestEmailCheck", "onDataSuccess " + checkUserResponseModel.getStatusMessage());
                listener.onUserNameExist();
            }

            @Override
            public void onDataFailure(String error) {
                Log.d("RequestEmailCheck", "onDataFailure " + error.toString());
                listener.onNewUser();
            }
        }).callService();
    }

    @Override
    public void registration(User user, final OnRegistrationFinishedListener listener) {
        new RequestRegistration(new RegistrationRequestObject(user.getUserName(), user.getPassword(), user.getLocation(), user.getContact(), user.getDeviceId()), new AppDataClient
                .OnDataReceived<User>() {

            @Override
            public void onDataSuccess(User newUser) {
                Log.d("RequestEmailCheck", "onDataSuccess " + newUser.getStatusMessage());
                listener.onRegistrationSuccess(newUser.getMessage());
            }

            @Override
            public void onDataFailure(String error) {
                Log.d("RequestEmailCheck", "onDataFailure " + error.toString());
                listener.onRegistrationFailed(error);
            }
        }).callService();
    }

    @Override
    public void getLocation(double lat, double lon, OnLocationListener listener) {
        mListener = listener;
        mReceiver = new AddressResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(mContext, GeocodeAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mReceiver);
        intent.putExtra(Constants.FETCH_TYPE_EXTRA, fetchType);
        intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA, lat);
        intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA, lon);

        mContext.startService(intent);
    }

    @Override
    public void getCurrentLatLon(OnLatLonListener listener) {

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == Constants.SUCCESS_RESULT) {
            Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
            if (mListener != null) {
                mListener.onLocationAvailable(address.getAddressLine(0)+" "+address.getLocality()+ " "+address.getAdminArea()+" "+address.getPostalCode()
                +" "+address.getCountryName());
            }
        } else {
            if (mListener != null) {
                mListener.onLocationError("Error");
            }
        }
    }
}
