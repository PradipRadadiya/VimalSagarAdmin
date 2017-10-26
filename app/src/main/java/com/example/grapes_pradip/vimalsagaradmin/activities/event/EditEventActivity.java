package com.example.grapes_pradip.vimalsagaradmin.activities.event;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.AudioPlayActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.video.VideoFullActivity;
import com.example.grapes_pradip.vimalsagaradmin.adapters.PhotoAudioVideoAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.PhotoAudioVideoItem;
import com.example.grapes_pradip.vimalsagaradmin.model.event.EventImage;
import com.example.grapes_pradip.vimalsagaradmin.util.ImageCompression;
import com.example.grapes_pradip.vimalsagaradmin.util.MarshMallowPermission;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntity;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.entity.mime.content.StringBody;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;

import static com.example.grapes_pradip.vimalsagaradmin.activities.gallery.AllGalleryActivity.itemSplashArrayList;
import static com.example.grapes_pradip.vimalsagaradmin.activities.video.VideoDetailActivity.video_play_url;

/**
 * Created by Grapes-Pradip on 2/16/2017.
 */

@SuppressWarnings("ALL")
public class EditEventActivity extends AppCompatActivity implements View.OnClickListener {
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
    private String photo;
    private ProgressDialog progressDialog;
    private EditText e_title;
    private EditText e_description;
    private EditText e_address;
    private EditText edit_date;
    private EditText edit_videolink;
    private TextView txt_header;
    private TextView txt_photo;
    private TextView txt_audio;
    private TextView txt_video;
    private ImageView img_back;
    private Button btn_add;
    private int mHour;
    private int mMinute;
    private Intent intent;
    private String picturePath = null;
    private String audioPath = null;
    private String videoPath = null;
    private boolean flag = false;
    private MarshMallowPermission permission;
    private ImageView img_category_icon;
    Bitmap thumbnail;
    ArrayList<String> photolist = new ArrayList<>();
    ArrayList<String> audiolist = new ArrayList<>();
    ArrayList<String> videolist = new ArrayList<>();
    ArrayList<EventImage> eventImages = new ArrayList<>();
    LinearLayout lin_main;
    private RecyclerView recyclerView_photos, recyclerView_audio, recyclerView_video;
    LinearLayoutManager linearLayoutManager1;
    LinearLayoutManager linearLayoutManager2;
    LinearLayoutManager linearLayoutManager3;
    AudioAdapter audioListAdapter;
    VideoAdapter videoListAdapter;
    ArrayList<PhotoAudioVideoItem> photoAudioVideoItems;
    PhotoAudioVideoAdapter photoAudioVideoAdapter;
    TextView textphotos, textaudios, textvideos;
    PhotoAdapter photoAdapter;
    ArrayList<EventImage> getEventImages = new ArrayList<>();
    private String imagename, audioname, videoname;
    EditText edit_time;
    String datetimefull;
    String fulltime;
    String fulldate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        permission = new MarshMallowPermission(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("event_id");
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
        findID();


        if (CommonMethod.isInternetConnected(EditEventActivity.this)) {
            new GetEventDetail().execute();
        } else {
            Toast.makeText(this, R.string.internet, Toast.LENGTH_SHORT).show();
        }
//        edit_date.setVisibility(View.GONE);

        idClick();
//        setContent();
    }

    private void idClick() {
        txt_photo.setOnClickListener(this);
        txt_audio.setOnClickListener(this);
        txt_video.setOnClickListener(this);

        edit_date.setEnabled(true);
        edit_time.setEnabled(true);

        edit_date.setCursorVisible(false);
        edit_date.setFocusableInTouchMode(false);
//        edit_date.setFocusable(false);

        edit_time.setCursorVisible(false);
        edit_time.setFocusableInTouchMode(false);
//        edit_date.setFocusable(false);
        edit_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    openDatePicker();
                }
            }
        });

        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        edit_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(EditEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            edit_time.setText(selectedHour + ":" + selectedMinute + ":00");
                            fulltime = selectedHour + ":" + selectedMinute + ":00";

//                            e_address.requestFocus();
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            }
        });

        edit_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edit_time.setText(selectedHour + ":" + selectedMinute + ":00");
                        fulltime = selectedHour + ":" + selectedMinute + ":00";

//                        e_address.requestFocus();
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(e_title.getText().toString())) {
                    e_title.setError(getResources().getString(R.string.infotitle));
                    e_title.requestFocus();
                } else if (TextUtils.isEmpty(e_description.getText().toString())) {
                    e_description.setError(getResources().getString(R.string.infodes));
                    e_description.requestFocus();
                } else if (TextUtils.isEmpty(e_address.getText().toString())) {
                    e_address.setError(getResources().getString(R.string.infoaddress));
                    e_address.requestFocus();

                } else if (TextUtils.isEmpty(edit_date.getText().toString())) {
                    edit_date.setError(getResources().getString(R.string.selectdate));
                    edit_time.requestFocus();
                } else if (TextUtils.isEmpty(edit_time.getText().toString())) {
                    edit_time.setError(getResources().getString(R.string.selecttime));
                    e_address.requestFocus();
                } else {
                    datetimefull = fulldate + " " + fulltime;
                    if (CommonMethod.isInternetConnected(EditEventActivity.this)) {
                        new EditEvent().execute();
                    } else {
                        Toast.makeText(EditEventActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setContent() {
        if (CommonMethod.isInternetConnected(EditEventActivity.this)) {
            e_title.setText(title);
            e_description.setText(description);
            e_address.setText(address);
            edit_date.setText(date);
            edit_videolink.setText(videoLink);

            Log.e("image", "---------------" + CommonURL.ImagePath + CommonAPI_Name.eventimage + photo);
            Picasso.with(EditEventActivity.this).load(CommonURL.ImagePath + CommonAPI_Name.eventimage + photo).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).into(img_category_icon);
//            new AddInformation().execute(e_title.getText().toString(), e_description.getText().toString(), e_date.getText().toString(), e_address.getText().toString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        lin_main = (LinearLayout) findViewById(R.id.lin_main);
        lin_main.setVisibility(View.GONE);
        e_title = (EditText) findViewById(R.id.e_title);
        e_description = (EditText) findViewById(R.id.e_description);
        e_address = (EditText) findViewById(R.id.e_address);
        edit_date = (EditText) findViewById(R.id.edit_date);
        edit_videolink = (EditText) findViewById(R.id.edit_videolink);
        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_photo = (TextView) findViewById(R.id.txt_photo);
        txt_audio = (TextView) findViewById(R.id.txt_audio);
        txt_video = (TextView) findViewById(R.id.txt_video);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_add = (Button) findViewById(R.id.btn_add);
        img_category_icon = (ImageView) findViewById(R.id.img_category_icon);
        img_category_icon.setVisibility(View.GONE);
        txt_header.setText("Edit Event");
        btn_add.setText("Update");

        edit_time = (EditText) findViewById(R.id.edit_time);
        textphotos = (TextView) findViewById(R.id.textphotos);
        textaudios = (TextView) findViewById(R.id.textaudios);
        textvideos = (TextView) findViewById(R.id.textvideos);
        textphotos.setVisibility(View.VISIBLE);
        textaudios.setVisibility(View.VISIBLE);
        textvideos.setVisibility(View.VISIBLE);

//        txt_photo.setVisibility(View.GONE);
//        txt_audio.setVisibility(View.GONE);
//        txt_video.setVisibility(View.GONE);

        recyclerView_photos = (RecyclerView) findViewById(R.id.recyclerView_photos);
        recyclerView_audio = (RecyclerView) findViewById(R.id.recyclerView_audio);
        recyclerView_video = (RecyclerView) findViewById(R.id.recyclerView_video);

        linearLayoutManager1 = new LinearLayoutManager(EditEventActivity.this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager2 = new LinearLayoutManager(EditEventActivity.this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager3 = new LinearLayoutManager(EditEventActivity.this);
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_photos.setLayoutManager(linearLayoutManager1);
        recyclerView_audio.setLayoutManager(linearLayoutManager2);
        recyclerView_video.setLayoutManager(linearLayoutManager3);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }

    private void setVideos() {
        photoAudioVideoItems = new ArrayList<>();
        for (int i = 0; i < videolist.size(); i++) {
            Log.e("video split", "---------------" + videolist.get(i));
            photoAudioVideoItems.add(new PhotoAudioVideoItem(videolist.get(i), "Video" + i, "Video" + i));
            videoListAdapter = new VideoAdapter(EditEventActivity.this, photoAudioVideoItems);
            recyclerView_video.setAdapter(videoListAdapter);
            recyclerView_video.scrollToPosition(photoAudioVideoItems.size() - 1);
        }
//        videoListAdapter = new VideoListAdapter(EventDetailActivity.this, videolist);
//        recycleview_slide.setAdapter(videoListAdapter);
    }

    private void setAudios() {
        photoAudioVideoItems = new ArrayList<>();
        for (int i = 0; i < audiolist.size(); i++) {
            Log.e("audio split", "---------------" + audiolist.get(i));
            photoAudioVideoItems.add(new PhotoAudioVideoItem(audiolist.get(i), "Audio" + i, "Audio" + i));
            audioListAdapter = new AudioAdapter(EditEventActivity.this, photoAudioVideoItems);
            recyclerView_audio.setAdapter(audioListAdapter);
            recyclerView_audio.scrollToPosition(photoAudioVideoItems.size() - 1);

            /*if (CommonMethod.isInternetConnected(EditEventActivity.this)) {
                new AddAudio().execute();
            } else {
                Toast.makeText(this, R.string.internet, Toast.LENGTH_SHORT).show();
            }*/
        }

//        audioListAdapter = new AudioListAdapter(EventDetailActivity.this, audiolist);
//        recycleview_slide.setAdapter(audioListAdapter);
    }

    private void setPhotos() {
//                Photo set
//        photoAudioVideoItems = new ArrayList<PhotoAudioVideoItem>();
        photoAdapter = new PhotoAdapter(EditEventActivity.this, getEventImages);
        recyclerView_photos.setAdapter(photoAdapter);
        recyclerView_photos.scrollToPosition(getEventImages.size() - 1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_photo:
                checkPermissionImage();
                break;

            case R.id.txt_audio:
                checkPermissionAudio();
                break;

            case R.id.txt_video:
                checkPermissionVideo();
                break;
        }
    }

    private void checkPermissionImage() {
        selectImage();

    }

    private void checkPermissionAudio() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!permission.checkPermissionForExternalStorage()) {
                permission.requestPermissionForExternalStorage();
            } else {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        }
    }

    private void checkPermissionVideo() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 3);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!permission.checkPermissionForExternalStorage()) {
                permission.requestPermissionForExternalStorage();
            } else {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(EditEventActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 4);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!permission.checkPermissionForCamera()) {
                            permission.requestPermissionForCamera();
                        } else {
                            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 4);
                        }
                    }


                } else if (options[item].equals("Choose from Gallery"))

                {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 1);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!permission.checkPermissionForExternalStorage()) {
                            permission.requestPermissionForExternalStorage();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 1);
                        }
                    }

                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            flag = true;
            if (requestCode == 1) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                flag = true;
                Cursor c = EditEventActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                String imagepath = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + imagepath);
//                txt_photo.setText("Selected photo : " + imagepath);

                thumbnail = (BitmapFactory.decodeFile(picturePath));




                getEventImages.add(new EventImage("img", "eid", picturePath, thumbnail, false));
                photoAdapter = new PhotoAdapter(EditEventActivity.this, getEventImages);
                recyclerView_photos.setAdapter(photoAdapter);
                recyclerView_photos.setVisibility(View.VISIBLE);
                if (CommonMethod.isInternetConnected(EditEventActivity.this)) {
                    new AddPhoto().execute();
                } else {
                    Toast.makeText(this, R.string.internet, Toast.LENGTH_SHORT).show();
                }

                img_category_icon.setVisibility(View.GONE);
                img_category_icon.setImageBitmap(thumbnail);
                //Log.w("path of image from gallery......******************.........", picturePath + "");

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Audio.Media.DATA};
                flag = true;
                Cursor c = EditEventActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                audioPath = c.getString(columnIndex);
                audiolist.add(audioPath);
                setAudios();
                recyclerView_audio.setVisibility(View.VISIBLE);
                c.close();
                if (CommonMethod.isInternetConnected(EditEventActivity.this)) {
                    new AddAudio().execute();
                } else {
                    Toast.makeText(this, R.string.internet, Toast.LENGTH_SHORT).show();
                }
                String audiopath = audioPath.substring(audioPath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + audiopath);
//                txt_audio.setText("Selected audio : " + audiopath);
                //Log.w("path of image from gallery......******************.........", picturePath + "");

            } else if (requestCode == 3) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Video.Media.DATA};
                flag = true;
                Cursor c = EditEventActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                videoPath = c.getString(columnIndex);
                videolist.add(videoPath);
                setVideos();
                recyclerView_video.setVisibility(View.VISIBLE);
                c.close();
                if (CommonMethod.isInternetConnected(EditEventActivity.this)) {
                    new AddVideo().execute();
                } else {
                    Toast.makeText(this, R.string.internet, Toast.LENGTH_SHORT).show();
                }
                String videopath = videoPath.substring(videoPath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + videopath);
//                txt_video.setText("Selected video : " + videopath);

                //Log.w("path of image from gallery......******************.........", picturePath + "");
            } else if (requestCode == 4) {
                flag = true;

                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
//                        profile_image.setImageBitmap(bitmap);
                    img_category_icon.setVisibility(View.GONE);
                    img_category_icon.setImageBitmap(bitmap);
                    picturePath = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator;
//                            + "Phoenix" + File.separator + "default";

                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(picturePath, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    picturePath = picturePath + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outFile);
                        outFile.flush();
                        outFile.close();

                        getEventImages.add(new EventImage("img", "eid", picturePath, thumbnail, false));
                        photoAdapter = new PhotoAdapter(EditEventActivity.this, getEventImages);
                        recyclerView_photos.setAdapter(photoAdapter);
                        recyclerView_photos.setVisibility(View.VISIBLE);
                        if (CommonMethod.isInternetConnected(EditEventActivity.this)) {
                            new AddPhoto().execute();
                        } else {
                            Toast.makeText(this, R.string.internet, Toast.LENGTH_SHORT).show();
                        }
                        String imagepath = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                        Log.e("result", "--------------" + imagepath);
//                        txt_photo.setText("Selected photo : " + imagepath);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Log.w("path of image from gallery......******************.........", picturePath + "");
                Log.e("picturepath", "--------------" + picturePath);
                Log.e("audioPath", "--------------" + audioPath);
                Log.e("videoPath", "--------------" + videoPath);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private class EditEvent extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditEventActivity.this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("method", "----------------" + "call");

            String title = e_title.getText().toString();
            String description = e_description.getText().toString();
            String date = edit_date.getText().toString();
            String address = e_address.getText().toString();
            String VideoLink = edit_videolink.getText().toString();

            Log.e("title", "----------" + title);
            Log.e("description", "----------" + description);
            Log.e("date", "----------" + date);
            Log.e("address", "----------" + address);
            Log.e("VideoLink", "----------" + VideoLink);


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(CommonURL.Main_url + CommonAPI_Name.editevent);
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartEntity.addPart("eid", new StringBody(id));
                multipartEntity.addPart("Title", new StringBody(title));
                multipartEntity.addPart("Description", new StringBody(description));
                multipartEntity.addPart("Date", new StringBody(datetimefull));
                multipartEntity.addPart("Address", new StringBody(address));
                multipartEntity.addPart("VideoLink", new StringBody(VideoLink));


                String[] photoArr = new String[photolist.size()];
                for (int i = 0; i < photolist.size(); i++) {
                    photoArr[i] = photolist.get(i);
                    Log.e("photoArr", "--------" + photoArr[i]);
                    multipartEntity.addPart("hiddenphoto[" + i + "]", new StringBody(photoArr[i]));

                }

                String[] audioArr = new String[audiolist.size()];
                for (int i = 0; i < audiolist.size(); i++) {
                    audioArr[i] = audiolist.get(i);
                    Log.e("audioArr", "--------" + audioArr[i]);
                    multipartEntity.addPart("hiddenaudio[" + i + "]", new StringBody(audioArr[i]));

                }


                String[] videoArr = new String[videolist.size()];
                for (int i = 0; i < videolist.size(); i++) {
                    videoArr[i] = videolist.get(i);
                    Log.e("videoArr", "--------" + videoArr[i]);
                    multipartEntity.addPart("hiddenvideo[" + i + "]", new StringBody(videoArr[i]));

                }


//                multipartEntity.addPart("hiddenphoto", new StringBody(photo));
//                multipartEntity.addPart("hiddenaudio", new StringBody(audio));
//                multipartEntity.addPart("hiddenvideo", new StringBody(video));


                /*if (picturePath == null) {
                    Log.e("if call", "-----------");
                } else {
                    Log.e(" else call", "-----------");
                    File file1 = new File(picturePath);
                    FileBody fileBody1 = new FileBody(file1);
                    multipartEntity.addPart("Photo", fileBody1);
                }
                if (audioPath == null) {
                    Log.e("if call", "-----------");
                } else {
                    File file2 = new File(audioPath);
                    FileBody fileBody2 = new FileBody(file2);
                    multipartEntity.addPart("Audio", fileBody2);
                }
                if (videoPath == null) {
                    Log.e("if call", "-----------");
                } else {
                    File file3 = new File(videoPath);
                    FileBody fileBody3 = new FileBody(file3);
                    multipartEntity.addPart("Video", fileBody3);
                }*/

                httpPost.setEntity(multipartEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);

                    responseJSON = s.toString();
                }


            } catch (Exception e) {
                Log.e("exception", "------------" + e.toString());
            }

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("response", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progressDialog.dismiss();
//                    Toast.makeText(EditEventActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(EditEventActivity.this, "Event edit successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    progressDialog.dismiss();
//                    Toast.makeText(EditEventActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(EditEventActivity.this, "Event not edit.", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();

                progressDialog.dismiss();
            }
        }
    }

    private class GetEventDetail extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditEventActivity.this);
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

                    String likecount = jsonObject.getString("likecount");
                    Log.e("like", "-----------------" + likecount);

                    photolist = new ArrayList<>();
                    audiolist = new ArrayList<>();
                    videolist = new ArrayList<>();
                    getEventImages = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        title = object.getString("Title");
                        description = object.getString("Description");
                        address = object.getString("Address");
                        String dates = object.getString("Date");
                        Log.e("dates", "-------------" + dates);
                        audio = object.getString("Audio").replaceAll(" ", "%20");

                        if (audio.equalsIgnoreCase("")) {

                        } else {
                            String[] audios = audio.split(",");
                            for (int j = 0; i < audios.length; i++) {
                                audiolist.add(audios[i]);
                            }
                        }
                        videoLink = object.getString("VideoLink");
                        video = object.getString("Video").replaceAll(" ", "%20");

                        if (video.equalsIgnoreCase("")) {

                        } else {
                            String[] videos = video.split(",");
                            for (int k = 0; k < videos.length; k++) {
                                videolist.add(videos[k]);
                            }
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
//                        date = dayOfTheWeek + ", " + day + "/" + intMonth + "/" + year + " " + string[1];
                        date = year + "-" + intMonth + "-" + day;
                        edit_date.setText(date);
                        edit_time.setText(string[1]);

                        datetimefull = date + " " + string[1];


                    }
                    setContent();
                    if (audio.equalsIgnoreCase("")) {

                    } else {
                        recyclerView_audio.setVisibility(View.VISIBLE);
                        setAudios();
                    }

                    if (video.equalsIgnoreCase("")) {

                    } else {
                        recyclerView_video.setVisibility(View.VISIBLE);
                        setVideos();
                    }
                    JSONArray jsonArray1 = jsonObject.getJSONArray("Photo");
                    photo = "null";
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject object = jsonArray1.getJSONObject(i);
                        String id = object.getString("ID");
                        String eventid = object.getString("EventID");
                        photo = object.getString("Photo");
                        photolist.add(photo);
                        String photos = object.getString("Photo");
                        getEventImages.add(new EventImage(id, eventid, photos, null, false));
//                        itemSplashArrayList.add(new ImageItemSplash(CommonURL.ImagePath + CommonAPI_Name.eventimage + photo, CommonURL.ImagePath + CommonAPI_Name.eventimage + photo, false));
                        Log.e("size", "------------------" + itemSplashArrayList.size());
//                        setPhotos();
//                        Log.e("photo","------------------"+photo);
                    }


                    Log.e("photo", "------------------" + photo);
//                    setContent();
                    if (photo.equalsIgnoreCase("null")) {

                    } else {
                        recyclerView_photos.setVisibility(View.VISIBLE);
                        setPhotos();
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

    public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {


        private final Activity activity;
        private final ArrayList<PhotoAudioVideoItem> itemArrayList;
        private String id;
        Intent intent;

        public AudioAdapter(Activity activity, ArrayList<PhotoAudioVideoItem> itemArrayList) {
            super();
            this.activity = activity;
            this.itemArrayList = itemArrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_audioitem, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            Log.e("audio", "---------------" + itemArrayList.get(position));
            final PhotoAudioVideoItem photoAudioVideoItem = itemArrayList.get(position);
//        Picasso.with(activity).load(CommonURL.AudioPath + CommonAPI_Name.eventaudio + photoAudioVideoItem.getUrl().replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).into(holder.img_item);
            holder.txt_audioname.setText(photoAudioVideoItem.getName());
            holder.img_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, AudioPlayActivity.class);
                    Log.e("audiopath", "----------------" + CommonURL.AudioPath + CommonAPI_Name.eventaudio + photoAudioVideoItem.getUrl().replaceAll(" ", "%20"));
                    intent.putExtra("audiopath", CommonURL.AudioPath + CommonAPI_Name.eventaudio + photoAudioVideoItem.getUrl().replaceAll(" ", "%20"));
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            holder.btncheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

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
//                            Toast.makeText(activity, "You clicked on YES", Toast.LENGTH_SHORT).show();
                            audioname = photoAudioVideoItem.getUrl();
                            new DeleteAudio().execute();
                            itemArrayList.remove(position);
                            notifyItemRemoved(position);
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
//                            Toast.makeText(activity, "You clicked on NO", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog.show();

                }
            });

        }

        @Override
        public int getItemCount() {
            return itemArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            ImageView img_item, img_play, btncheck;
            TextView txt_audioname;

            public ViewHolder(View itemView) {
                super(itemView);

                btncheck = (ImageView) itemView.findViewById(R.id.btncheck);
                img_item = (ImageView) itemView.findViewById(R.id.img_item);
                img_play = (ImageView) itemView.findViewById(R.id.img_play);
                txt_audioname = (TextView) itemView.findViewById(R.id.txt_audioname);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View v) {

            }

            @Override
            public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

    }

    public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {


        private final Activity activity;
        private final ArrayList<PhotoAudioVideoItem> itemArrayList;
        private String id;
        Intent intent;

        public VideoAdapter(Activity activity, ArrayList<PhotoAudioVideoItem> itemArrayList) {
            super();
            this.activity = activity;
            this.itemArrayList = itemArrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_videoitem, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            final PhotoAudioVideoItem photoAudioVideoItem = itemArrayList.get(position);
//        Log.e("video", "---------------" + itemArrayList.get(position));
//        Picasso.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.eventimage + itemArrayList.get(position).replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).into(holder.img_item);
//        Picasso.with(activity).load(CommonURL.VideoPath + CommonAPI_Name.eventvideo + photoAudioVideoItem.getUrl().replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).into(holder.img_item);
            Log.e("video", "---------------" + CommonURL.VideoPath + CommonAPI_Name.eventvideo + photoAudioVideoItem.getUrl().replaceAll(" ", "%20"));
            holder.txt_videoname.setText(photoAudioVideoItem.getName());
            holder.img_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, VideoFullActivity.class);
                    intent.putExtra("videourl", CommonURL.VideoPath + CommonAPI_Name.eventvideo + photoAudioVideoItem.getUrl().replaceAll(" ", "%20"));
                    video_play_url = CommonURL.VideoPath + CommonAPI_Name.eventvideo + photoAudioVideoItem.getUrl().replaceAll(" ", "%20");
                    Log.e("video url", "---------------" + video_play_url);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            holder.btncheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

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
//                            Toast.makeText(activity, "You clicked on YES", Toast.LENGTH_SHORT).show();
                            videoname = photoAudioVideoItem.getUrl();
                            new DeleteVideo().execute();
                            itemArrayList.remove(position);
                            notifyItemRemoved(position);
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
//                            Toast.makeText(activity, "You clicked on NO", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog.show();


                }
            });

        }

        @Override
        public int getItemCount() {
            return itemArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            ImageView img_item, img_play, btncheck;
            TextView txt_videoname;

            public ViewHolder(View itemView) {
                super(itemView);

                img_item = (ImageView) itemView.findViewById(R.id.img_item);
                img_play = (ImageView) itemView.findViewById(R.id.img_play);
                btncheck = (ImageView) itemView.findViewById(R.id.btncheck);
                txt_videoname = (TextView) itemView.findViewById(R.id.txt_videoname);

                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View v) {

            }

            @Override
            public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

    }

    public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

        private final Activity activity;
        private final ArrayList<EventImage> itemArrayList;
        private String id;
        Intent intent;

        public PhotoAdapter(Activity activity, ArrayList<EventImage> itemArrayList) {
            super();
            this.activity = activity;
            this.itemArrayList = itemArrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photoitem, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final EventImage eventImage = itemArrayList.get(position);

            if (eventImage.getBitmap() != null) {
                Log.e("images", "---------------" + eventImage.getPhoto());
                holder.img_item.setImageBitmap(eventImage.getBitmap());

            } else {
                Log.e("images", "---------------" + itemArrayList.get(position));
                Picasso.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.eventimage + eventImage.getPhoto().replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).into(holder.img_item);


              /*  holder.img_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    *//*Intent intent = new Intent(activity, SlidingGalleryImage.class);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtra("cid", "");
//                intent.putExtra("imagePath", CommonURL.ImagePath + CommonAPI_Name.eventimage + itemArrayList.get(position).replaceAll(" ", "%20"));
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*//*

                    }
                });*/


            }

            holder.btncheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(activity, "" + position, Toast.LENGTH_SHORT).show();
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

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
//                            Toast.makeText(activity, "You clicked on YES", Toast.LENGTH_SHORT).show();
                            itemArrayList.remove(position);
                            notifyItemRemoved(position);
                            imagename = eventImage.getPhoto();
                            new DeletePhoto().execute();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
//                            Toast.makeText(activity, "You clicked on NO", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog.show();

                }
            });


        }

        @Override
        public int getItemCount() {
            return itemArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            ImageView img_item, img_play;
            ImageView btncheck;

            public ViewHolder(View itemView) {
                super(itemView);

                img_item = (ImageView) itemView.findViewById(R.id.img_item);
                img_play = (ImageView) itemView.findViewById(R.id.img_play);
                btncheck = (ImageView) itemView.findViewById(R.id.btncheck);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);

            }

            @Override
            public void onClick(View v) {

            }

            @Override
            public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        private void selectImage() {
            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


            AlertDialog.Builder builder = new AlertDialog.Builder(EditEventActivity.this);

            builder.setTitle("Add Photo!");

            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int item) {

                    if (options[item].equals("Take Photo"))

                    {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 4);
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!permission.checkPermissionForCamera()) {
                                permission.requestPermissionForCamera();
                            } else {
                                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                                startActivityForResult(intent, 4);
                            }
                        }


                    } else if (options[item].equals("Choose from Gallery"))

                    {

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(intent, 1);
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!permission.checkPermissionForExternalStorage()) {
                                permission.requestPermissionForExternalStorage();
                            } else {
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                startActivityForResult(intent, 1);
                            }
                        }


                    } else if (options[item].equals("Cancel")) {

                        dialog.dismiss();

                    }

                }

            });

            builder.show();


        }

    }

    private void openDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
//        mHour=c.get(Calendar.HOUR);
//        mMinute=c.get(Calendar.MINUTE);
        //launch datepicker modal
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditEventActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.e("Date---", "DATE SELECTED " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                        fulldate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        fulldate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        edit_date.setText(fulldate);

                        edit_time.requestFocus();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    //Add photo
    private class AddPhoto extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditEventActivity.this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("method", "----------------" + "call");

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.grapes-solutions.com/vimalsagarji/event/addmultiphoto/");
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                Log.e("eid", "--------------" + id);
                Log.e("picturepath", "--------------" + picturePath);
                multipartEntity.addPart("eid", new StringBody(id));
                Log.e(" else call", "-----------");
                File file1 = new File(picturePath);
                FileBody fileBody1 = new FileBody(file1);
                multipartEntity.addPart("Photo", fileBody1);


                httpPost.setEntity(multipartEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                    responseJSON = s.toString();
                }

            } catch (Exception e) {
                Log.e("exception", "------------" + e.toString());
            }

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progressDialog.dismiss();
                    e_title.setText("");
                    Toast.makeText(EditEventActivity.this, "Photo added successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddEventActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                    finish();
                    new GetEventDetail().execute();
//                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(EditEventActivity.this, "Photo not added.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddEventActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    //Add audio
    private class AddAudio extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditEventActivity.this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("method", "----------------" + "call");

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.grapes-solutions.com/vimalsagarji/event/addmultiaudio/");
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartEntity.addPart("eid", new StringBody(id));

                if (audioPath == null) {
                    Log.e("if call", "-----------");
                } else {
                    Log.e(" else call", "-----------");
                    File file1 = new File(audioPath);
                    FileBody fileBody1 = new FileBody(file1);
                    multipartEntity.addPart("Audio", fileBody1);
                }


                httpPost.setEntity(multipartEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);

                    responseJSON = s.toString();
                }


            } catch (Exception e) {
                Log.e("exception", "------------" + e.toString());
            }

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progressDialog.dismiss();
                    e_title.setText("");
                    Toast.makeText(EditEventActivity.this, "Audio added successfully.", Toast.LENGTH_SHORT).show();
                    new GetEventDetail().execute();
//                    Toast.makeText(AddEventActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                    finish();
//                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(EditEventActivity.this, "Audio not added.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddEventActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    //Add video
    private class AddVideo extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditEventActivity.this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("method", "----------------" + "call");

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://www.grapes-solutions.com/vimalsagarji/event/addmultivideo/");
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartEntity.addPart("eid", new StringBody(id));
                if (videoPath == null) {
                    Log.e("if call", "-----------");
                } else {
                    Log.e(" else call", "-----------");
                    File file1 = new File(videoPath);
                    FileBody fileBody1 = new FileBody(file1);
                    multipartEntity.addPart("Video", fileBody1);
                }


                httpPost.setEntity(multipartEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);

                    responseJSON = s.toString();
                }


            } catch (Exception e) {
                Log.e("exception", "------------" + e.toString());
            }

            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progressDialog.dismiss();
                    e_title.setText("");
                    Toast.makeText(EditEventActivity.this, "Video added successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddEventActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    new GetEventDetail().execute();
//                    finish();
//                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(EditEventActivity.this, "Video not added.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddEventActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    private class DeletePhoto extends AsyncTask<String, Void, String> {
        String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseString = JsonParser.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/event/deletephoto/?eid=" + id + "&EventPhoto=" + imagename);
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
        }
    }

    private class DeleteAudio extends AsyncTask<String, Void, String> {

        String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            responseString = JsonParser.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/event/deleteaudio/?eid=" + id + "&EventAudio=" + audioname);
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);

        }


    }

    private class DeleteVideo extends AsyncTask<String, Void, String> {
        String responseString = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseString = JsonParser.getStringResponse("http://www.grapes-solutions.com/vimalsagarji/event/deletevideo/?eid=" + id + "&EventVideo=" + videoname);
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);

        }

    }

}
