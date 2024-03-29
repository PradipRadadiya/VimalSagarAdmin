package com.example.grapes_pradip.vimalsagaradmin.activities.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


@SuppressWarnings("ALL")
public class ImageViewActivity extends AppCompatActivity {
    private RelativeLayout rel_full_img;
    private SubsamplingScaleImageView imageView;
    private Bitmap myBitmap;
    private String image;
    private ProgressWheel p_wheel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagefull);
        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        TextView txt_header = (TextView) findViewById(R.id.txt_header);
        txt_header.setText("View Image");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView = (SubsamplingScaleImageView) findViewById(R.id.grid_fullImage);
        rel_full_img = (RelativeLayout) findViewById(R.id.rel_full_img);
        p_wheel = (ProgressWheel) findViewById(R.id.p_wheel);
        image = getIntent().getStringExtra("imagePath");
        Log.e("image path", "----------------" + image);
        if (CommonMethod.isInternetConnected(ImageViewActivity.this)) {
            new ImageLoad().execute();
        } else {
            imageView.setImage(ImageSource.resource(R.drawable.noimageavailable));
            p_wheel.setVisibility(View.GONE);
            Toast.makeText(ImageViewActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
        }
    }

    private class ImageLoad extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p_wheel.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(image);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            imageView.setImage(ImageSource.bitmap(myBitmap));
            p_wheel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
