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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.util.MarshMallowPermission;

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

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntity;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.entity.mime.content.StringBody;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;

/**
 * Created by Grapes-Pradip on 2/16/2017.
 */

@SuppressWarnings("ALL")
public class AddEventActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private EditText e_title;
    private EditText e_description;
    private EditText e_address;
    private EditText edit_date;
    private EditText edit_time;
    private EditText edit_videolink;
    private TextView txt_header;
    private TextView txt_photo;
    private TextView txt_audio;
    private TextView txt_video;
    private ImageView img_back;
    private Button btn_add;
    private int mHour;
    private int mMinute;
    private String fulldate;
    private Intent intent;
    private String picturePath = null;
    private String audioPath = null;
    private String videoPath = null;
    private boolean flag = false;
    private MarshMallowPermission permission;
    private ImageView img_category_icon;
    Bitmap thumbnail;
    Bitmap thumbnail1;
    private Switch notificationswitch;
    private RecyclerView recyclerView_photos, recyclerView_audio, recyclerView_video;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager, linearLayoutManager2, linearLayoutManager3;
    ArrayList<String> photoarray = new ArrayList<>();
    ArrayList<String> audioarray = new ArrayList<>();
    ArrayList<String> videoarray = new ArrayList<>();
    String datetimefull;
    String fulltime;
    String notify = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_event);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        permission = new MarshMallowPermission(this);
        findID();
//        edit_date.setVisibility(View.GONE);


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
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    openDatePicker();
                }
            }
        });

        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
                    mTimePicker = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                mTimePicker = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
        });




        idClick();

    }

    private void idClick() {
        txt_photo.setOnClickListener(this);
        txt_audio.setOnClickListener(this);
        txt_video.setOnClickListener(this);
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
                    if (CommonMethod.isInternetConnected(AddEventActivity.this)) {
                        datetimefull = fulldate + " " + fulltime;
                        new AddEvent().execute();
                    } else {
                        Toast.makeText(AddEventActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void findID() {
        gridLayoutManager = new GridLayoutManager(AddEventActivity.this, 1);
        linearLayoutManager = new LinearLayoutManager(AddEventActivity.this);
        linearLayoutManager2 = new LinearLayoutManager(AddEventActivity.this);
        linearLayoutManager3 = new LinearLayoutManager(AddEventActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_photos = (RecyclerView) findViewById(R.id.recyclerView_photos);
        recyclerView_audio = (RecyclerView) findViewById(R.id.recyclerView_audio);
        recyclerView_video = (RecyclerView) findViewById(R.id.recyclerView_video);
//        recyclerView_photos.setLayoutManager(gridLayoutManager);
        recyclerView_photos.setLayoutManager(linearLayoutManager);
        recyclerView_audio.setLayoutManager(linearLayoutManager2);
        recyclerView_video.setLayoutManager(linearLayoutManager3);
        e_title = (EditText) findViewById(R.id.e_title);
        e_description = (EditText) findViewById(R.id.e_description);
        e_address = (EditText) findViewById(R.id.e_address);
        edit_date = (EditText) findViewById(R.id.edit_date);
        edit_time = (EditText) findViewById(R.id.edit_time);
        edit_videolink = (EditText) findViewById(R.id.edit_videolink);
        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_photo = (TextView) findViewById(R.id.txt_photo);
        txt_audio = (TextView) findViewById(R.id.txt_audio);
        txt_video = (TextView) findViewById(R.id.txt_video);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_add = (Button) findViewById(R.id.btn_add);
        img_category_icon = (ImageView) findViewById(R.id.img_category_icon);
        txt_header.setText("Add Event");
        notificationswitch = (Switch) findViewById(R.id.notificationswitch);
        notificationswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notify = "0";
                    Log.e("checked", "----------------" + isChecked);
                } else {
                    notify = "1";
                    Log.e("checked", "----------------" + isChecked);

                }
            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
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

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        startActivityForResult(intent, 4);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!permission.checkPermissionForCamera()) {
                            permission.requestPermissionForCamera();
                        } else {
                            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 4);
                        }
                    }


                } else if (options[item].equals("Choose from Gallery")) {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 1);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!permission.checkPermissionForExternalStorage()) {
                            permission.requestPermissionForExternalStorage();
                        } else {
                            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

    private void checkPermissionImage() {
        selectImage();

    }

    private void checkPermissionAudio() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!permission.checkPermissionForExternalStorage()) {
                permission.requestPermissionForExternalStorage();
            } else {
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        }
    }

    private void checkPermissionVideo() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 3);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!permission.checkPermissionForExternalStorage()) {
                permission.requestPermissionForExternalStorage();
            } else {
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.e("Date---", "DATE SELECTED " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                        fulldate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        fulldate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        edit_date.setText(fulldate);

//                        edit_time.requestFocus();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
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
                Cursor c = AddEventActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);


                Log.e("photo array", "------------" + photoarray);

                c.close();
                String imagepath = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + imagepath);
//                txt_photo.setText("Selected photo : " + imagepath);

                try {
                    decodeFile(picturePath);
                    photoarray.add(picturePath);
                    PhotoAdapter photoAdapter = new PhotoAdapter(AddEventActivity.this, photoarray);
                    recyclerView_photos.setVisibility(View.VISIBLE);
                    recyclerView_photos.setAdapter(photoAdapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                /*thumbnail = (BitmapFactory.decodeFile(picturePath));
                img_category_icon.setVisibility(View.VISIBLE);
                img_category_icon.setImageBitmap(thumbnail);*/

                Log.e("picturepath", "--------------" + picturePath);
                Log.e("audioPath", "--------------" + audioPath);
                Log.e("videoPath", "--------------" + videoPath);

                //Log.w("path of image from gallery......******************.........", picturePath + "");
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Audio.Media.DATA};
                flag = true;
                Cursor c = AddEventActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                audioPath = c.getString(columnIndex);
                audioarray.add(audioPath);
                AudioAdapter audioAdapter = new AudioAdapter(AddEventActivity.this, audioarray);
                recyclerView_audio.setVisibility(View.VISIBLE);
                recyclerView_audio.setAdapter(audioAdapter);

                Log.e("photo array", "------------" + audioarray);


                c.close();

                String audiopath = audioPath.substring(audioPath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + audiopath);
//                txt_audio.setText("Selected audio : " + audiopath);


                //Log.w("path of image from gallery......******************.........", picturePath + "");
                Log.e("picturepath", "--------------" + picturePath);
                Log.e("audioPath", "--------------" + audioPath);
                Log.e("videoPath", "--------------" + videoPath);
            } else if (requestCode == 3) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Video.Media.DATA};
                flag = true;
                Cursor c = AddEventActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                videoPath = c.getString(columnIndex);
                videoarray.add(videoPath);
                VideoAdapter videoAdapter = new VideoAdapter(AddEventActivity.this, videoarray);
                recyclerView_video.setVisibility(View.VISIBLE);
                recyclerView_video.setAdapter(videoAdapter);

                Log.e("photo array", "------------" + videoarray);

                c.close();

                String videopath = videoPath.substring(videoPath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + videopath);
//                txt_video.setText("Selected video : " + videopath);
                //Log.w("path of image from gallery......******************.........", picturePath + "");
                Log.e("picturepath", "--------------" + picturePath);
                Log.e("audioPath", "--------------" + audioPath);
                Log.e("videoPath", "--------------" + videoPath);
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
                    img_category_icon.setVisibility(View.VISIBLE);
                    img_category_icon.setImageBitmap(bitmap);
                    picturePath = android.os.Environment
                            .getExternalStorageDirectory() + "/VimalsagarjiImage"
                            + File.separator;


//                            + "Phoenix" + File.separator + "default";

                    f.delete();
                    OutputStream outFile = null;

                    File root = android.os.Environment.getExternalStorageDirectory();
                    File dir = new File(root.getAbsolutePath() + "/VimalsagarjiImage" + File.separator);
                    dir.mkdirs();
                    String pic = CommonMethod.getRandomString(30);
                    File file = new File(dir, String.valueOf(pic + ".jpg"));
                    picturePath = picturePath + String.valueOf(pic) + ".jpg";

                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outFile);
                        outFile.flush();
                        outFile.close();
                        photoarray.add(picturePath);
                        PhotoAdapter photoAdapter = new PhotoAdapter(AddEventActivity.this, photoarray);
                        recyclerView_photos.setVisibility(View.VISIBLE);
                        recyclerView_photos.setAdapter(photoAdapter);
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

    private class AddEvent extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddEventActivity.this);
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
            String Audio = "no audio";
            String Video = "no video";
            String Photo = "no photo";
            String Videolink = "";


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(CommonURL.Main_url + CommonAPI_Name.addevent);
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartEntity.addPart("Title", new StringBody(title));
                multipartEntity.addPart("Description", new StringBody(description));
                multipartEntity.addPart("Date", new StringBody(datetimefull));
                multipartEntity.addPart("Address", new StringBody(address));
                multipartEntity.addPart("VideoLink", new StringBody(VideoLink));

                if (picturePath == null) {
                    Log.e("if call", "-----------");
                } else {

                    String[] photoArr = new String[photoarray.size()];
                    for (int i = 0; i < photoarray.size(); i++) {
                        photoArr[i] = photoarray.get(i);
                        Log.e("photoArr", "--------" + photoArr[i]);
                        File file1 = new File(photoArr[i]);
                        FileBody fileBody1 = new FileBody(file1);
                        multipartEntity.addPart("Photo[" + i + "]", fileBody1);

                    }
                    /*Log.e(" else call", "-----------");
                    File file1 = new File(picturePath);
                    FileBody fileBody1 = new FileBody(file1);
                    multipartEntity.addPart("Photo", fileBody1);*/
                }
                if (audioPath == null) {
                    Log.e("if call", "-----------");
                } else {

                    String[] audioArr = new String[audioarray.size()];
                    for (int i = 0; i < audioarray.size(); i++) {
                        audioArr[i] = audioarray.get(i);
                        Log.e("photoArr", "--------" + audioArr[i]);
                        File file2 = new File(audioArr[i]);
                        FileBody fileBody2 = new FileBody(file2);
                        multipartEntity.addPart("Audio[" + i + "]", fileBody2);

                    }
/*
                    File file2 = new File(audioPath);
                    FileBody fileBody2 = new FileBody(file2);
                    multipartEntity.addPart("Audio", fileBody2);
*/

                }
                if (videoPath == null) {
                    Log.e("if call", "-----------");
                } else {

                    String[] videoArr = new String[videoarray.size()];
                    for (int i = 0; i < videoarray.size(); i++) {
                        videoArr[i] = videoarray.get(i);
                        Log.e("photoArr", "--------" + videoArr[i]);
                        File file3 = new File(videoArr[i]);
                        FileBody fileBody3 = new FileBody(file3);
                        multipartEntity.addPart("Video[" + i + "]", fileBody3);
                    }

                    /*File file3 = new File(videoPath);
                    FileBody fileBody3 = new FileBody(file3);
                    multipartEntity.addPart("Video", fileBody3);*/
                }
                multipartEntity.addPart("Is_notify", new StringBody(notify));

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
                    e_title.setText("");
                    Toast.makeText(AddEventActivity.this, "Event added successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddEventActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(AddEventActivity.this, "Event not added.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddEventActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    //Photo Adapter
    public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {


        private final Activity activity;
        private final ArrayList<String> itemArrayList;
        private String id;
        Bitmap thumb;

        public PhotoAdapter(Activity activity, ArrayList<String> itemArrayList) {
            super();
            this.activity = activity;
            this.itemArrayList = itemArrayList;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_item, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int i) {


            String path = itemArrayList.get(i);
            thumb = (BitmapFactory.decodeFile(path));
            Log.e("adapetr path", "----------" + itemArrayList.get(i));
            Log.e("thumbnail", "--------------------" + thumb);
            holder.photo.setImageBitmap(thumb);

            holder.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    PopupMenu popup = new PopupMenu(AddEventActivity.this, holder.photo);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(AddEventActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                }

            });

        }

        @Override
        public int getItemCount() {

            return itemArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            ImageView photo;

            public ViewHolder(View itemView) {
                super(itemView);
                photo = (ImageView) itemView.findViewById(R.id.photo_scroll);


            }

            @Override
            public void onClick(View v) {

            }

            @Override
            public boolean onLongClick(View v) {
                return false;
            }

        }

    }

    //Audio Adapter
    public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {


        private final Activity activity;
        private final ArrayList<String> itemArrayList;
        private String id;
        Bitmap thumb;

        public AudioAdapter(Activity activity, ArrayList<String> itemArrayList) {
            super();
            this.activity = activity;
            this.itemArrayList = itemArrayList;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_item, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int i) {


            String path = itemArrayList.get(i);
            thumb = (BitmapFactory.decodeFile(path));
            Log.e("adapetr path", "----------" + itemArrayList.get(i));
            Log.e("thumbnail", "--------------------" + thumb);
            holder.photo.setImageResource(R.drawable.ic_audiotrack);

            holder.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    PopupMenu popup = new PopupMenu(AddEventActivity.this, holder.photo);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(AddEventActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                }

            });

        }

        @Override
        public int getItemCount() {

            return itemArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            ImageView photo;

            public ViewHolder(View itemView) {
                super(itemView);
                photo = (ImageView) itemView.findViewById(R.id.photo_scroll);


            }

            @Override
            public void onClick(View v) {

            }

            @Override
            public boolean onLongClick(View v) {
                return false;
            }

        }

    }


    //Video Adapter
    public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {


        private final Activity activity;
        private final ArrayList<String> itemArrayList;
        private String id;
        Bitmap thumb;

        public VideoAdapter(Activity activity, ArrayList<String> itemArrayList) {
            super();
            this.activity = activity;
            this.itemArrayList = itemArrayList;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_item, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int i) {


            String path = itemArrayList.get(i);
            thumb = (BitmapFactory.decodeFile(path));
            Log.e("adapetr path", "----------" + itemArrayList.get(i));
            Log.e("thumbnail", "--------------------" + thumb);
            holder.photo.setImageResource(R.drawable.ic_ondemand_video);

            holder.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    PopupMenu popup = new PopupMenu(AddEventActivity.this, holder.photo);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(AddEventActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    popup.show();//showing popup menu
                }

            });

        }

        @Override
        public int getItemCount() {

            return itemArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            ImageView photo;

            public ViewHolder(View itemView) {
                super(itemView);
                photo = (ImageView) itemView.findViewById(R.id.photo_scroll);


            }

            @Override
            public void onClick(View v) {

            }

            @Override
            public boolean onLongClick(View v) {
                return false;
            }

        }

    }

    public void decodeFile(String filePath) throws IOException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        thumbnail = BitmapFactory.decodeFile(filePath, o2);

        img_category_icon.setVisibility(View.VISIBLE);
        img_category_icon.setImageBitmap(thumbnail);
        OutputStream outFile = null;
        File file=new File(picturePath);
        outFile = new FileOutputStream(file);
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 40, outFile);
        outFile.flush();
        outFile.close();
    }


}
