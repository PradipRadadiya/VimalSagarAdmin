package com.example.grapes_pradip.vimalsagaradmin.activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.MainActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.admin.LoginActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.SharedPreferencesClass;


@SuppressWarnings("ALL")
public class Splash_Activity3 extends AppCompatActivity {
    Intent intent;
    private SharedPreferencesClass sharedPreferencesClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity3);
        sharedPreferencesClass = new SharedPreferencesClass(Splash_Activity3.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }


    private final CountDownTimer timer = new CountDownTimer(2000, 2000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (sharedPreferencesClass.getUser_Id().equalsIgnoreCase("")) {
                Intent intent = new Intent(Splash_Activity3.this, LoginActivity.class);
                startActivity(intent);
                finish();

            } else {
                Intent intent = new Intent(Splash_Activity3.this, MainActivity.class);
                sharedPreferencesClass.saveActionClick("main_click");
                intent.putExtra("click_action", "main_click");
                intent.putExtra("message", "message");
                startActivity(intent);
                finish();
            }
        }
    };
}
