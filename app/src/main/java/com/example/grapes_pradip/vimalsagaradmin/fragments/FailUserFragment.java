package com.example.grapes_pradip.vimalsagaradmin.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.thought.AddThoughtActivity;
import com.example.grapes_pradip.vimalsagaradmin.adapters.competition.RecyclerPassFailUserAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.competition.PassFailtem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import static com.example.grapes_pradip.vimalsagaradmin.adapters.question.RecyclerQuestionAnswerAdapter.questionid;


@SuppressWarnings("ALL")
public class FailUserFragment extends Fragment implements View.OnClickListener {
    private View rootview;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recyclerView_question;
    private LinearLayoutManager linearLayoutManager;
    private List<PassFailtem> responseArrayList = new ArrayList<>();
    private RecyclerPassFailUserAdapter recyclerPassFailUserAdapter;
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
    ProgressBar progress_load;
    String cid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.result_answer_fragment, container, false);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        findID();

        Bundle bundle = this.getArguments();
        cid = bundle.getString("cid");

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CommonMethod.isInternetConnected(getActivity())) {
                    refreshContent();
                } else {
                    swipe_refresh.setRefreshing(false);
                }
            }
        });

        /*recyclerView_question.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                                                  if (CommonMethod.isInternetConnected(getActivity())) {

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

        );*/
        return rootview;
    }

    private void refreshContent() {
        page_count = 1;
        questionid.clear();
        responseArrayList = new ArrayList<>();
        swipe_refresh.setRefreshing(false);
        new GetAllQuestion().execute(cid);
    }

    private void findID() {
        swipe_refresh = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh);
        recyclerView_question = (RecyclerView) rootview.findViewById(R.id.recyclerView_question);
        recyclerView_question.setLayoutManager(linearLayoutManager);
        txt_addnew = (TextView) rootview.findViewById(R.id.txt_addnew);
        img_nodata = (ImageView) rootview.findViewById(R.id.img_nodata);
        progress_load = (ProgressBar) rootview.findViewById(R.id.progress_load);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_addnew:
                Intent intent = new Intent(getActivity(), AddThoughtActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;


            case R.id.delete_data:
                if (questionid.size() > 0) {

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

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
                    Toast.makeText(getActivity(), "Select question for delete.", Toast.LENGTH_SHORT).show();
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
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("cid", params[0]));

            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + "competition/getcompetitionresult", nameValuePairs, getActivity());
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
                        String user_total_mark = jsonObject1.getString("user_total_mark");
                        String competition_id = jsonObject1.getString("competition_id");
                        String total_mark = jsonObject1.getString("total_mark");
                        String taken_time = jsonObject1.getString("taken_time");
                        String Name = jsonObject1.getString("Name");
                        String user_id = jsonObject1.getString("user_id");

                        Log.e("user_total_mark", "---------------" + user_total_mark);

                        responseArrayList.add(new PassFailtem(user_total_mark, competition_id, total_mark,taken_time,Name,user_id));
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
                recyclerPassFailUserAdapter = new RecyclerPassFailUserAdapter(getActivity(), responseArrayList);
                if (recyclerPassFailUserAdapter.getItemCount() != 0) {
                    recyclerView_question.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_question.setAdapter(recyclerPassFailUserAdapter);
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
            progressDialog = new ProgressDialog(getActivity());
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
        if (CommonMethod.isInternetConnected(getActivity())) {
            new GetAllQuestion().execute(cid);
        }
    }
}
