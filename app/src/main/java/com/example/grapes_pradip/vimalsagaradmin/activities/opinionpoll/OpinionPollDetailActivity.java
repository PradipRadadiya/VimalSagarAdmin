package com.example.grapes_pradip.vimalsagaradmin.activities.opinionpoll;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class OpinionPollDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String oid, yespoll, nopoll, question, totalpoll;
    private TextView txt_title;
    private TextView txt_header;
    private ImageView img_back;
    private Button btn_edit;
    private Button btn_delete;
    private ProgressBar progress_yes;
    private ProgressBar progres_no;
    private TextView txtYes;
    private TextView txtNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opinionpoll_detail_activity);
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
        txt_title = (TextView) findViewById(R.id.txt_titles);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        progres_no = (ProgressBar) findViewById(R.id.progres_no);
        progress_yes = (ProgressBar) findViewById(R.id.progress_yes);
        txtYes = (TextView) findViewById(R.id.txtYes);
        txtNo = (TextView) findViewById(R.id.txtNo);
    }

    private void idClick() {
        img_back.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void setContent() {
        txt_title.setText(question);
        txt_header.setText("OpinionPoll Detail");


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
                Intent intent = new Intent(OpinionPollDetailActivity.this, OpinionPollEditActivity.class);
                intent.putExtra("ID", oid);
                intent.putExtra("Ques", question);
                intent.putExtra("total_polls", totalpoll);
                intent.putExtra("yes_polls", yespoll);
                intent.putExtra("no_polls", nopoll);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.btn_delete:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(OpinionPollDetailActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage(" Are you sure you want to delete?.");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_warning);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (CommonMethod.isInternetConnected(OpinionPollDetailActivity.this)) {
                            new DeleteOpinionPoll().execute();
                        } else {
                            Toast.makeText(OpinionPollDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private class DeleteOpinionPoll extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deletepoll + "?qid=" + oid);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(OpinionPollDetailActivity.this, "Opinion poll delete successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(OpinionPollDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(OpinionPollDetailActivity.this, "Opinion poll not delete.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(OpinionPollDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
