package com.example.grapes_pradip.vimalsagaradmin.activities.competition;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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


@SuppressWarnings("ALL")
public class AddComptitionQuestion extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private EditText e_title;
    private TextView txt_header;
    private ImageView img_back;
    private Button btn_add;
    private Spinner spiner_qtype, spinner_option, spinner_q_answer;
    private String cid;
    private Switch notificationswitch;
    String notify = "0";
    private int numberOfLines = 0;
    List<EditText> allEds = new ArrayList<EditText>();
    String[] strings;
    TextView txt_sel_answer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_comptition_question);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        cid = getIntent().getStringExtra("cid");
        findID();

        txt_sel_answer.setVisibility(View.GONE);
        List<String> qtype = new ArrayList<>();
        qtype.add("--Select Question Type--");
        qtype.add("Option");
        qtype.add("yes_no");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, qtype);
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


        spinner_option.setVisibility(View.GONE);
        spinner_q_answer.setVisibility(View.GONE);

        spiner_qtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = spiner_qtype.getSelectedItem().toString();
                Log.e("item", "----------------" + item);
                if (item.equalsIgnoreCase("Option")) {
                    spinner_option.setVisibility(View.VISIBLE);
                    txt_sel_answer.setVisibility(View.GONE);
                } else if(item.equalsIgnoreCase("yes_no")){
                    spinner_option.setVisibility(View.GONE);
                    txt_sel_answer.setVisibility(View.VISIBLE);
                }else {
                    removeLine();
                    spinner_option.setVisibility(View.GONE);
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
                String[] arr = {"A", "B", "C", "D"};

                if (spiner_qtype.getSelectedItem().toString().equalsIgnoreCase("yes_no")) {
                    strings = new String[2];
                    strings[0] = arr[0] + ") " + "YES";
                    strings[1] = arr[1] + ") " + "NO";

                    spinner_q_answer.setVisibility(View.VISIBLE);

                    final List<String> answerlist = new ArrayList<>();
                    for (int i = 0; i < strings.length; i++) {
                        answerlist.add(strings[i]);
                    }

                    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(AddComptitionQuestion.this,
                            android.R.layout.simple_spinner_item, answerlist);
                    dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_q_answer.setAdapter(dataAdapter1);
                    txt_sel_answer.setVisibility(View.GONE);


                } else {

                    strings = new String[allEds.size()];

                    for (int i = 0; i < allEds.size(); i++) {
                        strings[i] = arr[i]+") "+allEds.get(i).getText().toString();
                        Log.e("edittext value", "-------------" + strings[i]);
                    }

                    spinner_q_answer.setVisibility(View.VISIBLE);

                    final List<String> answerlist = new ArrayList<>();
                    for (int i = 0; i < strings.length; i++) {
                        answerlist.add(strings[i]);
                    }
                    ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(AddComptitionQuestion.this,
                            android.R.layout.simple_spinner_item, answerlist);
                    dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_q_answer.setAdapter(dataAdapter1);
                    txt_sel_answer.setVisibility(View.GONE);
                }

            }
        });
    }


    public void Add_Line(int length) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.lin_option);
        // add edittext

        for (int i = 0; i < length; i++) {
            EditText et = new EditText(this);
            et.setBackground(getResources().getDrawable(R.drawable.edittext_round));
            et.setHint("\tEnter Option");
            et.setText("\t");
            et.setGravity(Gravity.CENTER);
//            et.setPadding(10,10,10,10);

            allEds.add(et);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.setMargins(0, 10, 0, 0);

            et.setLayoutParams(p);
            et.setText("");
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
        txt_sel_answer = (TextView) findViewById(R.id.txt_sel_answer);
        e_title = (EditText) findViewById(R.id.e_title);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_add = (Button) findViewById(R.id.btn_add);
        spiner_qtype = (Spinner) findViewById(R.id.spinner_qtype);
        spinner_option = (Spinner) findViewById(R.id.spinner_option);
        spinner_q_answer = (Spinner) findViewById(R.id.spinner_q_answer);

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
                String title = CommonMethod.encodeEmoji(e_title.getText().toString());
                String questiontype = CommonMethod.encodeEmoji(spiner_qtype.getSelectedItem().toString());


//                Log.e("spin answer","----------------"+spinner_q_answer.getSelectedItem().toString());

                spinner_q_answer.setVisibility(View.VISIBLE);
//                if (txt_sel_answer.setVisibility(View.VISIBLE)){
//
//                }

//                if(spinner_q_answer != null && spinner_q_answer.getSelectedItem() !=null ) {
//                    String name = (String)spinner_q_answer.getSelectedItem();
//                    Log.e("name","----------"+name);
//                } else  {
//                    Toast.makeText(this, "Please select answer.", Toast.LENGTH_SHORT).show();
//                }

                if (TextUtils.isEmpty(e_title.getText().toString())) {
                    e_title.setError(getResources().getString(R.string.questionerror));
                    e_title.requestFocus();
                } else if (spinner_q_answer != null && spinner_q_answer.getSelectedItem() != null) {
                    String name = (String) spinner_q_answer.getSelectedItem();
                    Log.e("name", "----------" + name);
                    new AddQuestion().execute(cid, CommonMethod.encodeEmoji(e_title.getText().toString()), CommonMethod.encodeEmoji(spinner_q_answer.getSelectedItem().toString()), CommonMethod.encodeEmoji(spiner_qtype.getSelectedItem().toString()), notify);
                } else {
                    Toast.makeText(this, "Please select correct answer.", Toast.LENGTH_LONG).show();
                    if (CommonMethod.isInternetConnected(AddComptitionQuestion.this)) {
//                        String answer = CommonMethod.encodeEmoji(spinner_q_answer.getSelectedItem().toString());
                        Log.e("data", "-------" + cid + e_title.getText().toString() + spiner_qtype.getSelectedItem().toString());

//                        new AddQuestion().execute(cid, CommonMethod.encodeEmoji(e_title.getText().toString()), CommonMethod.encodeEmoji(spinner_q_answer.getSelectedItem().toString()), CommonMethod.encodeEmoji(spiner_qtype.getSelectedItem().toString()), notify);


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
            nameValuePairs.add(new BasicNameValuePair("Answer", params[2]));
            nameValuePairs.add(new BasicNameValuePair("QType", params[3]));
            nameValuePairs.add(new BasicNameValuePair("Is_notify", params[4]));

            for (int i = 0; i < strings.length; i++) {
                nameValuePairs.add(new BasicNameValuePair("Options[" + String.valueOf(i) + "]", CommonMethod.encodeEmoji(strings[i])));
            }

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
