package com.example.grapes_pradip.vimalsagaradmin.activities.competition;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.adapters.competition.RecyclerCompetitionQuestionCorrectWrongAnswerAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.competition.CompetitionQuestionCorrectWrongItem;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

public class ParticularUserResultDetail extends AppCompatActivity {


    private ProgressBar progress_load;
    private RecyclerView recycleview_comp_result;
    private String cid;
    private String uid;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerCompetitionQuestionCorrectWrongAnswerAdapter recyclerCompetitionQuestionCorrectWrongAnswerAdapter;
    private final ArrayList<CompetitionQuestionCorrectWrongItem> competitionQuestionCorrectWrongItems=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_user_result_detail);
        toolbarClick();


        linearLayoutManager=new LinearLayoutManager(ParticularUserResultDetail.this);


        cid=getIntent().getStringExtra("competition_id");
        uid=getIntent().getStringExtra("user_id");

        Log.e("cid","-------------------"+cid);
        Log.e("uid","-------------------"+uid);

        progress_load= (ProgressBar) findViewById(R.id.progress_load);
        recycleview_comp_result= (RecyclerView) findViewById(R.id.recycleview_comp_result);
        recycleview_comp_result.setLayoutManager(linearLayoutManager);

        new GetAllCompetitionQuestion().execute();
    }

    @SuppressLint("SetTextI18n")
    private void toolbarClick() {
        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        TextView txt_header = (TextView) findViewById(R.id.txt_header);

        txt_header.setText("Results Detail");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private class GetAllCompetitionQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(AllCompetionQuestionActivity.this);
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            progress_load.setVisibility(View.VISIBLE);
            recycleview_comp_result.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("cid", cid));
            nameValuePairs.add(new BasicNameValuePair("uid", uid));
            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + "competition/getcompetitionresultbyuser", nameValuePairs, ParticularUserResultDetail.this);
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


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String question = jsonObject1.getString("question");
                        String qid = jsonObject1.getString("qid");
                        Log.e("question", "---------------" + question);
                        String true_answer = jsonObject1.getString("true_answer");
                        String competition_id = jsonObject1.getString("competition_id");
                        String Name = jsonObject1.getString("Name");

                        String user_id = jsonObject1.getString("user_id");
                        String answer = jsonObject1.getString("answer");

//                        String qtype = null;
//                        if (questiontype.equalsIgnoreCase("Radio")){
//                            qtype="Option";
//                        }else {
//                            qtype = jsonObject1.getString("QType");
//                        }

                        JSONArray jsonArray1 = jsonObject1.getJSONArray("options");

                        ArrayList<String> optionArrayList = new ArrayList<>();
                        for (int j = 0; j < jsonArray1.length(); j++) {

                            JSONObject object=jsonArray1.getJSONObject(j);

                            optionArrayList.add(object.getString("optionvalue"));
                            Log.e("option", "---------------------" + optionArrayList);
                        }

                        String listString = "";

                        for (String str : optionArrayList) {
                            listString += str + "|";
                        }


                        competitionQuestionCorrectWrongItems.add(new CompetitionQuestionCorrectWrongItem(question,qid,true_answer,competition_id,Name,user_id,answer,listString));


                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
            progress_load.setVisibility(View.GONE);
            recycleview_comp_result.setVisibility(View.VISIBLE);
            if (recycleview_comp_result != null) {
                recyclerCompetitionQuestionCorrectWrongAnswerAdapter = new RecyclerCompetitionQuestionCorrectWrongAnswerAdapter(ParticularUserResultDetail.this, competitionQuestionCorrectWrongItems);
                if (recyclerCompetitionQuestionCorrectWrongAnswerAdapter.getItemCount() != 0) {
                    recycleview_comp_result.setVisibility(View.VISIBLE);
//                    txt_nodata.setVisibility(View.GONE);
                    recycleview_comp_result.setAdapter(recyclerCompetitionQuestionCorrectWrongAnswerAdapter);
                } else {
//                    txt_nodata.setVisibility(View.VISIBLE);
                    recycleview_comp_result.setVisibility(View.GONE);
                }
            }

        }
    }



}
