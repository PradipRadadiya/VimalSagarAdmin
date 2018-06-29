package com.example.grapes_pradip.vimalsagaradmin.activities.gallery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.adapters.gallery.CustomImageAdapter;

import static com.example.grapes_pradip.vimalsagaradmin.activities.gallery.AllGalleryActivity.itemSplashArrayList;



@SuppressWarnings("ALL")
public class SlidingGalleryImage extends AppCompatActivity {


    private ViewPager viewpager_splash;
    //    private CirclePageIndicator indicator;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    //    private ArrayList<ImageItemSplash> itemSplashArrayList = new ArrayList<>();
    private CustomImageAdapter customImageAdapter;
    private TextView txt_skip;
    private LinearLayout lin_join_now;
    private ProgressDialog loadingProgressDialog;
    String cid;
    int item;
    String pos;
    ImageView img_back;
    TextView txt_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagesliding);
        viewpager_splash = (ViewPager) findViewById(R.id.viewpager_image);
        img_back= (ImageView) findViewById(R.id.img_back);
        txt_header= (TextView) findViewById(R.id.txt_header);
        txt_header.setText("Image Sliding");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        pos = intent.getStringExtra("position");
//        cid = intent.getStringExtra("cid");
        Log.e("position", "------------------" + pos);
//        String u = "drawable/noimageavailable.png";

        Log.e("itemSplashArrayList","----------------"+itemSplashArrayList);

        customImageAdapter = new CustomImageAdapter(SlidingGalleryImage.this, itemSplashArrayList);
        item = Integer.parseInt(pos);
        viewpager_splash.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewpager_splash.setCurrentItem(item, true);
            }
        }, 100);
        viewpager_splash.setAdapter(customImageAdapter);

        Log.e("image length", "" + itemSplashArrayList.size());

    }


}
