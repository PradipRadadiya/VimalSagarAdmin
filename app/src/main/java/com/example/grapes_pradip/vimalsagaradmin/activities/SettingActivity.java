package com.example.grapes_pradip.vimalsagaradmin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.SharedPreferencesClass;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    private SharedPreferencesClass sharedpreferance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting);
        sharedpreferance = new SharedPreferencesClass(SettingActivity.this);


        Intent intent=getIntent();
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList=  intent.getStringArrayListExtra("list");
        Log.e("list","------------------"+arrayList);


        final ToggleButton pushonoff = (ToggleButton) findViewById(R.id.pushonoff);
        ImageView img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (sharedpreferance.getPushNotification().equalsIgnoreCase("pushon")) {
            pushonoff.setChecked(true);
        } else {
            pushonoff.setChecked(false);
        }
        pushonoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    sharedpreferance.savePushNotification("pushon");
                    pushonoff.setChecked(true);
                    Toast.makeText(SettingActivity.this, "Push notification on.", Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    sharedpreferance.savePushNotification("pushoff");
                    pushonoff.setChecked(false);
                    Toast.makeText(SettingActivity.this, "Push notification off.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
