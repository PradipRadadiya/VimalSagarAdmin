package com.example.grapes_pradip.vimalsagaradmin.activities.information;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
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
public class AddInformationActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private EditText e_title;
    private EditText e_description;
    private EditText e_address;
    private EditText e_date;
    private TextView txt_header;
    private ImageView img_back;
    private Button btn_add;
    private Switch notificationswitch;
    String notify="0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_information);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        findID();

    }

    private void setContent() {
        if (TextUtils.isEmpty(e_title.getText().toString())) {
            e_title.setError(getResources().getString(R.string.infotitle));
            e_title.requestFocus();
        } else if (TextUtils.isEmpty(e_description.getText().toString())) {
            e_description.setError(getResources().getString(R.string.infodes));
            e_description.requestFocus();
        } else if (TextUtils.isEmpty(e_address.getText().toString())) {
            e_address.setError(getResources().getString(R.string.infoaddress));
            e_address.requestFocus();
        } else  {
            if (CommonMethod.isInternetConnected(AddInformationActivity.this)) {
                new AddInformation().execute(e_title.getText().toString(),CommonMethod.encodeEmoji(e_description.getText().toString()), CommonMethod.encodeEmoji(e_date.getText().toString()), CommonMethod.encodeEmoji(e_address.getText().toString()),notify);
            }
            else {
                Toast.makeText(AddInformationActivity.this,R.string.internet,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        e_title = (EditText) findViewById(R.id.e_title);
        e_description = (EditText) findViewById(R.id.e_description);
        e_address = (EditText) findViewById(R.id.e_address);
        e_date = (EditText) findViewById(R.id.e_date);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_add = (Button) findViewById(R.id.btn_add);
        txt_header.setText("Add Information");

        notificationswitch= (Switch) findViewById(R.id.notificationswitch);
        notificationswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    notify="0";
                    Log.e("checked","----------------"+isChecked);
                }else{
                    notify="1";
                    Log.e("checked","----------------"+isChecked);

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
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContent();
            }
        });
    }

    private class AddInformation extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddInformationActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("Title", params[0]));
                    nameValuePairs.add(new BasicNameValuePair("Description", params[1]));
                    nameValuePairs.add(new BasicNameValuePair("Date", params[2]));
                nameValuePairs.add(new BasicNameValuePair("Address", params[3]));
                nameValuePairs.add(new BasicNameValuePair("Is_notify", params[4]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.addInformation, nameValuePairs, AddInformationActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @SuppressLint("ShowToast")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(AddInformationActivity.this, "Infromation added successfully.", Toast.LENGTH_SHORT).show();
                    e_title.setText("");
                    e_description.setText("");
                    e_address.setText("");
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(AddInformationActivity.this, "Infromation not added.", Toast.LENGTH_SHORT).show();
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
