package com.example.grapes_pradip.vimalsagaradmin.activities.event;

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
import android.support.v4.content.ContextCompat;
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

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.adapters.AudioListAdapter;
import com.example.grapes_pradip.vimalsagaradmin.adapters.PhotoAudioVideoAdapter;
import com.example.grapes_pradip.vimalsagaradmin.adapters.VideoListAdapter;
import com.example.grapes_pradip.vimalsagaradmin.adapters.event.RecyclerEventCommentAdapter;
import com.example.grapes_pradip.vimalsagaradmin.adapters.information.RecyclerLikeAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.PhotoAudioVideoItem;
import com.example.grapes_pradip.vimalsagaradmin.model.gallery.ImageItemSplash;
import com.example.grapes_pradip.vimalsagaradmin.model.information.LikeList;
import com.example.jean.jcplayer.JcPlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import static com.example.grapes_pradip.vimalsagaradmin.activities.gallery.AllGalleryActivity.itemSplashArrayList;

/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String id;
    private String title;
    private String description;
    private String address;
    private String date;
    private String audio;
    private String audio_img;
    private String videoLink;
    private String video;
    private String video_img;
    private String photo = "";
    private String view;
    private TextView txt_title;
    private TextView txt_date;
    private TextView txt_description;
    private TextView txt_address;
    private TextView txt_header;
    private TextView txt_videolink;
    private TextView detaillike;
    private ImageView img_back;
    //    private ImageView img_photo;
    private Button btn_edit;
    private Button btn_delete;
    private Button eventlike;
    private JcPlayerView jcplayer_audio;
    private TextView txt_img;
    private TextView txt_audio;
    private TextView txt_video;
    private TextView txt_views;
    private ProgressDialog progressDialog;

    //page count
    private int page_count = 1;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private ImageView img_nodata;
    private RecyclerLikeAdapter recyclerLikeAdapter;
    private ArrayList<LikeList> likeLists = new ArrayList<>();
    private RecyclerView recyclerView_likes;
    private Dialog dialog;

    private ArrayList<com.example.grapes_pradip.vimalsagaradmin.model.information.CommentList> commentLists = new ArrayList<>();
    private RecyclerEventCommentAdapter recyclerCommentAdapter;
    private RecyclerView recyclerView_comments, recycleview_slide;
    private Button infocomment;
    private EditText edit_comment;
    private ImageView img_send;
    private ImageView img_play;
    private RelativeLayout rel_audio;
    private TextView detailcomment;
    String click_action;
    RelativeLayout rel_video;

    private ImageView img_photos;
    private ImageView img_audios;
    private ImageView img_videos;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManager1;

    ArrayList<PhotoAudioVideoItem> photoAudioVideoItems;
    PhotoAudioVideoAdapter photoAudioVideoAdapter;

    AudioListAdapter audioListAdapter;
    VideoListAdapter videoListAdapter;

    ArrayList<String> photolist = new ArrayList<>();
    ArrayList<String> audiolist = new ArrayList<>();
    ArrayList<String> videolist = new ArrayList<>();
    String[] photos;
    String[] audios;
    String[] videos;
    TextView txt_nodata;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        id = intent.getStringExtra("event_id");
        click_action = intent.getStringExtra("click_action");
        if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {
            new GetEventDetail().execute();
        } else {
            Toast.makeText(EventDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
        }
//        title = intent.getStringExtra("title");
//        description = intent.getStringExtra("description");
//        address = intent.getStringExtra("address");
//        date = intent.getStringExtra("date");
//        audio = intent.getStringExtra("audio").replaceAll(" ", "%20");
//        audio_img = intent.getStringExtra("audio_img");
//        videoLink = intent.getStringExtra("videoLink");
//        video = intent.getStringExtra("video").replaceAll(" ", "%20");
//        video_img = intent.getStringExtra("video_img");
//        photo = intent.getStringExtra("photo").replaceAll(" ", "%20");
//        view = intent.getStringExtra("view");
//        Log.e("photo", "------------------" + photo);
        findID();
        idClick();


//        setContent();
    }

    private void findID() {
        txt_nodata=(TextView)findViewById(R.id.txt_nodata);
        recycleview_slide = (RecyclerView) findViewById(R.id.recycleview_slide);
        linearLayoutManager1 = new LinearLayoutManager(EventDetailActivity.this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleview_slide.setLayoutManager(linearLayoutManager1);

        rel_video = (RelativeLayout) findViewById(R.id.rel_video);
        txt_img = (TextView) findViewById(R.id.txt_img);
        txt_audio = (TextView) findViewById(R.id.txt_audio);
        txt_video = (TextView) findViewById(R.id.txt_video);
        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_title = (TextView) findViewById(R.id.txt_titles);
        txt_date = (TextView) findViewById(R.id.txt_date);
        txt_description = (TextView) findViewById(R.id.txt_description);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_videolink = (TextView) findViewById(R.id.txt_videolink);
        detaillike = (TextView) findViewById(R.id.detaillike);
        img_back = (ImageView) findViewById(R.id.img_back);
//        img_photo = (ImageView) findViewById(R.id.img_photo);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        txt_views = (TextView) findViewById(R.id.txt_views);

        edit_comment = (EditText) findViewById(R.id.edit_comment);
        infocomment = (Button) findViewById(R.id.infocomment);
        img_send = (ImageView) findViewById(R.id.img_send);

        btn_delete = (Button) findViewById(R.id.btn_delete);
        eventlike = (Button) findViewById(R.id.eventlike);
        jcplayer_audio = (JcPlayerView) findViewById(R.id.jcplayer_audio);
        img_play = (ImageView) findViewById(R.id.img_play);
        rel_audio = (RelativeLayout) findViewById(R.id.rel_audio);
        detailcomment = (TextView) findViewById(R.id.detailcomment);

        img_photos = (ImageView) findViewById(R.id.img_photos);
        img_audios = (ImageView) findViewById(R.id.img_audios);
        img_videos = (ImageView) findViewById(R.id.img_video_icon);

    }

    private void idClick() {
        img_back.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        eventlike.setOnClickListener(this);
        infocomment.setOnClickListener(this);
        img_photos.setOnClickListener(this);
        img_audios.setOnClickListener(this);
        img_videos.setOnClickListener(this);

    }

    @SuppressLint("SetTextI18n")
    private void setContent() {
        txt_header.setText("Event Detail");

        txt_title.setText(CommonMethod.decodeEmoji(title));
        txt_description.setText(CommonMethod.decodeEmoji(description));
        txt_address.setText(CommonMethod.decodeEmoji(address));
        txt_date.setText(CommonMethod.decodeEmoji(date));
        txt_videolink.setText(CommonMethod.decodeEmoji(videoLink));

        if (photo.equalsIgnoreCase("null")) {
            txt_nodata.setVisibility(View.VISIBLE);
            txt_nodata.setText("No Images\n Avalable.");
//            txt_nodata.setVisibility(View.GONE);
//            img_photo.setVisibility(View.GONE);
        } else {
            recycleview_slide.setVisibility(View.VISIBLE);
            setPhotos();
        }
        /*if (audio.equalsIgnoreCase("")) {
            txt_audio.setVisibility(View.GONE);
            jcplayer_audio.setVisibility(View.GONE);
            rel_audio.setVisibility(View.GONE);
        } else {
            rel_audio.setVisibility(View.VISIBLE);
            final String Audio = CommonURL.AudioPath + CommonAPI_Name.eventaudio + audio;
            Log.e("Audio", "-----------------" + Audio);
//        jcplayer_audio.playAudio();
            img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jcplayer_audio.setVisibility(View.VISIBLE);
                    img_play.setVisibility(View.GONE);
                    jcplayer_audio.playAudio(Audio, "music");
                }
            });

        }
        if (video.equalsIgnoreCase("")) {
            txt_video.setVisibility(View.GONE);
            rel_video.setVisibility(View.GONE);
        } else {
            video_play_url = CommonURL.VideoPath + CommonAPI_Name.eventvideo + video;
            rel_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EventDetailActivity.this, VideoFullActivity.class);
                    intent.putExtra("videourl", "============" + video_play_url);
                    startActivity(intent);
                }
            });

//            videoPlayerStandard.setUp(CommonURL.VideoPath + CommonAPI_Name.eventvideo + video, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        }*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_photos:
                if (photo.equalsIgnoreCase("")) {
                    txt_nodata.setVisibility(View.VISIBLE);
                    txt_nodata.setText("No Images\n Avalable.");
                } else {
                    txt_nodata.setVisibility(View.GONE);
                    recycleview_slide.setVisibility(View.VISIBLE);
                    setPhotos();
                }
                break;

            case R.id.img_audios:
                if (audio.equalsIgnoreCase("")) {
                    txt_nodata.setVisibility(View.VISIBLE);
                    txt_nodata.setText("No Audio\n Avalable.");
                } else {
                    txt_nodata.setVisibility(View.GONE);
                    recycleview_slide.setVisibility(View.VISIBLE);
                    setAudios();
                }


//                Audio set
                /*photoAudioVideoItems = new ArrayList<PhotoAudioVideoItem>();
                for (int i = 0; i <= audio.length; i++) {

                    photoAudioVideoItems.add(new PhotoAudioVideoItem(audio[i], "name", "specification"));
                }
                photoAudioVideoAdapter = new PhotoAudioVideoAdapter(EventDetailActivity.this, photoAudioVideoItems);
                recycleview_photo.setAdapter(photoAudioVideoAdapter);*/

                break;

            case R.id.img_video_icon:
                if (video.equalsIgnoreCase("")) {
                    txt_nodata.setVisibility(View.VISIBLE);
                    txt_nodata.setText("No Video\n Avalable.");
                } else {
                    txt_nodata.setVisibility(View.GONE);
                    recycleview_slide.setVisibility(View.VISIBLE);
                    setVideos();
                }

//                Video set

                /*photoAudioVideoItems = new ArrayList<PhotoAudioVideoItem>();
                for (int i = 0; i <= video.length; i++) {

                    photoAudioVideoItems.add(new PhotoAudioVideoItem(video[i], "name", "specification"));
                }
                photoAudioVideoAdapter = new PhotoAudioVideoAdapter(EventDetailActivity.this, photoAudioVideoItems);
                recycleview_photo.setAdapter(photoAudioVideoAdapter);*/

                break;

            case R.id.img_back:

                super.onBackPressed();
                jcplayer_audio.kill();
                onPause();
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.btn_edit:
                super.onBackPressed();
                jcplayer_audio.kill();
                onPause();
                Intent intent = new Intent(EventDetailActivity.this, EditEventActivity.class);
                intent.putExtra("event_id", id);
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("address", address);
                intent.putExtra("audio", audio);
                intent.putExtra("audio_img", audio_img);
                intent.putExtra("videoLink", videoLink);
                intent.putExtra("video", video);
                intent.putExtra("video_img", video_img);
                intent.putExtra("photo", photo);
                intent.putExtra("date", date);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.btn_delete:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EventDetailActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to delete?.");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_warning);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {
                            new DeleteEvent().execute();
                        } else {
                            Toast.makeText(EventDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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
            case R.id.eventlike:
                likeLists = new ArrayList<>();
                page_count = 1;
                dialog = new Dialog(EventDetailActivity.this);
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
                linearLayoutManager = new LinearLayoutManager(EventDetailActivity.this);
                recyclerView_likes = (RecyclerView) dialog.findViewById(R.id.recyclerView_likes);
                recyclerView_likes.setLayoutManager(linearLayoutManager);
                img_nodata = (ImageView) dialog.findViewById(R.id.img_nodata);

                if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {
                    new LikeUserList().execute();
                } else {
                    Toast.makeText(EventDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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
                                if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {

                                    Log.e("total count", "--------------------" + page_count);

                                    page_count++;
                                    new LikeUserList().execute();
                                } else {
                                    Toast.makeText(EventDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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
                dialog = new Dialog(EventDetailActivity.this);
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
                linearLayoutManager = new LinearLayoutManager(EventDetailActivity.this);
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
                            if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {
                                new AdminComment().execute(edit_comment.getText().toString());
                                edit_comment.setText("");

                            } else {
                                Toast.makeText(EventDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                recyclerView_comments.setLayoutManager(linearLayoutManager);
                img_nodata = (ImageView) dialog.findViewById(R.id.img_nodata);

                if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {
                    new CommentList().execute();
                } else {
                    Toast.makeText(EventDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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
                                if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {

                                    Log.e("total count", "--------------------" + page_count);

                                    page_count++;
                                    new CommentList().execute();
                                } else {
                                    Toast.makeText(EventDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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

    private void setVideos() {
        img_photos.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.white));
        img_audios.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.white));
        img_videos.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.kprogresshud_grey_color));
        photoAudioVideoItems = new ArrayList<>();
        for (int i = 0; i < videolist.size(); i++) {
            Log.e("video split", "---------------" + videolist.get(i));
            photoAudioVideoItems.add(new PhotoAudioVideoItem(videolist.get(i), "Video", "Video"));
            videoListAdapter = new VideoListAdapter(EventDetailActivity.this, photoAudioVideoItems);
            recycleview_slide.setAdapter(videoListAdapter);
        }
//        videoListAdapter = new VideoListAdapter(EventDetailActivity.this, videolist);
//        recycleview_slide.setAdapter(videoListAdapter);
    }

    private void setAudios() {
        img_photos.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.white));
        img_audios.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.kprogresshud_grey_color));
        img_videos.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.white));
        photoAudioVideoItems = new ArrayList<>();
        for (int i = 0; i < audiolist.size(); i++) {

            Log.e("audio split", "---------------" + audiolist.get(i));
            photoAudioVideoItems.add(new PhotoAudioVideoItem(audiolist.get(i), "Audio", "Audio"));
            audioListAdapter = new AudioListAdapter(EventDetailActivity.this, photoAudioVideoItems);
            recycleview_slide.setAdapter(audioListAdapter);
        }

//        audioListAdapter = new AudioListAdapter(EventDetailActivity.this, audiolist);
//        recycleview_slide.setAdapter(audioListAdapter);
    }

    private void setPhotos() {
        img_photos.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.kprogresshud_grey_color));
        img_audios.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.white));
        img_videos.setBackgroundColor(ContextCompat.getColor(EventDetailActivity.this, R.color.white));
//                Photo set

        photoAudioVideoItems = new ArrayList<PhotoAudioVideoItem>();
        photoAudioVideoAdapter = new PhotoAudioVideoAdapter(EventDetailActivity.this, photolist);
        recycleview_slide.setAdapter(photoAudioVideoAdapter);

    }

    private class DeleteEvent extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.delete_event + "?eid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(EventDetailActivity.this, "Event delete successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(EventDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EventDetailActivity.this, "Event not delete.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(EventDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private class LikeUserList extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EventDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallusernamesforlikeevent + "?eid=" + id + "&page=" + page_count + "&psize=30");
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
                        String informationID = jsonObject1.getString("EventID");
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
                recyclerLikeAdapter = new RecyclerLikeAdapter(EventDetailActivity.this, likeLists);
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
            progressDialog = new ProgressDialog(EventDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallcommentsevent + "?eid=" + id + "&page=" + page_count + "&psize=30");
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
                        String informationID = jsonObject1.getString("EventID");
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
                recyclerCommentAdapter = new RecyclerEventCommentAdapter(EventDetailActivity.this, commentLists);
                Log.e("response", "-----------------" + "object");
                if (recyclerCommentAdapter.getItemCount() != 0) {
                    recyclerView_comments.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_comments.setAdapter(recyclerCommentAdapter);
                    Log.e("if response", "-----------------" + "setlayout");
                } else {
                    recyclerView_comments.setVisibility(View.GONE);
                    img_nodata.setVisibility(View.VISIBLE);
                    Log.e("else response", "-----------------" + "setlayout");
                }
            }
        }
    }

    private class AdminComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EventDetailActivity.this);
            progressDialog.setMessage("Loading..");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("eid", id));
                nameValuePairs.add(new BasicNameValuePair("Comment", params[0]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.admincommentevent, nameValuePairs, EventDetailActivity.this);
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
                    Toast.makeText(EventDetailActivity.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
                    if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {
                        commentLists = new ArrayList<>();
                        new CommentList().execute();

//                        Toast.makeText(EventDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        edit_comment.setText("");
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(EventDetailActivity.this, "Comment not added.", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(EventDetailActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {

        super.onBackPressed();
        jcplayer_audio.kill();
        onPause();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private class GetEventDetail extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EventDetailActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getEventDetail + "?eid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------------" + s);
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
                        JSONObject object = jsonArray.getJSONObject(i);

                        title = object.getString("Title");
                        description = object.getString("Description");
                        address = object.getString("Address");
                        String dates = object.getString("Date");
                        view = object.getString("View");
                        txt_views.setText(view);
                        audio = object.getString("Audio").replaceAll(" ", "%20");

                        audios = audio.split(",");
                        for (int j = 0; i < audios.length; i++) {
                            audiolist.add(audios[i]);
                        }

                        videoLink = object.getString("VideoLink");
                        video = object.getString("Video").replaceAll(" ", "%20");

                        videos = video.split(",");
                        for (int k = 0; k < videos.length; k++) {
                            videolist.add(videos[k]);
                        }

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


                        if (click_action.equalsIgnoreCase("event_comment_click")) {
                            commentLists = new ArrayList<>();
                            page_count = 1;
                            dialog = new Dialog(EventDetailActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_comment_list);
                            dialog.show();

                            linearLayoutManager = new LinearLayoutManager(EventDetailActivity.this);
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
                                        if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {
                                            new AdminComment().execute(edit_comment.getText().toString());
                                            edit_comment.setText("");
                                            Toast.makeText(EventDetailActivity.this, "Comment added successfully.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(EventDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                            recyclerView_comments.setLayoutManager(linearLayoutManager);
                            img_nodata = (ImageView) dialog.findViewById(R.id.img_nodata);

                            if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {
                                new CommentList().execute();
                            } else {
                                Toast.makeText(EventDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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
                                            if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {

                                                Log.e("total count", "--------------------" + page_count);

                                                page_count++;
                                                new CommentList().execute();
                                            } else {
                                                Toast.makeText(EventDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
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
//                            click_action = "event_comment_click";
                        }
                    }
                    JSONArray jsonArray1 = jsonObject.getJSONArray("Photo");
                    photo = "null";
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject object = jsonArray1.getJSONObject(i);
                        photo = object.getString("Photo");
                        photolist.add(photo);
                        itemSplashArrayList.add(new ImageItemSplash(CommonURL.ImagePath + CommonAPI_Name.eventimage + photo, CommonURL.ImagePath + CommonAPI_Name.eventimage + photo, false));
                        Log.e("size", "------------------" + itemSplashArrayList.size());
//                        setPhotos();
//                        Log.e("photo","------------------"+photo);
                    }


                    Log.e("photo", "------------------" + photo);
                    setContent();
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

        /*if (CommonMethod.isInternetConnected(EventDetailActivity.this)) {
            new GetEventDetail().execute();
        } else {
            Toast.makeText(EventDetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
        }*/
    }


}
