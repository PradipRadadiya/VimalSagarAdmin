package com.example.grapes_pradip.vimalsagaradmin.activities.jainism;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.adapters.information.RecyclerCommentAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;


public class CommentListJainism extends AppCompatActivity {

    //page count
    private int page_count = 1;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView_comments;
    private EditText edit_comment;
    private ImageView img_send;
    private ImageView img_nodata;
    private ProgressDialog progressDialog;
    private String id;
    private String audioname;
    private String cid;
    private String categoryname;
    private RecyclerCommentAdapter recyclerCommentAdapter;

    private ArrayList<com.example.grapes_pradip.vimalsagaradmin.model.information.CommentList> commentLists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment_list);
        Intent intent = getIntent();
        id = intent.getStringExtra("j_id");
        String action_click = intent.getStringExtra("click_action");

        linearLayoutManager = new LinearLayoutManager(CommentListJainism.this);
        recyclerView_comments = (RecyclerView) findViewById(R.id.recyclerView_comments);
        edit_comment = (EditText) findViewById(R.id.edit_comment);
        img_send = (ImageView) findViewById(R.id.img_send);
        ImageView img_comment_back = (ImageView) findViewById(R.id.img_back);

        img_comment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_comment.getText().toString())) {
                    edit_comment.setError("Please enter comment.");
                    edit_comment.requestFocus();
                } else {
                    if (CommonMethod.isInternetConnected(CommentListJainism.this)) {
                        new AdminComment().execute(edit_comment.getText().toString());
                    } else {
                        Toast.makeText(CommentListJainism.this, R.string.internet, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        recyclerView_comments.setLayoutManager(linearLayoutManager);
        img_nodata = (ImageView) findViewById(R.id.img_nodata);

        /*recyclerView_comments.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        if (CommonMethod.isInternetConnected(CommentListJainism.this)) {

                            Log.e("total count", "--------------------" + page_count);

                            page_count++;
                            new CommentList().execute();
                        } else {
                            Toast.makeText(CommentListJainism.this, R.string.internet, Toast.LENGTH_SHORT).show();
                        }
                        loading = true;

                    }

                }
            }
        });*/


    }

    @SuppressLint("StaticFieldLeak")
    private class CommentList extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommentListJainism.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("jid", id));
            nameValuePairs.add(new BasicNameValuePair("page", "1"));
            nameValuePairs.add(new BasicNameValuePair("psize", "1000"));

            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + "jainaism/getallappcomments",nameValuePairs,CommentListJainism.this);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Log.e("response", "-----------------" + "success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        String informationID = jsonObject1.getString("JainismID");
                        String comment = jsonObject1.getString("Comment");
                        String date = jsonObject1.getString("Date");
                        String userID = jsonObject1.getString("UserID");
                        String is_approved = jsonObject1.getString("Is_Approved");
                        String name = jsonObject1.getString("Name");
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
                        commentLists.add(new com.example.grapes_pradip.vimalsagaradmin.model.information.CommentList(comment, is_approved, userID, informationID, id, fulldate, name));
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            if (recyclerView_comments != null) {
                recyclerCommentAdapter = new RecyclerCommentAdapter(CommentListJainism.this, commentLists);
                Log.e("response", "-----------------" + "object");

                if (recyclerCommentAdapter.getItemCount() != 0) {
                    recyclerView_comments.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_comments.setAdapter(recyclerCommentAdapter);
                    Log.e("response", "-----------------" + "setlayout");

                } else {
                    recyclerView_comments.setVisibility(View.GONE);
                    img_nodata.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AdminComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommentListJainism.this);
            progressDialog.setMessage("Loading..");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("jid", id));
                nameValuePairs.add(new BasicNameValuePair("Comment", params[0]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + "jainaism/admincomment", nameValuePairs, CommentListJainism.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    if (CommonMethod.isInternetConnected(CommentListJainism.this)) {
                        progressDialog.dismiss();
                        commentLists = new ArrayList<>();
                        new CommentList().execute();
                        Toast.makeText(CommentListJainism.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(AudioDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        edit_comment.setText("");
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(CommentListJainism.this, "Comment not added.", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(AudioDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
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
    public void onResume() {
        super.onResume();
        progressDialog = new ProgressDialog(CommentListJainism.this);
        progressDialog.dismiss();
        page_count = 1;
        commentLists = new ArrayList<>();
        if (CommonMethod.isInternetConnected(CommentListJainism.this)) {
            new CommentList().execute();
        } else {
            Toast.makeText(CommentListJainism.this, R.string.internet, Toast.LENGTH_SHORT).show();
        }
    }

//

}