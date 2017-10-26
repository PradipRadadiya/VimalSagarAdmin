package com.example.grapes_pradip.vimalsagaradmin.activities.thought;

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
public class AddThoughtActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private EditText edit_title;
    private EditText edit_description;
    private TextView txt_header;
    private ImageView img_back;
    private Button btn_add;
    private Switch notificationswitch;
    String notify="0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_thought);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        findID();

    }

    private void setContent() {
        if (TextUtils.isEmpty(edit_title.getText().toString())) {
            edit_title.setError(getResources().getString(R.string.thoughttitle));
            edit_title.requestFocus();
        } else if (TextUtils.isEmpty(edit_description.getText().toString())) {
            edit_description.setError(getResources().getString(R.string.thoughtdes));
            edit_description.requestFocus();
        } else {
            if (CommonMethod.isInternetConnected(AddThoughtActivity.this)) {
                new AddThought().execute(edit_title.getText().toString(), edit_description.getText().toString(),notify);
            }
            else {
                Toast.makeText(AddThoughtActivity.this,R.string.internet,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_description = (EditText) findViewById(R.id.edit_description);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_add = (Button) findViewById(R.id.btn_add);
        txt_header.setText("Add Thought");
        notificationswitch= (Switch) findViewById(R.id.notificationswitch);

        notificationswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Log.e("checked","-----------------"+isChecked);
                    notify="0";
                }else{
                    Log.e("checked","-----------------"+isChecked);
                    notify="1";

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

    private class AddThought extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddThoughtActivity.this);
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
                nameValuePairs.add(new BasicNameValuePair("Is_notify", params[2]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.addThought, nameValuePairs, AddThoughtActivity.this);
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
                    Toast.makeText(AddThoughtActivity.this, "Thought added successfully.", Toast.LENGTH_SHORT).show();
                    edit_title.setText("");
                    edit_description.setText("");
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(AddThoughtActivity.this, "Thought not added." , Toast.LENGTH_SHORT).show();
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
