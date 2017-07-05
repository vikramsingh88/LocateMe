package com.vikram.locateme.ui.login;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vikram.locateme.R;
import com.vikram.locateme.accounts.AccountUtils;
import com.vikram.locateme.firbase.SharedPrefManager;
import com.vikram.locateme.ui.registration.NewUserActivity;
import com.vikram.locateme.utils.Constants;
import com.vikram.locateme.utils.DialogHelper;

public class LoginActivity extends AccountAuthenticatorActivity implements AppCompatCallback, ILoginView {

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private EditText mEditTextPassword;
    private EditText mEditTextUserName;
    private Button mButtonLogin;
    private TextView mTextViewNewUser;
    private AppCompatDelegate mDelegate;
    private ILoginPresenter mLoginPresenter;
    private AccountManager mAccountManager;
    private Dialog mDialog;

    public static final String ARG_ACCOUNT_TYPE = "accountType";
    public static final String ARG_AUTH_TOKEN_TYPE = "authTokenType";
    public static final String ARG_IS_ADDING_NEW_ACCOUNT = "isAddingNewAccount";
    public static final String PARAM_USER_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate = AppCompatDelegate.create(this, this);
        //we need to call the onCreate() of the AppCompatDelegate
        mDelegate.onCreate(savedInstanceState);
        //we use the delegate to inflate the layout
        mDelegate.setContentView(R.layout.activity_login);
        //Finally, let's add the Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDelegate.setSupportActionBar(mToolbar);

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Typeface tf = Typeface.createFromAsset(getAssets(), Constants.FONT_NAME);
        mCollapsingToolbar.setCollapsedTitleTypeface(tf);
        mCollapsingToolbar.setExpandedTitleTypeface(tf);

        mButtonLogin = (Button) findViewById(R.id.btn_login);
        mEditTextUserName = (EditText) findViewById(R.id.edit_user_name);
        mEditTextPassword = (EditText) findViewById(R.id.edit_password);
        mEditTextPassword.setTransformationMethod(new PasswordTransformationMethod());
        mTextViewNewUser = (TextView) findViewById(R.id.text_lbl_new_user);

        mLoginPresenter = new LoginPresenter(this);
        mAccountManager = AccountManager.get(this);

        mTextViewNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, NewUserActivity.class));
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPresenter.authenticate();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.onDestroy();
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
        //let's leave this empty, for now
    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {
        // let's leave this empty, for now
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public void showProgressBar() {
        mDialog = DialogHelper.showProgressDialog(this, getString(R.string.login_process));
        mDialog.show();
    }

    @Override
    public void hideProgressBar() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void showUserNameEmptyFieldError() {
        mEditTextUserName.setError(getString(R.string.user_name_required));
    }

    @Override
    public void showPasswordEmptyFieldError() {
        mEditTextPassword.setError(getString(R.string.password_required));
    }

    @Override
    public void showLoginFailedError(String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginSuccess(String response) {
        Intent res = new Intent();
        res.putExtra(AccountManager.KEY_ACCOUNT_NAME, getUserName());
        res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AccountUtils.ACCOUNT_TYPE);
        res.putExtra(AccountManager.KEY_AUTHTOKEN, response); //response is auth token
        res.putExtra(PARAM_USER_PASSWORD, getPassword());
        finishLogin(res);
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
    public String getDeviceId() {
        return SharedPrefManager.getInstance(this).getToken();
    }

    private void finishLogin(Intent intent) {
        final String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        final String accountPassword = intent.getStringExtra(PARAM_USER_PASSWORD);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
        String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, AccountUtils.AUTH_TOKEN_TYPE, authToken);
        } else {
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(AccountAuthenticatorActivity.RESULT_OK, intent);

        finish();
    }
}
