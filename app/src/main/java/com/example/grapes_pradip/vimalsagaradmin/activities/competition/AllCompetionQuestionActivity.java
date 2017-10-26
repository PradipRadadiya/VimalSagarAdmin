package com.example.grapes_pradip.vimalsagaradmin.activities.competition;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.adapters.competition.RecyclerCompetitionQuestionAllAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.competition.CompetitionQuestionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class AllCompetionQuestionActivity extends AppCompatActivity implements View.OnClickListener {
    private SwipeRefreshLayout swipe_refresh_information;
    private RecyclerView recyclerView_all_competition;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<CompetitionQuestionItem> competitionQuestionItems = new ArrayList<>();
    private RecyclerCompetitionQuestionAllAdapter recyclerCompetitionQuestionAllAdapter;
    private TextView txt_addnew;
    private TextView txt_header;
    private ProgressDialog progressDialog;
    private String cid;
    private String title;
    private ImageView img_back;
    private ImageView img_nodata;

    private int page_count = 1;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    ProgressBar progress_load;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.competition_all_activity);
        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        linearLayoutManager = new LinearLayoutManager(AllCompetionQuestionActivity.this);
        findID();
        idClick();

        swipe_refresh_information.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CommonMethod.isInternetConnected(AllCompetionQuestionActivity.this)) {
                    refreshContent();
                }else {
                    swipe_refresh_information.setRefreshing(false);
                }
            }
        });
        recyclerView_all_competition.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                if (flag_scroll) {
                    Log.e("flag-Scroll", flag_scroll + "");
                } else {
                    if (loading) {
                        Log.e("flag-Loading", loading + "");
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                            //Log.e("flag-IF", (totalItemCount > previousTotal) + "");
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        Log.e("flag-Loading_second_if", loading + "");
                        if (CommonMethod.isInternetConnected(AllCompetionQuestionActivity.this)) {

                            Log.e("total count", "--------------------" + page_count);

                            page_count++;
                            new GetAllCompetitionCategory().execute();
                        }  else {
                            Toast.makeText(AllCompetionQuestionActivity.this,R.string.internet,Toast.LENGTH_SHORT).show();
                        }
                        loading = true;
                    }
                }
            }

        });

    }

    private void refreshContent() {

        page_count=1;
        competitionQuestionItems = new ArrayList<>();
        swipe_refresh_information.setRefreshing(false);
        new GetAllCompetitionCategory().execute();
    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        swipe_refresh_information = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recyclerView_all_competition = (RecyclerView) findViewById(R.id.recyclerView_all_competition);
        recyclerView_all_competition.setLayoutManager(linearLayoutManager);
        txt_addnew = (TextView) findViewById(R.id.txt_addnew);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_header.setText("Competition Question");
        img_nodata = (ImageView) findViewById(R.id.img_nodata);
        progress_load = (ProgressBar) findViewById(R.id.progress_load);
    }

    private void idClick() {
        txt_addnew.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_addnew:
                Intent intent = new Intent(AllCompetionQuestionActivity.this, AddComptitionQuestion.class);
                intent.putExtra("cid", cid);
                intent.putExtra("categoryname", title);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.img_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }

    private class GetAllCompetitionCategory extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(AllCompetionQuestionActivity.this);
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            progress_load.setVisibility(View.VISIBLE);
            recyclerView_all_competition.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallquestionsbycidforadmin + "?cid=" + cid + "&page=" + page_count + "&psize=30");
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
                    if (jsonArray.length() < 30 || jsonArray.length() == 0) {
                        flag_scroll = true;
                        Log.e("length_array_news", flag_scroll + "" + "<30===OR(0)===" + jsonArray.length());
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        Log.e("id", "---------------" + id);
                        String question = jsonObject1.getString("Question");

                        String questiontype = jsonObject1.getString("QType");
                        String qtype = null;
                        if (questiontype.equalsIgnoreCase("Radio")){
                            qtype="Option";
                        }else {
                            qtype = jsonObject1.getString("QType");
                        }
                        String categoryid = jsonObject1.getString("CategoryID");
                        String option = jsonObject1.getString("Options");
                        competitionQuestionItems.add(new CompetitionQuestionItem(categoryid, option, qtype, question, id));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
            progress_load.setVisibility(View.GONE);
            recyclerView_all_competition.setVisibility(View.VISIBLE);
            if (recyclerView_all_competition != null) {
                recyclerCompetitionQuestionAllAdapter = new RecyclerCompetitionQuestionAllAdapter(AllCompetionQuestionActivity.this, competitionQuestionItems);
                if (recyclerCompetitionQuestionAllAdapter.getItemCount() != 0) {
                    recyclerView_all_competition.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_all_competition.setAdapter(recyclerCompetitionQuestionAllAdapter);
                } else {
                    img_nodata.setVisibility(View.VISIBLE);
                    recyclerView_all_competition.setVisibility(View.GONE);
                }
            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        competitionQuestionItems = new ArrayList<>();
        if (CommonMethod.isInternetConnected(AllCompetionQuestionActivity.this)) {
            page_count = 1;
            new GetAllCompetitionCategory().execute();
        }
        else {
            Toast.makeText(AllCompetionQuestionActivity.this,R.string.internet,Toast.LENGTH_SHORT).show();
        }
    }


}
