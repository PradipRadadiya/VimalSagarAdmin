package com.example.grapes_pradip.vimalsagaradmin.activities.opinionpoll;

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
import android.widget.ProgressBar;
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
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class OpinionPollEditActivity extends AppCompatActivity implements View.OnClickListener {

    private String oid, yespoll, nopoll, question, totalpoll;
    private TextView txt_header;
    private EditText edit_title;
    private ImageView img_back;
    private Button btn_edit;
    private ProgressBar progress_yes;
    private ProgressBar progres_no;
    private TextView txtYes;
    private TextView txtNo;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opinionpoll_edit_activity);
        Intent intent = getIntent();
        oid = intent.getStringExtra("ID");
        yespoll = intent.getStringExtra("yes_polls");
        nopoll = intent.getStringExtra("no_polls");
        question = intent.getStringExtra("Ques");
        totalpoll = intent.getStringExtra("total_polls");

        findID();
        idClick();
        setContent();
    }


    private void findID() {
        edit_title = (EditText) findViewById(R.id.edit_titles);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        progres_no = (ProgressBar) findViewById(R.id.progres_no);
        progress_yes = (ProgressBar) findViewById(R.id.progress_yes);
        txtYes = (TextView) findViewById(R.id.txtYes);
        txtNo = (TextView) findViewById(R.id.txtNo);
        btn_edit.setText("Update");
    }

    private void idClick() {
        img_back.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void setContent() {
        edit_title.setText(question);
        txt_header.setText("OpinionPoll Edit");


        try {
            int ptotals = Integer.parseInt(totalpoll);
            int pyes = Integer.parseInt(yespoll);
            int pno = Integer.parseInt(nopoll);

            Log.e("final total", "-----------" + ptotals);
            Log.e("final pyes", "-----------" + pyes);
            Log.e("final pno", "-----------" + pno);


            float per = (pyes * 100) / ptotals;
            String yesper = String.valueOf(per);
            Log.e("yesper", "-----------" + yesper);
            float per1 = (pno * 100) / ptotals;
            String noper = String.valueOf(per1);
            Log.e("noper", "-----------" + noper);

            progress_yes.setMax(ptotals);
            progress_yes.setProgress(pyes);

            progres_no.setMax(ptotals);
            progres_no.setProgress(pno);
            String strYes = "Yes";
            String strNo = "No";
            txtYes.setText(yesper + "% " + strYes);
            txtNo.setText(noper + "% " + strNo);
        } catch (NumberFormatException | ArithmeticException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.btn_edit:

                if (TextUtils.isEmpty(edit_title.getText().toString())) {
                    edit_title.setError(getResources().getString(R.string.opinionpollquestion));
                    edit_title.requestFocus();
                } else {
                    if (CommonMethod.isInternetConnected(OpinionPollEditActivity.this)) {
                        new EditOpinionPoll().execute(edit_title.getText().toString());
                    } else {
                        Toast.makeText(OpinionPollEditActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private class EditOpinionPoll extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OpinionPollEditActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("qid", oid));
            nameValuePairs.add(new BasicNameValuePair("Ques", params[0]));
            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.updatequestion, nameValuePairs, OpinionPollEditActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(OpinionPollEditActivity.this, "Opinion poll edit successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(OpinionPollEditActivity.this, "Opinion poll not edit.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        }
    }


}
