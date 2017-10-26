package com.example.grapes_pradip.vimalsagaradmin.activities.competition;

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
public class EditCompetitionQuestionActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private EditText edit_question;
    private Button btn_update;
    private String qid;
    private String question;
    private ImageView img_back;
    private TextView txt_header;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_competition_question);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        qid = intent.getStringExtra("ID");
        question = intent.getStringExtra("Question");
        edit_question = (EditText) findViewById(R.id.edit_question);
        btn_update = (Button) findViewById(R.id.btn_update);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_header = (TextView) findViewById(R.id.txt_header);

        btn_update.setText("Update");
        edit_question.setText(question);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        txt_header.setText("Edit Question");

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_question.getText().toString())) {
                    edit_question.setError("Enter question.");
                    edit_question.requestFocus();
                } else {
                    if (CommonMethod.isInternetConnected(EditCompetitionQuestionActivity.this)) {
                        new EditQuestion().execute(edit_question.getText().toString());
                    } else {
                        Toast.makeText(EditCompetitionQuestionActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private class EditQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditCompetitionQuestionActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("qid", qid));
            nameValuePairs.add(new BasicNameValuePair("Question", params[0]));
            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.updatequestioncompetition, nameValuePairs, EditCompetitionQuestionActivity.this);

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progressDialog.dismiss();
                    Toast.makeText(EditCompetitionQuestionActivity.this, "Competition question edit successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(EditCompetitionQuestionActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else {
                    Toast.makeText(EditCompetitionQuestionActivity.this, "Competition question not edit.", Toast.LENGTH_SHORT).show();
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
