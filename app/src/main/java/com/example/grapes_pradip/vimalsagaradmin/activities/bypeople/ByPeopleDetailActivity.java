package com.example.grapes_pradip.vimalsagaradmin.activities.bypeople;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.admin.UserViewActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.gallery.ImageViewActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.video.VideoFullActivity;
import com.example.grapes_pradip.vimalsagaradmin.adapters.bypeople.RecyclerByPeopleCommentAdapter;
import com.example.grapes_pradip.vimalsagaradmin.adapters.information.RecyclerLikeAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.information.LikeList;
import com.example.jean.jcplayer.JcPlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import static com.example.grapes_pradip.vimalsagaradmin.activities.video.VideoDetailActivity.video_play_url;

@SuppressWarnings("ALL")
public class ByPeopleDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String id;
    private String title;
    private String post;
    private String audio;
    private String audioimage;
    private String video;
    private String videoimage;
    private String videoLink;
    private String uid;
    private String isapproved;
    private String date;
    private String name;
    private String photo;
    private String view;

    private EditText txt_title;
    private TextView txt_date;
    private EditText txt_description;
    private TextView txt_address;
    private TextView txt_header;
    private EditText txt_videolink;

    private TextView txt_views;
    private ImageView img_back;
    private ImageView img_photo;
    private Button btn_edit;
    private Button btn_delete;
    private JcPlayerView jcplayer_audio;
    private TextView detaillike;
    private Button infolike;
    private Dialog dialog;
    private ImageView img_play;
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

    private ArrayList<com.example.grapes_pradip.vimalsagaradmin.model.information.CommentList> commentLists = new ArrayList<>();
    private RecyclerByPeopleCommentAdapter recyclerCommentAdapter;
    private RecyclerView recyclerView_comments;
    private Button infocomment;
    private EditText edit_comment;
    private ImageView img_send;
    private TextView txt_img;
    private RelativeLayout rel_layout;
    private TextView audio_text;
    private TextView video_text;
    private TextView detailcomment;
    String action_click;
    RelativeLayout rel_video;
    private TextView txt_userdetail;
    private Button btn_update;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bypeople_detail_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final Intent intent = getIntent();

        id = intent.getStringExtra("ID");
        action_click = intent.getStringExtra("click_action");
        name = intent.getStringExtra("Name");

//        title = intent.getStringExtra("Title");
//        post = intent.getStringExtra("Post");
//        audio = intent.getStringExtra("Audio").replaceAll(" ", "%20");
//        audioimage = intent.getStringExtra("AudioImage");
//        video = intent.getStringExtra("Video").replaceAll(" ", "%20");
//        videoimage = intent.getStringExtra("VideoImage");
//        videoLink = intent.getStringExtra("VideoLink");
//        uid = intent.getStringExtra("UserID");
//        isapproved = intent.getStringExtra("Is_Approved");
//        date = intent.getStringExtra("Date");

//        view = intent.getStringExtra("view");
//        photo = intent.getStringExtra("Photo").replaceAll(" ", "%20");
        findID();
        idClick();
//        setContent();


        txt_userdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(ByPeopleDetailActivity.this, UserViewActivity.class);
                intent1.putExtra("uid", uid);
                startActivity(intent1);

            }
        });
    }


    private void findID() {
        txt_userdetail = (TextView) findViewById(R.id.txt_userdetail);
        rel_video = (RelativeLayout) findViewById(R.id.rel_video);
        txt_img = (TextView) findViewById(R.id.txt_img);
        txt_views = (TextView) findViewById(R.id.txt_views);
        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_title = (EditText) findViewById(R.id.txt_titles);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_description = (EditText) findViewById(R.id.txt_description);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_videolink = (EditText) findViewById(R.id.txt_videolink);
        detaillike = (TextView) findViewById(R.id.detaillike);
        infolike = (Button) findViewById(R.id.infolike);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_photo = (ImageView) findViewById(R.id.img_photo);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        jcplayer_audio = (JcPlayerView) findViewById(R.id.jcplayer_audio);
        edit_comment = (EditText) findViewById(R.id.edit_comment);
        infocomment = (Button) findViewById(R.id.infocomment);
        img_send = (ImageView) findViewById(R.id.img_send);
        btn_delete.setVisibility(View.GONE);
        img_play = (ImageView) findViewById(R.id.img_play);
        rel_layout = (RelativeLayout) findViewById(R.id.rel_layout);
        audio_text = (TextView) findViewById(R.id.audio_text);
        video_text = (TextView) findViewById(R.id.video_text);
        detailcomment = (TextView) findViewById(R.id.detailcomment);
        btn_update=findViewById(R.id.btn_update);
    }

    private void idClick() {
        img_back.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        infolike.setOnClickListener(this);
        infocomment.setOnClickListener(this);
//        btn_update.setOnClickListener(this);
    }


    private void setContent() {
        txt_header.setText("By People Detail");
        txt_title.setText(CommonMethod.decodeEmoji(title));
        txt_date.setText(CommonMethod.decodeEmoji(date));
        txt_description.setText(CommonMethod.decodeEmoji(post));
        txt_address.setText(CommonMethod.decodeEmoji(name));
        txt_videolink.setText(CommonMethod.decodeEmoji(videoLink));
        txt_views.setText(CommonMethod.decodeEmoji(view));

        if (photo.equalsIgnoreCase("null")) {
            txt_img.setVisibility(View.GONE);
            img_photo.setVisibility(View.GONE);
        } else {
//            Picasso.with(ByPeopleDetailActivity.this).load(CommonURL.ImagePath + CommonAPI_Name.bypeopleimage + photo.replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).resize(0,200).into(img_photo);

            Glide.with(ByPeopleDetailActivity.this).load(CommonURL.ImagePath + CommonAPI_Name.bypeopleimage + photo
                    .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loading_bar).dontAnimate().into(img_photo);

            Log.e("photo","--------------------"+CommonURL.ImagePath + CommonAPI_Name.bypeopleimage + photo);


        }
        if (video.equalsIgnoreCase("")) {
//            videoPlayerStandard.setVisibility(View.GONE);
            rel_video.setVisibility(View.GONE);
            video_text.setVisibility(View.GONE);
        } else {
//            videoPlayerStandard.setUp(CommonURL.VideoPath + CommonAPI_Name.bypeoplevideo + video, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
            video_play_url = CommonURL.VideoPath + CommonAPI_Name.bypeoplevideo + video;
            rel_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ByPeopleDetailActivity.this, VideoFullActivity.class);
                    intent.putExtra("videourl", "============" + video_play_url);
                    startActivity(intent);
                }
            });
        }
        if (audio.equalsIgnoreCase("")) {
            img_play.setVisibility(View.GONE);
            rel_layout.setVisibility(View.GONE);
            jcplayer_audio.setVisibility(View.GONE);
            audio_text.setVisibility(View.GONE);
        } else {
            final String Audio = CommonURL.AudioPath + CommonAPI_Name.bypeopleaudio + audio;
            Log.e("Audio", "-----------------" + Audio);
//        jcplayer_audio.playAudio();
//
            img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jcplayer_audio.setVisibility(View.VISIBLE);
                    jcplayer_audio.playAudio(Audio, "music");
                    img_play.setVisibility(View.GONE);
                }
            });
        }

        if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {
//            new CountLike().execute();
        } else {
            Toast.makeText(ByPeopleDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
        }
        img_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ByPeopleDetailActivity.this, ImageViewActivity.class);
                intent.putExtra("imagePath", CommonURL.ImagePath + CommonAPI_Name.bypeopleimage + photo.replaceAll(" ", "%20").replaceAll(" ", "%20"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_update:



                break;

            case R.id.img_back:

                jcplayer_audio.kill();
                onPause();
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.btn_delete:

                jcplayer_audio.kill();
                onPause();

                if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {
                    new DeletePost().execute();
                } else {
                    Toast.makeText(ByPeopleDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.infolike:
                likeLists = new ArrayList<>();
                page_count = 1;
                dialog = new Dialog(ByPeopleDetailActivity.this);
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
                linearLayoutManager = new LinearLayoutManager(ByPeopleDetailActivity.this);
                recyclerView_likes = (RecyclerView) dialog.findViewById(R.id.recyclerView_likes);
                recyclerView_likes.setLayoutManager(linearLayoutManager);
                img_nodata = (ImageView) dialog.findViewById(R.id.img_nodata);

                if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {
                    new LikeUserList().execute();
                } else {
                    Toast.makeText(ByPeopleDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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
                                if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {

                                    Log.e("total count", "--------------------" + page_count);

                                    page_count++;
                                    new LikeUserList().execute();
                                } else {
                                    Toast.makeText(ByPeopleDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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
                dialog = new Dialog(ByPeopleDetailActivity.this);
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
                linearLayoutManager = new LinearLayoutManager(ByPeopleDetailActivity.this);
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
//                            edit_comment.setText("");
                            if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {
                                new AdminComment().execute(edit_comment.getText().toString());
//                                dialog.dismiss();

                            } else {
                                Toast.makeText(ByPeopleDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                recyclerView_comments.setLayoutManager(linearLayoutManager);
                img_nodata = (ImageView) dialog.findViewById(R.id.img_nodata);

                if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {
                    new CommentList().execute();
                } else {
                    Toast.makeText(ByPeopleDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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
                                if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {

                                    Log.e("total count", "--------------------" + page_count);

                                    page_count++;
                                    new CommentList().execute();
                                } else {
                                    Toast.makeText(ByPeopleDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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

    private class DeletePost extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deletepost + "?p/id=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(ByPeopleDetailActivity.this, "Post delete successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(ByPeopleDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ByPeopleDetailActivity.this, "Post not delete.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(ByPeopleDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private class UpdatePost extends AsyncTask<String,Void,String>{
        String responseJson="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("id","1"));
            nameValuePairs.add(new BasicNameValuePair("title","1"));
            nameValuePairs.add(new BasicNameValuePair("post","1"));
            nameValuePairs.add(new BasicNameValuePair("videolink","1"));
            responseJson=JsonParser.postStringResponse(CommonURL.Main_url,nameValuePairs,ByPeopleDetailActivity.this);
            return responseJson;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }


    private class LikeUserList extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ByPeopleDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallusernamesforlikebypeople + "?pid=" + id + "&page=" + page_count + "&psize=1000");
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
                        String informationID = jsonObject1.getString("PostID");
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
                recyclerLikeAdapter = new RecyclerLikeAdapter(ByPeopleDetailActivity.this, likeLists);
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
            progressDialog = new ProgressDialog(ByPeopleDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallcommentsforadminbypeople + "?pid=" + id + "&page=" + page_count + "&psize=1000");
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
                        String informationID = jsonObject1.getString("PostID");
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
                recyclerCommentAdapter = new RecyclerByPeopleCommentAdapter(ByPeopleDetailActivity.this, commentLists);
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
            progressDialog = new ProgressDialog(ByPeopleDetailActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("pid", id));
                nameValuePairs.add(new BasicNameValuePair("Comment", params[0]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.admincommentbypeople, nameValuePairs, ByPeopleDetailActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------------" + s);
            progressDialog.dismiss();
            edit_comment.setText("");
            commentLists = new ArrayList<>();
            new CommentList().execute();
            Toast.makeText(ByPeopleDetailActivity.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    edit_comment.setText("");
                    if (CommonMethod.isInternetConnected(ByPeopleDetailActivity.this)) {
                        commentLists = new ArrayList<>();
                        new CommentList().execute();
                        Toast.makeText(ByPeopleDetailActivity.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                        Toast.makeText(ByPeopleDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        ;
                    } else {
                        Toast.makeText(ByPeopleDetailActivity.this, "Comment not added.", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(ByPeopleDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    private class GetPostDetail extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ByPeopleDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getpostbyidforadmin + "?pid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
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
                        JSONObject object = jsonArray.getJSONObject(i);
                        photo = object.getString("Photo").replaceAll(" ", "%20");
                        title = object.getString("Title");
                        post = object.getString("Post");
                        uid = object.getString("UserID");
                        String dates = object.getString("Date");
                        view = object.getString("View");
                        audio = object.getString("Audio").replaceAll(" ", "%20");
                        audioimage = object.getString("AudioImage").replaceAll(" ", "%20");
                        videoLink = object.getString("VideoLink");
                        video = object.getString("Video").replaceAll(" ", "%20");
                        videoimage = object.getString("VideoImage").replaceAll(" ", "%20");
                        videoLink = object.getString("VideoLink");
                        view = object.getString("View");

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

                        setContent();


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
        new GetPostDetail().execute();
    }

}