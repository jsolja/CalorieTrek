package foi.hr.calorietrek.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import foi.hr.calorietrek.ui.login.view.LoginActivity;

/**
 * Created by Andrea on 29.10.2017.
 * * Activity class used for splash screen.
 */

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_TIME_OUT = 2000; //2 sekunde Splash screen-a

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
