package com.example.grapes_pradip.vimalsagaradmin.activities.thought;

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
import android.widget.LinearLayout;
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
public class EditThoughtActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private EditText edit_title;
    private EditText edit_description;
    private TextView txt_header;
    private ImageView img_back;
    private Button btn_add;
    private String tid;
    private String title;
    private String description;
    LinearLayout lin_noti;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_thought);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        tid = intent.getStringExtra("tid");
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");
        findID();

    }

    private void setContent() {
        if (CommonMethod.isInternetConnected(EditThoughtActivity.this)) {

            if (TextUtils.isEmpty(edit_title.getText().toString())) {
                edit_title.setError(getResources().getString(R.string.thoughttitle));
                edit_title.requestFocus();
            } else if (TextUtils.isEmpty(edit_description.getText().toString())) {
                edit_description.setError(getResources().getString(R.string.thoughtdes));
                edit_description.requestFocus();
            } else {
                if (CommonMethod.isInternetConnected(EditThoughtActivity.this)) {
                    new EditThought().execute(tid, CommonMethod.encodeEmoji(edit_title.getText().toString()), CommonMethod.encodeEmoji(edit_description.getText().toString()));
                }
                else {
                    Toast.makeText(EditThoughtActivity.this,R.string.internet,Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        lin_noti= (LinearLayout) findViewById(R.id.lin_noti);
        lin_noti.setVisibility(View.GONE);
        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_description = (EditText) findViewById(R.id.edit_description);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_add = (Button) findViewById(R.id.btn_add);
        txt_header.setText("Edit Thought");
        btn_add.setText("Update");
        edit_title.setText(title);
        edit_description.setText(description);
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

    private class EditThought extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditThoughtActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("tid", params[0]));
                nameValuePairs.add(new BasicNameValuePair("Title", params[1]));
                nameValuePairs.add(new BasicNameValuePair("Description", params[2]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.editthought, nameValuePairs, EditThoughtActivity.this);
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
                    Toast.makeText(EditThoughtActivity.this, "Thought edit successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(EditThoughtActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    edit_title.setText("");
                    edit_description.setText("");
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(EditThoughtActivity.this, "Thought not edit.", Toast.LENGTH_SHORT).show();
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
