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
import com.example.grapes_pradip.vimalsagaradmin.adapters.competition.RecyclerAllOptionAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.competition.OptionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class AllQuestionOptionActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView_all_option;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<OptionItem> optionItems = new ArrayList<>();
    private RecyclerAllOptionAdapter recyclerAllOptionAdapter;
    private TextView txt_header;
    private ProgressDialog progressDialog;
    String cid, title;
    private ImageView img_back;
    private ImageView img_nodata;
    private Button button_add;
    private EditText editText_option;
    private String qid;
    private int option = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_view_option_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent intent = getIntent();
        qid = intent.getStringExtra("QID");
        linearLayoutManager = new LinearLayoutManager(AllQuestionOptionActivity.this);
        findID();
        idClick();
        if (CommonMethod.isInternetConnected(AllQuestionOptionActivity.this)) {
            new GetAllOption().execute();
        }
        else {
            Toast.makeText(AllQuestionOptionActivity.this,R.string.internet,Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        recyclerView_all_option = (RecyclerView) findViewById(R.id.recyclerView_all_option);
        recyclerView_all_option.setLayoutManager(linearLayoutManager);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_header.setText("Question Option");
        button_add = (Button) findViewById(R.id.button_add);
        editText_option = (EditText) findViewById(R.id.editText_option);
        img_nodata = (ImageView) findViewById(R.id.img_nodata);

    }

    private void idClick() {
        button_add.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
                if (TextUtils.isEmpty(editText_option.getText().toString())) {
                    editText_option.setError("Please enter option.");
                    editText_option.requestFocus();
                } else {
                    if (CommonMethod.isInternetConnected(AllQuestionOptionActivity.this)) {
                        if (optionItems.size() == option) {
                            Toast.makeText(AllQuestionOptionActivity.this, "Maximum 4 option are added.", Toast.LENGTH_SHORT).show();
                        } else {
                            new AddOption().execute(editText_option.getText().toString());
                        }

                    }
                    else {
                        Toast.makeText(AllQuestionOptionActivity.this,R.string.internet,Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.img_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }

    private class AddOption extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AllQuestionOptionActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
//            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("qid", qid));
            nameValuePairs.add(new BasicNameValuePair("OptionValue", params[0]));
            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.addoption, nameValuePairs, AllQuestionOptionActivity.this);
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
                    editText_option.setText("");
                    Toast.makeText(AllQuestionOptionActivity.this, "Option added successfully." , Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AllQuestionOptionActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    new GetAllOption().execute();
                }
                else {
                    Toast.makeText(AllQuestionOptionActivity.this, "Option not added." , Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    public class GetAllOption extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AllQuestionOptionActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getalloptionbyqid + "?qid=" + qid);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("json array", "-------------------" + jsonArray);
                    optionItems = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        Log.e("id", "---------------" + id);
                        String question = jsonObject1.getString("Question");
                        String qtype = jsonObject1.getString("QType");
                        String categoryid = jsonObject1.getString("CategoryID");
                        String option = jsonObject1.getString("QID");
                        String qid = jsonObject1.getString("QID");
                        String optionValue = jsonObject1.getString("OptionValue");
                        optionItems.add(new OptionItem(categoryid, qtype, optionValue, question, id, qid));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (recyclerView_all_option != null) {
                recyclerAllOptionAdapter = new RecyclerAllOptionAdapter(AllQuestionOptionActivity.this, optionItems);
                if (recyclerAllOptionAdapter.getItemCount() != 0) {
                    recyclerView_all_option.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_all_option.setAdapter(recyclerAllOptionAdapter);
                } else {
                    img_nodata.setVisibility(View.VISIBLE);
                    recyclerView_all_option.setVisibility(View.GONE);
                }
            }

        }
    }

}
