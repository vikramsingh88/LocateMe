package com.vikram.locateme.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.vikram.locateme.R;
import com.vikram.locateme.ui.main.MainActivity;
import com.vikram.locateme.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intentLogin = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intentLogin);
                finish();
            }
        }, Constants.SPLASH_TIME_OUT);
    }
}
