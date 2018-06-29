package com.example.grapes_pradip.vimalsagaradmin.activities.note;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;

@SuppressLint("Registered")
public class NoteDetailActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private String modulename;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        TextView txt_header = (TextView) findViewById(R.id.txt_header);


        TextView txt_title = (TextView) findViewById(R.id.txt_title);
        TextView txt_decription = (TextView) findViewById(R.id.txt_decription);
        TextView txt_date = (TextView) findViewById(R.id.txt_date);
        TextView txt_time = (TextView) findViewById(R.id.txt_time);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt_header.setText("Note Detail");

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");

        txt_title.setText(CommonMethod.decodeEmoji(title));
        txt_decription.setText(CommonMethod.decodeEmoji(description));
        txt_date.setText(CommonMethod.decodeEmoji(date));
        txt_time.setText(CommonMethod.decodeEmoji(time));

    }



}
