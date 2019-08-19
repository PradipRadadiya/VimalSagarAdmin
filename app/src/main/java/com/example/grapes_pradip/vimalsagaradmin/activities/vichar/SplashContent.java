package com.example.grapes_pradip.vimalsagaradmin.activities.vichar;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

public class SplashContent extends AppCompatActivity {

    private EditText edit_title;
    private Button btn_add;
    private String vichartext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_content);
        bindId();
        toolbarClick();



        Intent intent = getIntent();
        String str=intent.getStringExtra("title");

        if (str.equalsIgnoreCase("")){
            new ContentGet().execute();
        }else {
            edit_title.setText(CommonMethod.decodeEmoji(intent.getStringExtra("title")));
        }




//        new ContentGet().execute();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edit_title.getText().toString())) {
                    edit_title.setError("Please enter title.");
                    edit_title.requestFocus();
                } else {
                    new ContentEdit().execute(CommonMethod.encodeEmoji(edit_title.getText().toString()), "1");
                    vichartext=CommonMethod.encodeEmoji(edit_title.getText().toString());
                }

            }
        });

    }


    private void toolbarClick() {
        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        TextView txt_header = (TextView) findViewById(R.id.txt_header);

        txt_header.setText(R.string.splash_content);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void bindId() {
        edit_title = (EditText) findViewById(R.id.edit_title);
        btn_add = (Button) findViewById(R.id.btn_add);
    }

    @SuppressLint("StaticFieldLeak")
    private class ContentEdit extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SplashContent.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("quote", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("qid", strings[1]));
            responseJson = JsonParser.postStringResponse(CommonURL.Main_url + "quote/editQuote", nameValuePairs, SplashContent.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("response", "------------------" + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progressDialog.dismiss();

                    new ContentGet().execute();
//                    new AddVichar().execute();
//                    finish();

                } else {
                    progressDialog.dismiss();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    @SuppressLint("StaticFieldLeak")
    private class ContentGet extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SplashContent.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                responseJSON = JsonParser.getStringResponse(CommonURL.Main_url+"quote/viewQuote");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("reponse", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                    progressDialog.dismiss();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("status", "-----------------success");


                    JSONObject object = jsonArray.getJSONObject(0);

                    Log.e("quote", "-------------" + object.getString("title"));

                    edit_title.setText(CommonMethod.decodeEmoji(object.getString("title")));

                    Log.e("array", "-----------------success");


                } else {
                    progressDialog.dismiss();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @SuppressLint("StaticFieldLeak")
    private class AddVichar extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SplashContent.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONObject jsonObject1 = new JSONObject();
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("vichar", strings[0]));
            responseJson = JsonParser.postStringResponse(CommonURL.Main_url + "quote/editQuote", nameValuePairs, SplashContent.this);
            return responseJson;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("response", "------------------" + s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progressDialog.dismiss();

                    new ContentGet().execute();
//                    finish();

                } else {
                    progressDialog.dismiss();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

}
