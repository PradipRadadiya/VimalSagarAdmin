package com.example.grapes_pradip.vimalsagaradmin.activities.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grapes_pradip.vimalsagaradmin.R;



@SuppressWarnings("DefaultFileTemplate")
public class UserDetailActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);
        EditText edit_user_name = (EditText) findViewById(R.id.edit_user_name);
        EditText edit_user_email = (EditText) findViewById(R.id.edit_user_email);
        EditText edit_user_phone = (EditText) findViewById(R.id.edit_user_phone);
        EditText edit_user_address = (EditText) findViewById(R.id.edit_user_address);
        EditText edit_user_date = (EditText) findViewById(R.id.edit_user_date);
        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        TextView txt_header = (TextView) findViewById(R.id.txt_header);


        Intent intent=getIntent();
        edit_user_name.setText(intent.getStringExtra("name"));
        edit_user_email.setText(intent.getStringExtra("emailid"));
        edit_user_phone.setText(intent.getStringExtra("phone"));
        edit_user_address.setText(intent.getStringExtra("address"));
        edit_user_date.setText(intent.getStringExtra("date"));
        txt_header.setText("User Detail");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
