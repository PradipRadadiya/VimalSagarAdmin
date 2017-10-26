package com.example.grapes_pradip.vimalsagaradmin.activities.competition;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

/**
 * Created by Grapes-Pradip on 2/16/2017.
 */

@SuppressWarnings("ALL")
public class AddComptitionQuestion extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private EditText e_title;
    private TextView txt_header;
    private ImageView img_back;
    private Button btn_add;
    private Spinner spiner_qtype;
    private String cid;
    private Switch notificationswitch;
    String notify = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_comptition_question);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        cid = getIntent().getStringExtra("cid");
        findID();
        List<String> qtype = new ArrayList<>();
        qtype.add("Normal");
//        qtype.add("Radio");
        qtype.add("Option");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, qtype);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_qtype.setAdapter(dataAdapter);
        btn_add.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        e_title = (EditText) findViewById(R.id.e_title);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_add = (Button) findViewById(R.id.btn_add);
        spiner_qtype = (Spinner) findViewById(R.id.spinner_qtype);
        txt_header.setText("Add Competition Question");

        notificationswitch = (Switch) findViewById(R.id.notificationswitch);
        notificationswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e("checked", "----------------" + isChecked);
                    notify = "0";
                } else {
                    Log.e("checked", "----------------" + isChecked);
                    notify = "1";

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_add:
                if (TextUtils.isEmpty(e_title.getText().toString())) {
                    e_title.setError(getResources().getString(R.string.questionerror));
                    e_title.requestFocus();
                } else {
                    if (CommonMethod.isInternetConnected(AddComptitionQuestion.this)) {
                        if (spiner_qtype.getSelectedItem().toString().equalsIgnoreCase("Option")) {
                            String Option = "Radio";
                            new AddQuestion().execute(cid, e_title.getText().toString(), Option, notify);
                        } else {
                            Log.e("data", "-------" + cid + e_title.getText().toString() + spiner_qtype.getSelectedItem().toString());
                            new AddQuestion().execute(cid, e_title.getText().toString(), spiner_qtype.getSelectedItem().toString(), notify);
                        }

                    } else {
                        Toast.makeText(AddComptitionQuestion.this, R.string.internet, Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }

    private class AddQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddComptitionQuestion.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("cid", params[0]));
            nameValuePairs.add(new BasicNameValuePair("Question", params[1]));
            nameValuePairs.add(new BasicNameValuePair("QType", params[2]));
            nameValuePairs.add(new BasicNameValuePair("Is_notify", params[3]));
            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.addquestion, nameValuePairs, AddComptitionQuestion.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(AddComptitionQuestion.this, "Question added successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddComptitionQuestion.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    e_title.setText("");
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(AddComptitionQuestion.this, "Question not added.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddComptitionQuestion.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
