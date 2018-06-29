package com.example.grapes_pradip.vimalsagaradmin.activities.information;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.adapters.information.RecyclerCommentAdapter;
import com.example.grapes_pradip.vimalsagaradmin.adapters.information.RecyclerLikeAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.information.LikeList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;


@SuppressWarnings("ALL")
public class InfomationDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String id;
    private String click_action;
    private String title;
    private String description;
    private String address;
    private String date;
    private String view;
    private String photos;
    private String isodate;

    private TextView txt_title;
    private TextView txt_date;
    private TextView txt_description;
    private TextView txt_address;
    private TextView detaillike;
    private TextView txt_views;
    private ImageView img_back;
    private Button btn_edit;
    private Button btn_delete;
    private Button infolike;
    private Dialog dialog;
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
    private RecyclerLikeAdapter recyclerLikeAdapter;
    private RecyclerCommentAdapter recyclerCommentAdapter;

    private ArrayList<LikeList> likeLists = new ArrayList<>();
    private ArrayList<com.example.grapes_pradip.vimalsagaradmin.model.information.CommentList> commentLists = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView_likes;
    private RecyclerView recyclerView_comments;
    private Button infocomment;
    private EditText edit_comment;
    private ImageView img_send;
    private TextView detailcomment;
    private ImageView img_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_detail_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        id = intent.getStringExtra("info_id");
        click_action = intent.getStringExtra("click_action");
        Log.e("click_action", "------------" + click_action);
//        title = intent.getStringExtra("title");
//        description = intent.getStringExtra("description");
//        address = intent.getStringExtra("address");
//        date = intent.getStringExtra("date");
//        view = intent.getStringExtra("view");

        findID();
        idClick();

//        setContent();
    }

    private void findID() {
        txt_title = (TextView) findViewById(R.id.txt_titles);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_description = (TextView) findViewById(R.id.txt_description);
        txt_address = (TextView) findViewById(R.id.txt_address);
        detaillike = (TextView) findViewById(R.id.detaillike);
        txt_views = (TextView) findViewById(R.id.txt_views);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        infolike = (Button) findViewById(R.id.infolike);
        infocomment = (Button) findViewById(R.id.infocomment);
        detailcomment = (TextView) findViewById(R.id.detailcomment);
        img_info=(ImageView) findViewById(R.id.img_info);
    }

    private void idClick() {
        img_back.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        infolike.setOnClickListener(this);
        infocomment.setOnClickListener(this);
        img_info.setOnClickListener(this);
    }

    private void setContent() {

        if (CommonMethod.isInternetConnected(InfomationDetailActivity.this)) {
            new GetAllInfo().execute();
        } else {
            Toast.makeText(InfomationDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.img_info:
                Log.e("image","-------------");
                break;
            case R.id.btn_edit:
                Intent intent = new Intent(InfomationDetailActivity.this, EditInformationActivity.class);
                intent.putExtra("info_id", id);
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("address", address);
                intent.putExtra("date", date);
                intent.putExtra("photo", photos);
                intent.putExtra("isodate",isodate);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.btn_delete:
                if (CommonMethod.isInternetConnected(InfomationDetailActivity.this)) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(InfomationDetailActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Confirm Delete...");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want to delete?.");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_warning);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            new DeleteInformation().execute();
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

                } else {
                    Toast.makeText(InfomationDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.infolike:
                likeLists = new ArrayList<>();
                page_count = 1;
                dialog = new Dialog(InfomationDetailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_like_list);
                dialog.show();

                ImageView img_back = (ImageView) dialog.findViewById(R.id.img_back);

                img_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                linearLayoutManager = new LinearLayoutManager(InfomationDetailActivity.this);
                recyclerView_likes = (RecyclerView) dialog.findViewById(R.id.recyclerView_likes);
                recyclerView_likes.setLayoutManager(linearLayoutManager);
                img_nodata = (ImageView) dialog.findViewById(R.id.img_nodata);

                if (CommonMethod.isInternetConnected(InfomationDetailActivity.this)) {
                    new LikeUserList().execute();
                } else {
                    Toast.makeText(InfomationDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                }
                recyclerView_likes.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                if (CommonMethod.isInternetConnected(InfomationDetailActivity.this)) {

                                    Log.e("total count", "--------------------" + page_count);

                                    page_count++;
                                    new LikeUserList().execute();
                                } else {
                                    Toast.makeText(InfomationDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                                }
                                loading = true;

                            }
                        }
                    }
                });


                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                break;

            case R.id.infocomment:
                commentLists = new ArrayList<>();
                page_count = 1;
                dialog = new Dialog(InfomationDetailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_comment_list);
                dialog.show();

                ImageView img_comment_back = (ImageView) dialog.findViewById(R.id.img_back);

                img_comment_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                linearLayoutManager = new LinearLayoutManager(InfomationDetailActivity.this);
                recyclerView_comments = (RecyclerView) dialog.findViewById(R.id.recyclerView_comments);
                edit_comment = (EditText) dialog.findViewById(R.id.edit_comment);
                img_send = (ImageView) dialog.findViewById(R.id.img_send);
                img_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(edit_comment.getText().toString())) {
                            edit_comment.setError("Please enter comment.");
                            edit_comment.requestFocus();
                        } else {

                            if (CommonMethod.isInternetConnected(InfomationDetailActivity.this)) {
                                new AdminComment().execute(edit_comment.getText().toString());
                            } else {
                                Toast.makeText(InfomationDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                recyclerView_comments.setLayoutManager(linearLayoutManager);
                img_nodata = (ImageView) dialog.findViewById(R.id.img_nodata);

                if (CommonMethod.isInternetConnected(InfomationDetailActivity.this)) {
                    new CommentList().execute();
                } else {
                    Toast.makeText(InfomationDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                }
                recyclerView_comments.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                if (CommonMethod.isInternetConnected(InfomationDetailActivity.this)) {

                                    Log.e("total count", "--------------------" + page_count);

                                    page_count++;
                                    new CommentList().execute();
                                } else {
                                    Toast.makeText(InfomationDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                                }
                                loading = true;

                            }
                        }
                    }
                });

                WindowManager.LayoutParams attrs = getWindow().getAttributes();
                attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                getWindow().setAttributes(attrs);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private class DeleteInformation extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deleteInformation + "?infoid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }


    private class LikeUserList extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(InfomationDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallusernamesforlike + "?infoid=" + id + "&page=" + page_count + "&psize=1000");
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
                    if (jsonArray.length() < 30 || jsonArray.length() == 0) {
                        flag_scroll = true;
                        Log.e("length_array_news", flag_scroll + "" + "<30===OR(0)===" + jsonArray.length());
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        String informationID = jsonObject1.getString("InformationID");
                        String userID = jsonObject1.getString("UserID");
                        String name = jsonObject1.getString("Name");
                        likeLists.add(new LikeList(id, informationID, userID, name));
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            if (recyclerView_likes != null) {
                recyclerLikeAdapter = new RecyclerLikeAdapter(InfomationDetailActivity.this, likeLists);
                Log.e("response", "-----------------" + "object");
                if (recyclerLikeAdapter.getItemCount() != 0) {
                    recyclerView_likes.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_likes.setAdapter(recyclerLikeAdapter);
                    Log.e("response", "-----------------" + "setlayout");
                } else {
                    recyclerView_likes.setVisibility(View.GONE);
                    img_nodata.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private class CommentList extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(InfomationDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallcomments + "?infoid=" + id + "&page=" + page_count + "&psize=1000");
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
                    if (jsonArray.length() < 30 || jsonArray.length() == 0) {
                        flag_scroll = true;
                        Log.e("length_array_news", flag_scroll + "" + "<30===OR(0)===" + jsonArray.length());
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        String informationID = jsonObject1.getString("InformationID");
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
                recyclerCommentAdapter = new RecyclerCommentAdapter(InfomationDetailActivity.this, commentLists);
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

    private class AdminComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(InfomationDetailActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("infoid", id));
                nameValuePairs.add(new BasicNameValuePair("Comment", params[0]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.admincomment, nameValuePairs, InfomationDetailActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response","------------------"+s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    if (CommonMethod.isInternetConnected(InfomationDetailActivity.this)) {
                        progressDialog.dismiss();
                        commentLists = new ArrayList<>();
                        new CommentList().execute();
                        Toast.makeText(InfomationDetailActivity.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
                        edit_comment.setText("");
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(InfomationDetailActivity.this, "Comment not added.", Toast.LENGTH_SHORT).show();
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


    private class GetAllInfo extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(InfomationDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getinfobyidforadmin + "?infoid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String commentcount = jsonObject.getString("commentcount");
                Log.e("commentcount", "-----------------" + commentcount);
                detailcomment.setText(commentcount);

                String likecount = jsonObject.getString("likecount");
                Log.e("like", "-----------------" + likecount);
                detaillike.setText(likecount);

                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progressDialog.dismiss();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        title = object.getString("Title");
                        description = object.getString("Description");
                        address = object.getString("Address");
                        String dates = object.getString("Date");
                        view = object.getString("View");
                        photos = object.getString("Photo");
                        isodate=dates;

                        String[] string = dates.split(" ");
                        Log.e("str1", "--------" + string[0]);
                        Log.e("str2", "--------" + string[1]);

                        Date dt = CommonMethod.convert_date(dates);
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
                        date = dayOfTheWeek + ", " + day + "/" + intMonth + "/" + year + ", " + string[1];

                        txt_title.setText(CommonMethod.decodeEmoji(title));
                        txt_date.setText(CommonMethod.decodeEmoji(date));
                        txt_description.setText(CommonMethod.decodeEmoji(description));
                        txt_address.setText(CommonMethod.decodeEmoji(address));
                        txt_views.setText(CommonMethod.decodeEmoji(view));


                        if (!photos.equalsIgnoreCase("")) {
                            Glide.with(InfomationDetailActivity.this).load(CommonURL.ImagePath + CommonAPI_Name.infoimage + photos
                                    .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loading_bar).dontAnimate().into(img_info);
                        }else{
                            img_info.setVisibility(View.GONE);
                        }



                        if (click_action.equalsIgnoreCase("information_comment_click")) {
                            commentLists = new ArrayList<>();
                            page_count = 1;
                            dialog = new Dialog(InfomationDetailActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_comment_list);
                            dialog.show();

                            linearLayoutManager = new LinearLayoutManager(InfomationDetailActivity.this);
                            recyclerView_comments = (RecyclerView) dialog.findViewById(R.id.recyclerView_comments);
                            edit_comment = (EditText) dialog.findViewById(R.id.edit_comment);
                            img_send = (ImageView) dialog.findViewById(R.id.img_send);
                            img_send.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (TextUtils.isEmpty(edit_comment.getText().toString())) {
                                        edit_comment.setError("Please enter comment.");
                                        edit_comment.requestFocus();
                                    } else {

                                        if (CommonMethod.isInternetConnected(InfomationDetailActivity.this)) {
                                            new AdminComment().execute(edit_comment.getText().toString());
                                        } else {
                                            Toast.makeText(InfomationDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                            recyclerView_comments.setLayoutManager(linearLayoutManager);
                            img_nodata = (ImageView) dialog.findViewById(R.id.img_nodata);

                            if (CommonMethod.isInternetConnected(InfomationDetailActivity.this)) {
                                new CommentList().execute();
                            } else {
                                Toast.makeText(InfomationDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                            }
                            recyclerView_comments.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                            if (CommonMethod.isInternetConnected(InfomationDetailActivity.this)) {

                                                Log.e("total count", "--------------------" + page_count);

                                                page_count++;
                                                new CommentList().execute();
                                            } else {
                                                Toast.makeText(InfomationDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                                            }
                                            loading = true;

                                        }
                                    }
                                }
                            });

                            WindowManager.LayoutParams attrs = getWindow().getAttributes();
                            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                            getWindow().setAttributes(attrs);
                            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        } else {
//                            click_action="information_comment_click";
                        }

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
        // put your code here...

        setContent();
    }

}
