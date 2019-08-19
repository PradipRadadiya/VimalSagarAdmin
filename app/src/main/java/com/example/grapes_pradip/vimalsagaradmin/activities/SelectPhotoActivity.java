package com.example.grapes_pradip.vimalsagaradmin.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.isseiaoki.simplecropview.CropImageView;

public class SelectPhotoActivity extends AppCompatActivity {


    CropImageView mCropView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        mCropView = (CropImageView) findViewById(R.id.cropImageView);

    }
}
