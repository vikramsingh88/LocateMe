package com.vikram.locateme.ui.main;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.TextView;

import com.vikram.locateme.R;
import com.vikram.locateme.accounts.AccountUtils;
import com.vikram.locateme.services.LocationService;
import com.vikram.locateme.ui.login.LoginActivity;
import com.vikram.locateme.ui.main.approve.ApproveFragment;
import com.vikram.locateme.ui.main.connected.ConnectedFragment;
import com.vikram.locateme.ui.main.contact.ContactFragment;
import com.vikram.locateme.ui.main.pending.PendingFragment;
import com.vikram.locateme.ui.settings.SettingActivity;
import com.vikram.locateme.utils.Constants;
import com.vikram.locateme.utils.CustomTypefaceSpan;
import com.vikram.locateme.utils.PermissionCallback;
import com.vikram.locateme.utils.PermissionHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements IOnConnectedUpdated,IOnPendingUpdated, PermissionCallback {
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;

    private AccountManager mAccountManager;
    private static final int REQ_LOGIN = 1;

    private ConnectedFragment connectedFragment;
    private PendingFragment pendingFragment;
    private ApproveFragment approveFragment;
    private ContactFragment contactFragment;
    private String title;
    private PermissionHelper mPermissionHelper;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccountManager = AccountManager.get(this);
        mAccountManager.getAuthTokenByFeatures(AccountUtils.ACCOUNT_TYPE, AccountUtils.AUTH_TOKEN_TYPE, null, this, null, null, new GetAuthTokenCallback(), null);

        setContentView(R.layout.activity_main);

        mPermissionHelper = new PermissionHelper(this);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        //setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        //setupTabLabels();
    }

    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            String[] permissions = { ACCESS_FINE_LOCATION};

            if (mPermissionHelper.isPermissionGranted(ACCESS_FINE_LOCATION)) {
                startService();
            } else {
                mPermissionHelper.requestPermissions(permissions, this);
            }
        } else {
            startService();
        }
    }

    private void startService() {
        Intent intent = new Intent(this, LocationService.class);
        intent.putExtra("authToken", authToken);
        startService(intent);
    }

    @Override
    public void onConnectedUpdated() {
        connectedFragment.updateContact();
    }

    @Override
    public void onPendingUpdated() {
        pendingFragment.updateContact();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionHelper.onRequestPermissionsResult(permissions, grantResults);
    }

    @Override
    public void onResponseReceived(HashMap<String, PermissionHelper.PermissionGrant> mapPermissionGrants) {
        PermissionHelper.PermissionGrant permissionGrant2 = mapPermissionGrants.get(ACCESS_FINE_LOCATION);
        if (permissionGrant2 != null) {
            switch (permissionGrant2) {
                case GRANTED:
                    //permission has been granted
                    startService();
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

    private class GetAuthTokenCallback implements AccountManagerCallback<Bundle> {

        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            Bundle bundle;

            try {
                bundle = result.getResult();

                final Intent intent = (Intent) bundle.get(AccountManager.KEY_INTENT);
                if (null != intent) {
                    startActivityForResult(intent, REQ_LOGIN);
                } else {
                    authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                    final String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
                    setupViewPager(authToken, mViewPager);
                    setupTabLabels();
                    getCurrentLocation();
                    // If the logged account didn't exist, we need to create it on the device
                    Account account = AccountUtils.getAccount(MainActivity.this, accountName);
                    if (null == account) {
                        account = new Account(accountName, AccountUtils.ACCOUNT_TYPE);
                        mAccountManager.addAccountExplicitly(account, bundle.getString(LoginActivity.PARAM_USER_PASSWORD), null);
                        mAccountManager.setAuthToken(account, AccountUtils.AUTH_TOKEN_TYPE, authToken);
                    }
                }
            } catch(OperationCanceledException e) {
                // If login was cancelled, force activity termination
                finish();
            } catch(Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void setupTabLabels() {
        TextView tabConnected = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_label, null);
        tabConnected.setText(R.string.connected);
        mTabLayout.getTabAt(0).setCustomView(tabConnected);

        TextView tabPending = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_label, null);
        tabPending.setText(R.string.pending);
        mTabLayout.getTabAt(1).setCustomView(tabPending);

        TextView tabApprove = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_label, null);
        tabApprove.setText(R.string.approve);
        mTabLayout.getTabAt(2).setCustomView(tabApprove);

        TextView tabContacts = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_label, null);
        tabContacts.setText(R.string.contacts);
        mTabLayout.getTabAt(3).setCustomView(tabContacts);

        if (title != null && title.contains("Approve")) {
            mTabLayout.setScrollPosition(2,0f,true);
            mViewPager.setCurrentItem(2);
        }
    }

    private void setupViewPager(String authToken, ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putString("authToken", authToken );

        connectedFragment = new ConnectedFragment();
        connectedFragment.setArguments(bundle);
        adapter.addFragment(connectedFragment, getString(R.string.connected));

        pendingFragment = new PendingFragment();
        pendingFragment.setArguments(bundle);
        adapter.addFragment(pendingFragment, getString(R.string.pending));

        approveFragment = new ApproveFragment();
        approveFragment.setArguments(bundle);
        adapter.addFragment(approveFragment, getString(R.string.approve));


        contactFragment = new ContactFragment();
        contactFragment.setArguments(bundle);
        adapter.addFragment(contactFragment, getString(R.string.contacts));

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        for (int i=0;i<menu.size();i++) {
            MenuItem mi = menu.getItem(i);
            //for applying a font to subMenu
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }
        return true;
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), Constants.FONT_NAME);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intentSettings = new Intent(this, SettingActivity.class);
            intentSettings.putExtra("authToken",authToken);
            startActivity(intentSettings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
