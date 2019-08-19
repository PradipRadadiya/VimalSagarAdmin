package com.example.grapes_pradip.vimalsagaradmin.activities.competition;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.fragments.FailUserFragment;
import com.example.grapes_pradip.vimalsagaradmin.fragments.PassUserFragment;

public class ViewResultActivity extends AppCompatActivity {

    private TextView txt_pending_qa, txt_approve_qa;
    private String cid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);
        cid = getIntent().getStringExtra("competitioncategory_id");
        findID();
        idClick();
        toolbarClick();
        openPass();
    }

    @SuppressLint("SetTextI18n")
    private void toolbarClick() {
        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        TextView txt_header = (TextView) findViewById(R.id.txt_header);
        txt_header.setText("Results");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void idClick() {
        txt_pending_qa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPass();
            }
        });

        txt_approve_qa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFail();
            }
        });
    }


    private void findID() {
        txt_pending_qa = (TextView) findViewById(R.id.txt_pending_qa);
        txt_approve_qa = (TextView) findViewById(R.id.txt_approve_qa);
    }


    private void openPass() {
        txt_pending_qa.setBackgroundColor(getResources().getColor(R.color.green));
        txt_approve_qa.setBackgroundColor(getResources().getColor(R.color.white));

        Bundle bundle = new Bundle();

        bundle.putString("cid", cid);

        Fragment fr;
        fr = new PassUserFragment();
        fr.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_result, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openFail() {

        txt_pending_qa.setBackgroundColor(getResources().getColor(R.color.white));
        txt_approve_qa.setBackgroundColor(getResources().getColor(R.color.green));
        Bundle bundle = new Bundle();

        bundle.putString("cid", cid);
        Fragment fr;
        fr = new FailUserFragment();
        fr.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_result, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("back", "-------------------");
    }
}
