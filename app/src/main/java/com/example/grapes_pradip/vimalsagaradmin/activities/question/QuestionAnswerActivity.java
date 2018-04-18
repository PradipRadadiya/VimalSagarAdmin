package com.example.grapes_pradip.vimalsagaradmin.activities.question;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.example.grapes_pradip.vimalsagaradmin.activities.thought.AddThoughtActivity;
import com.example.grapes_pradip.vimalsagaradmin.adapters.question.RecyclerQuestionAnswerAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.question.QuestiinItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.grapes_pradip.vimalsagaradmin.adapters.question.RecyclerQuestionAnswerAdapter.questionid;

/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class QuestionAnswerActivity extends AppCompatActivity implements View.OnClickListener {
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recyclerView_question;
    private LinearLayoutManager linearLayoutManager;
    private List<QuestiinItem> responseArrayList = new ArrayList<>();
    private RecyclerQuestionAnswerAdapter recyclerQuestionAnswerAdapter;
    private TextView txt_addnew;
    //page count
    private int page_count = 1;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private ProgressDialog progressDialog;
    private ImageView img_nodata;
    private ImageView delete_data;
    ProgressBar progress_load;

    ImageView img_back;
    TextView txt_header;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_answer_activity);

        linearLayoutManager = new LinearLayoutManager(QuestionAnswerActivity.this);
        findID();

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CommonMethod.isInternetConnected(QuestionAnswerActivity.this)) {
                    refreshContent();
                } else {
                    swipe_refresh.setRefreshing(false);
                }
            }
        });

        recyclerView_question.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                                                  if (CommonMethod.isInternetConnected(QuestionAnswerActivity.this)) {

                                                                      Log.e("total count", "--------------------" + page_count);

                                                                      page_count++;
                                                                      new GetAllQuestion().execute();
                                                                  } else {
                                                                      //internet not connected
                                                                  }
                                                                  loading = true;

                                                              }
                                                          }
                                                      }

                                                  }

        );

    }

    private void refreshContent() {
        page_count = 1;
        questionid.clear();
        responseArrayList = new ArrayList<>();
        swipe_refresh.setRefreshing(false);
        new GetAllQuestion().execute();
    }

    private void findID() {
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_header = (TextView) findViewById(R.id.txt_header);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        recyclerView_question = (RecyclerView) findViewById(R.id.recyclerView_question);
        recyclerView_question.setLayoutManager(linearLayoutManager);
        txt_addnew = (TextView) findViewById(R.id.txt_addnew);
        img_nodata = (ImageView) findViewById(R.id.img_nodata);
        delete_data = (ImageView) findViewById(R.id.delete_data);
        progress_load = (ProgressBar) findViewById(R.id.progress_load);
        delete_data.setOnClickListener(this);
        txt_header.setText("Question & Anwer");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_addnew:
                Intent intent = new Intent(QuestionAnswerActivity.this, AddThoughtActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.delete_data:
                if (questionid.size() > 0) {

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(QuestionAnswerActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Confirm Delete...");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want to delete?.");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_warning);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            // Write your code here to invoke YES event
                            Log.e("array delete", "-------------------" + questionid);
                            new DeleteQuestion().execute();

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
                    Toast.makeText(QuestionAnswerActivity.this, "Select question for delete.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class GetAllQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
            progress_load.setVisibility(View.VISIBLE);
            recyclerView_question.setVisibility(View.GONE);
//            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.viewallappques + "?page=" + page_count + "&psize=30");
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
                        String answer = jsonObject1.getString("Answer");
                        String date = jsonObject1.getString("Date");
                        String is_Approved = jsonObject1.getString("Is_Approved");
                        String uid = jsonObject1.getString("UserID");
                        String name = jsonObject1.getString("Name");
                        String view = jsonObject1.getString("View");
                        String[] string = date.split(" ");
                        Log.e("str1", "--------" + string[0]);
                        Log.e("str2", "--------" + string[1]);

                        Date dt = CommonMethod.convert_date(date);
                        Log.e("Convert date is", "------------------" + dt);
                        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", dt);//Thursday
                        String stringMonth = (String) android.text.format.DateFormat.format("MMM", dt); //Jun
                        String intMonth = (String) android.text.format.DateFormat.format("MM", dt); //06
                        String year = (String) android.text.format.DateFormat.format("yyyy", dt); //2013
                        String day = (String) android.text.format.DateFormat.format("dd", dt); //20

                        Log.e("dayOfTheWeek", "-----------------" + dayOfTheWeek);
                        Log.e("stringMonth", "-----------------" + stringMonth);
                        Log.e("intMonth", "-----------------" + intMonth);
                        Log.e("year", "-----------------" + year);
                        Log.e("day", "-----------------" + day);

                        String fulldate = dayOfTheWeek + ", " + day + "/" + intMonth + "/" + year + ", " + string[1];
                        responseArrayList.add(new QuestiinItem(answer, is_Approved, uid, question, id, fulldate, name, view,false));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }

            progress_load.setVisibility(View.GONE);
            recyclerView_question.setVisibility(View.VISIBLE);
            if (recyclerView_question != null) {
                recyclerQuestionAnswerAdapter = new RecyclerQuestionAnswerAdapter(QuestionAnswerActivity.this, responseArrayList);
                if (recyclerQuestionAnswerAdapter.getItemCount() != 0) {
                    recyclerView_question.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_question.setAdapter(recyclerQuestionAnswerAdapter);
                } else {
                    recyclerView_question.setVisibility(View.GONE);
                    img_nodata.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    private class DeleteQuestion extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(QuestionAnswerActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < questionid.size(); i++) {
                String value = questionid.get(i);
                responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deletequestionbyadmin + "?qid=" + value);
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            if (progressDialog != null) {
                progressDialog.dismiss();
                questionid.clear();
                onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        questionid.clear();
        responseArrayList = new ArrayList<>();
        page_count = 1;
        if (CommonMethod.isInternetConnected(QuestionAnswerActivity.this)) {
            new GetAllQuestion().execute();
        }
    }
}
