package com.example.grapes_pradip.vimalsagaradmin.activities.competition;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.adapters.competition.RecyclerAllAnswerAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.competition.AnswerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.grapes_pradip.vimalsagaradmin.fcm.MyFirebaseMessagingService.questionid;

/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class AllQuestionAnswerActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView_all_answer;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<AnswerItem> answerItems = new ArrayList<>();
    private RecyclerAllAnswerAdapter recyclerAllAnswerAdapter;
    private TextView txt_header;
    private ProgressDialog progressDialog;
    String cid, title;
    private ImageView img_back;
    private ImageView img_nodata;
    private EditText editText_question;
    private String qid;
    private String question;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_question_answer_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        linearLayoutManager = new LinearLayoutManager(AllQuestionAnswerActivity.this);
        findID();
        idClick();
        /*if (CommonMethod.isInternetConnected(AllQuestionAnswerActivity.this)) {
            new GetAllAnswer().execute();
        }
        else {
            Toast.makeText(AllQuestionAnswerActivity.this,R.string.internet,Toast.LENGTH_SHORT).show();
        }*/

    }


    @SuppressLint("SetTextI18n")
    private void findID() {
        recyclerView_all_answer = (RecyclerView) findViewById(R.id.recyclerView_all_answer);
        recyclerView_all_answer.setLayoutManager(linearLayoutManager);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_header.setText("Question Answer");
        editText_question = (EditText) findViewById(R.id.editText_question);
        editText_question.setText(question);
        img_nodata = (ImageView) findViewById(R.id.img_nodata);
    }

    private void idClick() {
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }

    public class GetAllAnswer extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AllQuestionAnswerActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
//            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallanswersbyqid + "?qid=" + qid);
            return responseJSON;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    editText_question.setText(jsonObject.getString("Question"));
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("json array", "-------------------" + jsonArray);
                    answerItems = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        Log.e("id", "---------------" + id);
                        String qid = jsonObject1.getString("QID");
                        String cid = jsonObject1.getString("CID");
                        String userid = jsonObject1.getString("UserID");
                        String answer = jsonObject1.getString("Answer");
                        String name = jsonObject1.getString("Name");
                        answerItems.add(new AnswerItem(id, qid, cid, userid, answer, name));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (progressDialog != null) {
                progressDialog.dismiss();
//                new GetQuestion().execute();
            }
            if (recyclerView_all_answer != null) {
                recyclerAllAnswerAdapter = new RecyclerAllAnswerAdapter(AllQuestionAnswerActivity.this, answerItems);
                if (recyclerAllAnswerAdapter.getItemCount() != 0) {
                    recyclerView_all_answer.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_all_answer.setAdapter(recyclerAllAnswerAdapter);

                } else {
                    img_nodata.setVisibility(View.VISIBLE);
                    recyclerView_all_answer.setVisibility(View.GONE);
                }
            }

        }
    }

    public class GetQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AllQuestionAnswerActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
//            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse("http://eonion.in/vimalsagarji/competition/getquestionbyid/?qid=" + qid);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (progressDialog != null) {
                progressDialog.dismiss();
            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        Intent intent = getIntent();
        qid = intent.getStringExtra("QID");
        question = intent.getStringExtra("Question");
//        editText_question.setText(question);
        Log.e("qid", "---------------" + qid);
        Log.e("Question", "---------------------" + question);

        qid = questionid;
        Log.e("qid", "---------------" + qid);
        answerItems = new ArrayList<>();
        if (CommonMethod.isInternetConnected(AllQuestionAnswerActivity.this)) {
            new GetAllAnswer().execute();

        } else {
            Toast.makeText(AllQuestionAnswerActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
        }


    }


}
