package com.example.grapes_pradip.vimalsagaradmin.activities.information;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

/**
 * Created by Grapes-Pradip on 2/16/2017.
 */

@SuppressWarnings("ALL")
public class EditInformationActivity extends AppCompatActivity {
    private String id;
    private String title;
    private String description;
    private String address;
    private String date;
    private EditText edit_title;
    private EditText edit_description;
    private EditText edit_date;
    private EditText edit_address;
    private ProgressDialog progressDialog;
    private Button button_update;
    private TextView txt_header;
    private ImageView img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_information);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        id = intent.getStringExtra("info_id");
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");
        address = intent.getStringExtra("address");
        date = intent.getStringExtra("date");
        findID();
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_title.getText().toString())) {
                    edit_title.setError(getResources().getString(R.string.infotitle));
                    edit_title.requestFocus();
                } else if (TextUtils.isEmpty(edit_description.getText().toString())) {
                    edit_description.setError(getResources().getString(R.string.infodes));
                    edit_description.requestFocus();
                } else if (TextUtils.isEmpty(edit_address.getText().toString())) {
                    edit_address.setError(getResources().getString(R.string.infoaddress));
                    edit_address.requestFocus();
                } else {
                    if (CommonMethod.isInternetConnected(EditInformationActivity.this)) {
                        new EditInformation().execute(id, edit_title.getText().toString(), edit_description.getText().toString(), date, edit_address.getText().toString());
                    }
                    else {
                        Toast.makeText(EditInformationActivity.this,R.string.internet,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_description = (EditText) findViewById(R.id.edit_description);
        edit_date = (EditText) findViewById(R.id.edit_date);
        edit_address = (EditText) findViewById(R.id.edit_address);
        button_update = (Button) findViewById(R.id.button_update);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_header.setText("Edit Information");
        edit_title.setText(title);
        edit_description.setText(description);
        edit_date.setText(date);
        edit_address.setText(address);
    }

    private class EditInformation extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditInformationActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("infoid", params[0]));
                nameValuePairs.add(new BasicNameValuePair("Title", params[1]));
                nameValuePairs.add(new BasicNameValuePair("Description", params[2]));
                nameValuePairs.add(new BasicNameValuePair("Date", params[3]));
                nameValuePairs.add(new BasicNameValuePair("Address", params[4]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.editInformation, nameValuePairs, EditInformationActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @SuppressLint("ShowToast")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
//                    Toast.makeText(EditInformationActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(EditInformationActivity.this, "Information edit successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                } else {
                    Toast.makeText(EditInformationActivity.this, "Infromation not edit." , Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
