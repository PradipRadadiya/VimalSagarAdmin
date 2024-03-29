package com.example.grapes_pradip.vimalsagaradmin.activities.competition;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;


@SuppressWarnings("ALL")
public class EditComptitionQuestionActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private EditText e_title;
    private TextView txt_header;
    private ImageView img_back;
    private Button btn_add;
    private Spinner spiner_qtype, spinner_option, spinner_q_answer;
    //    private String cid;
    private Switch notificationswitch;
    String notify = "0";
    private int numberOfLines = 0;
    List<EditText> allEds = new ArrayList<EditText>();
    String[] strings;
    TextView txt_sel_answer;
    String qid, title;
    String[] optarr;
    String[] arr = {"A", "B", "C", "D"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_comptition_question);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        findID();
        Intent intent = getIntent();
        qid = intent.getStringExtra("qid");
        String Question = intent.getStringExtra("Question");
        String Answer = intent.getStringExtra("Answer");
        String Option = intent.getStringExtra("Option");
        final String QType = intent.getStringExtra("QType");
        optarr = Option.split("\\|");


        e_title.setText(CommonMethod.decodeEmoji(Question));

        List<String> qtype = new ArrayList<>();
        qtype.add("Option");
        qtype.add("yes_no");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, qtype);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_qtype.setAdapter(dataAdapter);


        final List<String> howmanyquestion = new ArrayList<>();

//        howmanyquestion.add("2");
//        qtype.add("Radio");

        howmanyquestion.add("Select Option");
        howmanyquestion.add("4");


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, howmanyquestion);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_option.setAdapter(dataAdapter1);

        if (QType.equalsIgnoreCase("Option")){
            spinner_option.setSelection(1);
        }else{
            spiner_qtype.setSelection(1);
        }

        spiner_qtype.setEnabled(false);
        spiner_qtype.setClickable(false);

        spinner_option.setVisibility(View.GONE);
        spinner_q_answer.setVisibility(View.GONE);

        txt_sel_answer = (TextView) findViewById(R.id.txt_sel_answer);

        defaultDelectedAnswer();


        spiner_qtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = spiner_qtype.getSelectedItem().toString();
                Log.e("item", "----------------" + item);
                if (item.equalsIgnoreCase("Option")) {
//                    strings = null;
                    removeLine();
                    spinner_option.setVisibility(View.VISIBLE);
                    spinner_q_answer.setVisibility(View.GONE);
                    txt_sel_answer.setVisibility(View.VISIBLE);

                } else {
//                    strings = null;
                    removeLine();
                    spinner_option.setVisibility(View.GONE);
                    spinner_option.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        spinner_option.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = spinner_option.getSelectedItem().toString();
                if (item.equalsIgnoreCase("2")) {
                    spinner_q_answer.setVisibility(View.GONE);
                    txt_sel_answer.setVisibility(View.VISIBLE);
                    removeLine();
                    allEds.clear();
                    Add_Line(2);
                } else if (item.equalsIgnoreCase("4")) {

                    spinner_q_answer.setVisibility(View.GONE);
                    txt_sel_answer.setVisibility(View.VISIBLE);
                    removeLine();
                    allEds.clear();
                    Add_Line(4);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        btn_add.setOnClickListener(this);

        txt_sel_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("size", "------------------" + allEds.size());

//                strings = null;

                if (spiner_qtype.getSelectedItem().toString().equalsIgnoreCase("yes_no")) {

                    strings = new String[2];
                    strings[0] = arr[0] + ") " + "YES";
                    strings[1] = arr[1] + ") " + "NO";

                    spinner_q_answer.setVisibility(View.VISIBLE);

                    final List<String> answerlist = new ArrayList<>();
                    for (int i = 0; i < strings.length; i++) {
                        answerlist.add(strings[i]);
                    }
                    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(EditComptitionQuestionActivity.this,
                            android.R.layout.simple_spinner_item, answerlist);
                    dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_q_answer.setAdapter(dataAdapter1);
                    txt_sel_answer.setVisibility(View.GONE);


                } else {

                    strings = new String[allEds.size()];

                    for (int i = 0; i < allEds.size(); i++) {
                        strings[i] = allEds.get(i).getText().toString();
//                        strings[i] = arr[i] + ") " +allEds.get(i).getText().toString();
                        Log.e("edittext value", "-------------" + strings[i]);
                    }

                    spinner_q_answer.setVisibility(View.VISIBLE);

                    final List<String> answerlist = new ArrayList<>();
                    for (int i = 0; i < strings.length; i++) {
                        answerlist.add(strings[i]);
                    }
                    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(EditComptitionQuestionActivity.this,
                            android.R.layout.simple_spinner_item, answerlist);
                    dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_q_answer.setAdapter(dataAdapter1);
                    txt_sel_answer.setVisibility(View.GONE);

                }

            }
        });
    }

    private void defaultDelectedAnswer() {

//        strings = null;

        if (spiner_qtype.getSelectedItem().toString().equalsIgnoreCase("yes_no")) {

            strings = new String[2];
            strings[0] = arr[0] + ") " + "YES";
            strings[1] = arr[1] + ") " + "NO";

            spinner_q_answer.setVisibility(View.VISIBLE);

            final List<String> answerlist = new ArrayList<>();
            for (int i = 0; i < strings.length; i++) {
                answerlist.add(strings[i]);
            }
            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(EditComptitionQuestionActivity.this,
                    android.R.layout.simple_spinner_item, answerlist);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_q_answer.setAdapter(dataAdapter1);
            txt_sel_answer.setVisibility(View.GONE);


        } else {

            strings = new String[allEds.size()];

            for (int i = 0; i < allEds.size(); i++) {
                strings[i] = allEds.get(i).getText().toString();
//                strings[i] = arr[i] + ") " +allEds.get(i).getText().toString();
                Log.e("edittext value", "-------------" + strings[i]);
            }

            spinner_q_answer.setVisibility(View.VISIBLE);

            final List<String> answerlist = new ArrayList<>();
            for (int i = 0; i < strings.length; i++) {
                answerlist.add(strings[i]);
            }
            ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(EditComptitionQuestionActivity.this,
                    android.R.layout.simple_spinner_item, answerlist);
            dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_q_answer.setAdapter(dataAdapter1);
            txt_sel_answer.setVisibility(View.GONE);
        }

    }


    public void Add_Line(int length) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.lin_option);
        // add edittext
        for (int i = 0; i < length; i++) {

            if (optarr.length == 2) {

                EditText et = new EditText(this);
                et.setBackground(getResources().getDrawable(R.drawable.edittext_round));
                et.setHint("\tEnter Option");

                et.setGravity(Gravity.CENTER);
//            et.setPadding(10,10,10,10);

                allEds.add(et);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                p.setMargins(0, 10, 0, 0);

                et.setLayoutParams(p);
//            et.setText("");
                et.setId(numberOfLines + 1);
                ll.addView(et);

                numberOfLines++;


            } else {
                EditText et = new EditText(this);
                et.setBackground(getResources().getDrawable(R.drawable.edittext_round));
                et.setHint("\tEnter Option");

                et.setText(optarr[i]);
                Log.e("settext", "------------" + optarr[i]);
                et.setGravity(Gravity.CENTER);


//            et.setPadding(10,10,10,10);

                allEds.add(et);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                p.setMargins(0, 10, 0, 0);

                et.setLayoutParams(p);
//            et.setText("");
                et.setId(numberOfLines + 1);
                ll.addView(et);

                numberOfLines++;
            }


        }
    }

    public void Add_Line1(int length) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.lin_option);
        // add edittext
        for (int i = 0; i < length; i++) {
            EditText et = new EditText(this);
            et.setBackground(getResources().getDrawable(R.drawable.edittext_round));
//            et.setHint("\tEnter Option");
            et.setText(optarr[i]);
            Log.e("settext", "------------" + optarr[i]);
            et.setGravity(Gravity.CENTER);
//            et.setPadding(10,10,10,10);

            allEds.add(et);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins(0, 10, 0, 0);

            et.setLayoutParams(p);
//            et.setText("");
            et.setId(numberOfLines + 1);
            ll.addView(et);
            numberOfLines++;

        }
    }


    public void removeLine() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.lin_option);
        ll.removeAllViews();

    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        e_title = (EditText) findViewById(R.id.e_title);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_add = (Button) findViewById(R.id.btn_add);
        spiner_qtype = (Spinner) findViewById(R.id.spinner_qtype);
        spinner_option = (Spinner) findViewById(R.id.spinner_option);
        spinner_q_answer = (Spinner) findViewById(R.id.spinner_q_answer);

        txt_header.setText("Edit Competition Question");

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
                String title = CommonMethod.encodeEmoji(e_title.getText().toString());
                String questiontype = CommonMethod.encodeEmoji(spiner_qtype.getSelectedItem().toString());

                String answer = CommonMethod.encodeEmoji(spinner_q_answer.getSelectedItem().toString());


                if (TextUtils.isEmpty(e_title.getText().toString())) {
                    e_title.setError(getResources().getString(R.string.questionerror));
                    e_title.requestFocus();
                } else if (spinner_q_answer != null && spinner_q_answer.getSelectedItem() != null) {
                    String name = (String) spinner_q_answer.getSelectedItem();
                    Log.e("name", "----------" + name);
                    new EditQuestion().execute(qid, CommonMethod.encodeEmoji(e_title.getText().toString()), CommonMethod.encodeEmoji(spinner_q_answer.getSelectedItem().toString()), CommonMethod.encodeEmoji(spiner_qtype.getSelectedItem().toString()));
                } else {

                    Toast.makeText(this, "Please select correct answer.", Toast.LENGTH_LONG).show();

                    if (CommonMethod.isInternetConnected(EditComptitionQuestionActivity.this)) {

                        Log.e("data", "-------" + qid + e_title.getText().toString() + spiner_qtype.getSelectedItem().toString());

//                        new EditQuestion().execute(qid, CommonMethod.encodeEmoji(e_title.getText().toString()), CommonMethod.encodeEmoji(spinner_q_answer.getSelectedItem().toString()), CommonMethod.encodeEmoji(spiner_qtype.getSelectedItem().toString()));

                    } else {
                        Toast.makeText(EditComptitionQuestionActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }

    private class EditQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditComptitionQuestionActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("qid", params[0]));
            nameValuePairs.add(new BasicNameValuePair("Question", params[1]));
            nameValuePairs.add(new BasicNameValuePair("Answer", params[2]));
            nameValuePairs.add(new BasicNameValuePair("QType", params[3]));


            Log.e("array size", "---------------------" + strings.length);


            for (int i = 0; i < strings.length; i++) {
                Log.e("array value", "---------------------" + strings[i]);

                nameValuePairs.add(new BasicNameValuePair("Options[" + String.valueOf(i) + "]", strings[i]));
            }

            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + "competition/updatequestion", nameValuePairs, EditComptitionQuestionActivity.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(EditComptitionQuestionActivity.this, "Question added successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddComptitionQuestion.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    e_title.setText("");
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(EditComptitionQuestionActivity.this, "Question not added.", Toast.LENGTH_SHORT).show();
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
