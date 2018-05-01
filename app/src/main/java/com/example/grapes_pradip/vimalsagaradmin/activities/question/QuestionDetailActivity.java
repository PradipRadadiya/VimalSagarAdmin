package com.example.grapes_pradip.vimalsagaradmin.activities.question;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.example.grapes_pradip.vimalsagaradmin.activities.admin.UserViewActivity;
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
public class QuestionDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String qid;
    private String question;
    private String answer;
    private String date;
    private String isApprove;
    private String userid;
    private String name;
    private String view;
    private TextView txt_views;
    private TextView txt_title;
    private TextView txt_date;
    private TextView txt_header;
    private TextView txt_name;
    private TextView txt_approve;
    private TextView txt_reject;
    private EditText edit_answer;
    private ImageView img_back;
    private Button btn_reply;
    private LinearLayout lin_approve;
    private LinearLayout lin_replay_answer;
    private int status;
    private ProgressDialog progressDialog;
    private TextView txt_userdetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_detail_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        qid = intent.getStringExtra("qid");
        question = intent.getStringExtra("question");
        answer = intent.getStringExtra("answer");
        date = intent.getStringExtra("date");
        isApprove = intent.getStringExtra("isApprove");
        userid = intent.getStringExtra("userid");
        name = intent.getStringExtra("name");
        view = intent.getStringExtra("view");
        findID();
        idClick();
        setContent();

        txt_userdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(QuestionDetailActivity.this, UserViewActivity.class);
                intent1.putExtra("uid", userid);
                startActivity(intent1);
            }
        });
    }


    private void findID() {
        txt_userdetail = (TextView) findViewById(R.id.txt_userdetail);
        txt_name = (TextView) findViewById(R.id.txt_unm);
        txt_title = (TextView) findViewById(R.id.txt_titles);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_approve = (TextView) findViewById(R.id.txt_approve);
        txt_reject = (TextView) findViewById(R.id.txt_reject);
        txt_views = (TextView) findViewById(R.id.txt_views);
        img_back = (ImageView) findViewById(R.id.img_back);
        edit_answer = (EditText) findViewById(R.id.edit_answer_reply);
        btn_reply = (Button) findViewById(R.id.btn_reply);
        lin_approve = (LinearLayout) findViewById(R.id.lin_approve);
        lin_replay_answer = (LinearLayout) findViewById(R.id.lin_replay_answer);
    }

    private void idClick() {
        img_back.setOnClickListener(this);
        btn_reply.setOnClickListener(this);
        txt_approve.setOnClickListener(this);
        txt_reject.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void setContent() {
        txt_title.setText(CommonMethod.decodeEmoji(question));
        txt_date.setText(CommonMethod.decodeEmoji(date));
        txt_name.setText(CommonMethod.decodeEmoji(name));
        txt_header.setText("Question Detail");
        txt_views.setText(CommonMethod.decodeEmoji(view));
        if (isApprove.equalsIgnoreCase("R")) {
            lin_replay_answer.setVisibility(View.GONE);
            lin_approve.setVisibility(View.GONE);
            Toast.makeText(QuestionDetailActivity.this, "This question rejected.", Toast.LENGTH_SHORT).show();
        } else {
            status = Integer.parseInt(isApprove);
            if (status == 1) {
                lin_replay_answer.setVisibility(View.VISIBLE);
                lin_approve.setVisibility(View.GONE);
                edit_answer.setText(CommonMethod.decodeEmoji(answer));
            } else {
                lin_replay_answer.setVisibility(View.GONE);
                lin_approve.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.btn_reply:
                if (TextUtils.isEmpty(edit_answer.getText().toString())) {
                    edit_answer.setError(getResources().getString(R.string.questionreply));
                    edit_answer.requestFocus();
                } else {
                    if (CommonMethod.isInternetConnected(QuestionDetailActivity.this)) {
                        answer = edit_answer.getText().toString();
                        new ReplyAnswer().execute(qid, edit_answer.getText().toString());
                    } else {
                        Toast.makeText(QuestionDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.txt_approve:
                if (CommonMethod.isInternetConnected(QuestionDetailActivity.this)) {
                    new ApproveQuestion().execute();
                } else {
                    Toast.makeText(QuestionDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txt_reject:
                if (CommonMethod.isInternetConnected(QuestionDetailActivity.this)) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(QuestionDetailActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Confirm Delete...");

                    // Setting Dialog Message
                    alertDialog.setMessage(" Are you sure you want to delete?.");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_warning);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (CommonMethod.isInternetConnected(QuestionDetailActivity.this)) {
                                new RejectQuestion().execute();
                            } else {
                                Toast.makeText(QuestionDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                            }
                            // Write your code here to invoke YES event
//                            Toast.makeText(activity, "You clicked on YES", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
//                            Toast.makeText(activity, "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    alertDialog.show();

                } else {
                    Toast.makeText(QuestionDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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

    private class ReplyAnswer extends AsyncTask<String, Void, String> {
        String responseJSON = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(QuestionDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("qid", params[0]));
            nameValuePairs.add(new BasicNameValuePair("Answer", params[1]));

            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.questionanswer, nameValuePairs, QuestionDetailActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(QuestionDetailActivity.this, "Answer reply successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(QuestionDetailActivity.this, "Answer not replay.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        }
    }

    private class ApproveQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(QuestionDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.approveques + "?qid=" + qid);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(QuestionDetailActivity.this, "Question approved.", Toast.LENGTH_SHORT).show();
                    lin_replay_answer.setVisibility(View.VISIBLE);
                    lin_approve.setVisibility(View.GONE);
//                    finish();
//                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(QuestionDetailActivity.this, "Question not approved.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        }
    }

    private class RejectQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(QuestionDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.rejectques + "?qid=" + qid);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(QuestionDetailActivity.this, "Question rejected.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(QuestionDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    lin_replay_answer.setVisibility(View.GONE);
                    lin_approve.setVisibility(View.GONE);
//                    finish();
//                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(QuestionDetailActivity.this, "Question not reject.", Toast.LENGTH_SHORT).show();
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
