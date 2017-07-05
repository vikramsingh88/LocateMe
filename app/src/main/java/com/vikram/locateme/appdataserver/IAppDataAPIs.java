package com.vikram.locateme.appdataserver;

import com.vikram.locateme.appdataserver.requestmodel.ApproveRequestObject;
import com.vikram.locateme.appdataserver.requestmodel.CheckUserRequestObject;
import com.vikram.locateme.appdataserver.requestmodel.ConnectRequestObject;
import com.vikram.locateme.appdataserver.requestmodel.ConnectedRequestObject;
import com.vikram.locateme.appdataserver.requestmodel.GetLocationRequestObject;
import com.vikram.locateme.appdataserver.requestmodel.LocationRequestObject;
import com.vikram.locateme.appdataserver.requestmodel.LoginRequestObject;
import com.vikram.locateme.appdataserver.requestmodel.PendingRequestObject;
import com.vikram.locateme.appdataserver.requestmodel.RegistrationRequestObject;
import com.vikram.locateme.appdataserver.requestmodel.SaveLocationRequestObject;
import com.vikram.locateme.appdataserver.requestmodel.ShareLocationRequestObject;
import com.vikram.locateme.ui.location.LocationResponse;
import com.vikram.locateme.ui.login.LoginResponse;
import com.vikram.locateme.ui.main.approve.ApproveResponse;
import com.vikram.locateme.ui.main.connected.ConnectedResponse;
import com.vikram.locateme.ui.main.pending.PendingResponse;
import com.vikram.locateme.ui.registration.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAppDataAPIs {

    String CONTENT_TYPE = "Content-Type";
    String JSON_TYPE = "application/json";
    String BASE_URL = "http://10.50.7.188:8080/";

    @POST("checkIfUserExist")
    Call<RetroResponse> checkIfUserExist(@Body CheckUserRequestObject body);

    @POST("checkIfContactExist")
    Call<RetroResponse> checkIfContactExist(@Body CheckUserRequestObject body);
    @POST("authenticate")
    Call<LoginResponse> authenticate(@Body LoginRequestObject body);

    @POST("registration")
    Call<User> registration(@Body RegistrationRequestObject body);

    @POST("requestlocation")
    Call<RetroResponse> requestLocationAccess(@Body LocationRequestObject body);

    @POST("getpendingcontacts")
    Call<PendingResponse> requestPendingContacts(@Body PendingRequestObject body);

    @POST("getapprovecontacts")
    Call<ApproveResponse> requestApproveContacts(@Body ApproveRequestObject body);

    @POST("getconnectedcontacts")
    Call<ConnectedResponse> requestConnectedContacts(@Body ConnectedRequestObject body);

    @POST("connect")
    Call<RetroResponse> requestConnect(@Body ConnectRequestObject body);

    @POST("addlocation")
    Call<RetroResponse> requestSaveLocationData(@Body SaveLocationRequestObject body);

    @POST("getlocation")
    Call<LocationResponse> requestGetLocationData(@Body GetLocationRequestObject body);

    @POST("sharelocation")
    Call<RetroResponse> requestShareLocation(@Body ShareLocationRequestObject body);
}
