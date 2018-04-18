package com.example.grapes_pradip.vimalsagaradmin.activities.audio;

import android.annotation.SuppressLint;
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

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.gallery.ImageViewActivity;
import com.example.grapes_pradip.vimalsagaradmin.adapters.audio.RecyclerAudioCommentAdapter;
import com.example.grapes_pradip.vimalsagaradmin.adapters.information.RecyclerLikeAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.information.LikeList;
import com.example.jean.jcplayer.JcPlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class AudioDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String aid;
    private String audioname;
    private String cid;
    private String audio;
    private String photo;
    private String duration;
    private String date;
    private String datefull;
    private String categoryname;
    private String view;
    private TextView txt_title;
    private TextView txt_date;
    private TextView txt_header;
    private TextView txt_category;
    private TextView txt_views;
    private ImageView img_back;
    private ImageView img_photo;
    private Button btn_edit;
    private Button btn_delete;
    private JcPlayerView jcplayer_audio;
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
    private ArrayList<LikeList> likeLists = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView_likes;
    private Button audiolike;
    private TextView detaillike;
    private TextView detailcomment;

    private ArrayList<com.example.grapes_pradip.vimalsagaradmin.model.information.CommentList> commentLists = new ArrayList<>();
    private RecyclerAudioCommentAdapter recyclerCommentAdapter;
    private RecyclerView recyclerView_comments;
    private Button infocomment;
    private EditText edit_comment;
    private ImageView img_send;
    String action_click;
    String fulldate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_detail_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        aid = intent.getStringExtra("aid");
        categoryname = intent.getStringExtra("categoryname");
        action_click = intent.getStringExtra("click_action");
//        view = intent.getStringExtra("view");
        findID();
        idClick();
//        setContent();
    }


    private void findID() {
        txt_views = (TextView) findViewById(R.id.txt_views);
        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_category = (TextView) findViewById(R.id.txt_category);
        txt_title = (TextView) findViewById(R.id.txt_titles);
        txt_date = (TextView) findViewById(R.id.txt_date);
        detaillike = (TextView) findViewById(R.id.detaillike);
        detailcomment = (TextView) findViewById(R.id.detailcomment);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_photo = (ImageView) findViewById(R.id.img_photo);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        audiolike = (Button) findViewById(R.id.audiolike);
        jcplayer_audio = (JcPlayerView) findViewById(R.id.jcplayer_audio);


        edit_comment = (EditText) findViewById(R.id.edit_comment);
        infocomment = (Button) findViewById(R.id.infocomment);
        img_send = (ImageView) findViewById(R.id.img_send);

    }

    private void idClick() {
        img_back.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        audiolike.setOnClickListener(this);
        infocomment.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void setContent() {
        txt_header.setText("Audio Detail");
        txt_category.setText(categoryname);

        if (CommonMethod.isInternetConnected(AudioDetailActivity.this)) {
            new AudioDetail().execute();
            img_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AudioDetailActivity.this, ImageViewActivity.class);
                    intent.putExtra("imagePath", CommonURL.ImagePath + CommonAPI_Name.audioimage + photo.replaceAll(" ", "%20").replaceAll(" ", "%20"));
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        } else {
            Toast.makeText(AudioDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                jcplayer_audio.kill();
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.btn_edit:
                jcplayer_audio.kill();
                Intent intent = new Intent(AudioDetailActivity.this, EditAudioActivity.class);
                intent.putExtra("aid", aid);
                intent.putExtra("audioname", audioname);
                intent.putExtra("cid", cid);
                intent.putExtra("audio", audio);
                intent.putExtra("photo", photo);
                intent.putExtra("duration", duration);
                intent.putExtra("date", datefull);
                intent.putExtra("categoryname", categoryname);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.btn_delete:
                jcplayer_audio.kill();
                if (CommonMethod.isInternetConnected(AudioDetailActivity.this)) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AudioDetailActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Confirm Delete...");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want to delete?.");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_warning);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            new DeleteAudio().execute();

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
                    Toast.makeText(AudioDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.audiolike:
                likeLists = new ArrayList<>();
                page_count = 1;
                dialog = new Dialog(AudioDetailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_like_list);
                dialog.show();

                linearLayoutManager = new LinearLayoutManager(AudioDetailActivity.this);
                recyclerView_likes = (RecyclerView) dialog.findViewById(R.id.recyclerView_likes);
                recyclerView_likes.setLayoutManager(linearLayoutManager);
                img_nodata = (ImageView) dialog.findViewById(R.id.img_nodata);
                ImageView img_back = (ImageView) dialog.findViewById(R.id.img_back);
                img_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                if (CommonMethod.isInternetConnected(AudioDetailActivity.this)) {
                    new LikeUserList().execute();
                } else {
                    Toast.makeText(AudioDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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
                                if (CommonMethod.isInternetConnected(AudioDetailActivity.this)) {

                                    Log.e("total count", "--------------------" + page_count);

                                    page_count++;
                                    new LikeUserList().execute();
                                } else {
                                    Toast.makeText(AudioDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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

                Intent intent1 = new Intent(AudioDetailActivity.this, CommentListAudio.class);
                intent1.putExtra("click_action", "audio_comment_click");
                intent1.putExtra("aid", aid);
                intent1.putExtra("categoryname", categoryname);
                startActivity(intent1);
                /*commentLists = new ArrayList<>();
                page_count = 1;
                dialog = new Dialog(AudioDetailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_comment_list);
                dialog.show();

                linearLayoutManager = new LinearLayoutManager(AudioDetailActivity.this);
                recyclerView_comments = (RecyclerView) dialog.findViewById(R.id.recyclerView_comments);
                edit_comment = (EditText) dialog.findViewById(R.id.edit_comment);
                img_send = (ImageView) dialog.findViewById(R.id.img_send);
                ImageView img_comment_back = (ImageView) dialog.findViewById(R.id.img_back);

                img_comment_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                img_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(edit_comment.getText().toString())) {
                            edit_comment.setError("Please enter comment.");
                            edit_comment.requestFocus();
                        } else {
                            if (CommonMethod.isInternetConnected(AudioDetailActivity.this)) {
                                new AdminComment().execute(edit_comment.getText().toString());
                            } else {
                                Toast.makeText(AudioDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                recyclerView_comments.setLayoutManager(linearLayoutManager);
                img_nodata = (ImageView) dialog.findViewById(R.id.img_nodata);

                if (CommonMethod.isInternetConnected(AudioDetailActivity.this)) {
                    new CommentList().execute();
                } else {
                    Toast.makeText(AudioDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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
                                if (CommonMethod.isInternetConnected(AudioDetailActivity.this)) {

                                    Log.e("total count", "--------------------" + page_count);

                                    page_count++;
                                    new CommentList().execute();
                                } else {
                                    Toast.makeText(AudioDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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
                break;*/
        }
    }

    private class DeleteAudio extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deleteaudio + "?aid=" + aid);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(AudioDetailActivity.this, "Audio delete successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AudioDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AudioDetailActivity.this, "Audio not delete.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AudioDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class AudioDetail extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AudioDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getaudiobyidforadmin + "?aid=" + aid);
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
                    progressDialog.dismiss();
                    String commentcount = jsonObject.getString("commentcount");
                    Log.e("like", "-----------------" + commentcount);
                    detailcomment.setText(commentcount);

                    String likecount = jsonObject.getString("likecount");
                    Log.e("like", "-----------------" + likecount);
                    detaillike.setText(likecount);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        audioname = jsonObject1.getString("AudioName");
                        cid = jsonObject1.getString("CategoryID");
                        audio = jsonObject1.getString("Audio").replaceAll(" ", "%20");
                        photo = jsonObject1.getString("Photo").replaceAll(" ", "%20");
                        duration = jsonObject1.getString("Duration");
                        date = jsonObject1.getString("Date");
                        view = jsonObject1.getString("View");


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

                        fulldate = dayOfTheWeek + ", " + day + "/" + intMonth + "/" + year + ", " + string[1];
                        datefull = fulldate;
                        txt_title.setText(audioname);
                        txt_date.setText(fulldate);
                        txt_views.setText(view);
                        Picasso.with(AudioDetailActivity.this).load(CommonURL.ImagePath + CommonAPI_Name.audioimage + jsonObject1.getString("Photo").replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).into(img_photo);
                        jcplayer_audio.playAudio(CommonURL.AudioPath + CommonAPI_Name.audios + jsonObject1.getString("Audio").replaceAll(" ", "%20"), jsonObject1.getString("AudioName"));

                        if (action_click.equalsIgnoreCase("audio_comment_click")) {
                            commentLists = new ArrayList<>();
                            page_count = 1;
                            dialog = new Dialog(AudioDetailActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_comment_list);
                            dialog.show();

                            linearLayoutManager = new LinearLayoutManager(AudioDetailActivity.this);
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
                                        if (CommonMethod.isInternetConnected(AudioDetailActivity.this)) {
                                            new AdminComment().execute(edit_comment.getText().toString());
                                        } else {
                                            Toast.makeText(AudioDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                            recyclerView_comments.setLayoutManager(linearLayoutManager);
                            img_nodata = (ImageView) dialog.findViewById(R.id.img_nodata);

                            if (CommonMethod.isInternetConnected(AudioDetailActivity.this)) {
                                new CommentList().execute();
                            } else {
                                Toast.makeText(AudioDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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
                                            if (CommonMethod.isInternetConnected(AudioDetailActivity.this)) {

                                                Log.e("total count", "--------------------" + page_count);

                                                page_count++;
                                                new CommentList().execute();
                                            } else {
                                                Toast.makeText(AudioDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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
                            action_click = "audio_comment_click";
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
//                new CountLike().execute();
            }
        }
    }

   /* private class CountComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getaudiobyidforadmin + "?aid=" + aid);
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
                    String comment = jsonObject.getString("commentcount");
                    detailcomment.setText(comment);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
                new CountLike().execute();
            }
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        jcplayer_audio.kill();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /*private class CountLike extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AudioDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.countlikesaudio + "?aid=" + aid);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    String likecount = jsonArray.getString(0);
                    Log.e("like", "-----------------" + likecount);
                    detaillike.setText(likecount);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }*/

    private class LikeUserList extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AudioDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallusernamesforlikeaudio + "?aid=" + aid + "&page=" + page_count + "&psize=30");
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
                        String informationID = jsonObject1.getString("AudioID");
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
                recyclerLikeAdapter = new RecyclerLikeAdapter(AudioDetailActivity.this, likeLists);
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
            progressDialog = new ProgressDialog(AudioDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallappcommentsaudio + "?aid=" + aid + "&page=" + page_count + "&psize=30");
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
                        String informationID = jsonObject1.getString("AudioID");
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
                recyclerCommentAdapter = new RecyclerAudioCommentAdapter(AudioDetailActivity.this, commentLists);
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
            progressDialog = new ProgressDialog(AudioDetailActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("aid", aid));
                nameValuePairs.add(new BasicNameValuePair("Comment", params[0]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.admincommentaudio, nameValuePairs, AudioDetailActivity.this);
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
                    if (CommonMethod.isInternetConnected(AudioDetailActivity.this)) {
                        progressDialog.dismiss();
                        commentLists = new ArrayList<>();
                        new CommentList().execute();
                        Toast.makeText(AudioDetailActivity.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(AudioDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        edit_comment.setText("");
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(AudioDetailActivity.this, "Comment not added.", Toast.LENGTH_SHORT).show();
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

        setContent();
//        onNewIntent(getIntent());
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        aid = intent.getStringExtra("aid");
//        categoryname = intent.getStringExtra("categoryname");
//        action_click = intent.getStringExtra("click_action");
//        Log.e("aid","---------------"+aid);
//        Log.e("categoryname","---------------"+categoryname);
//        Log.e("action_click","---------------"+action_click);
//        setContent();
//
//
////        if(intent.getStringExtra("methodName").equals("myMethod"))
////        {
////            myMethod();
////        }
//    }

}
