package com.example.grapes_pradip.vimalsagaradmin.activities.video;

import android.annotation.SuppressLint;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

/**
 * Created by Grapes-Pradip on 2/16/2017.
 */

@SuppressWarnings("ALL")
public class EditVideoActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private EditText edit_category_name;
    private EditText edit_video_name;
    private TextView txt_header;
    private TextView txt_photo;
    private TextView txt_video;
    private ImageView img_back;
    private ImageView img_category_icon;
    private Button btn_add;
    private Intent intent;
    private String picturePath;
    private String videoPath;
    private boolean flag = false;
    private Bitmap thumbnail;
    private String vid;
    private String videoname;
    private String cid;
    private String video;
    private String photo;
    private String duration;
    private String date;
    private String categoryname;
    private String description;
    private String videolink;
    private String isodate;
    private MarshMallowPermission permission;
    LinearLayout lin_noti;

    private EditText edit_date;
    private EditText edit_time;
    String datetimefull;
    String fulltime;
    String fulldate;

    String finaldate;
    String finaltime;
    private EditText e_description,edit_videolink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_video);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        permission = new MarshMallowPermission(this);
        Intent intent = getIntent();
        vid = intent.getStringExtra("vid");
        videoname = intent.getStringExtra("videoname");
        cid = intent.getStringExtra("cid");
        video = intent.getStringExtra("video");
        photo = intent.getStringExtra("photo");
        duration = intent.getStringExtra("duration");
        date = intent.getStringExtra("date");
        categoryname = intent.getStringExtra("categoryname");
        description = intent.getStringExtra("description");
        videolink = intent.getStringExtra("videolink");
        isodate = intent.getStringExtra("isodate");

        Log.e("date","------------"+date);
        String[] datesplit=date.split(",");
        Log.e("dates","----------"+datesplit[1].replaceAll("/","-"));
        Log.e("times","----------"+datesplit[2]);

        String[] newdate=datesplit[1].replaceAll("/","-").split("-");
        Log.e("day","---------"+newdate[0]);
        Log.e("month","---------"+newdate[1]);
        Log.e("year","---------"+newdate[2]);

        finaldate=newdate[2]+"-"+newdate[1]+"-"+newdate[0];
        finaltime=datesplit[2];
        Log.e("new date","--------------------"+newdate);


        findID();

        String[] string = isodate.split(" ");
        Log.e("str1", "--------" + string[0]);
        Log.e("str2", "--------" + string[1]);

        Date dt = CommonMethod.convert_date(isodate);
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

        idClick();
        setContent();

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

//        edit_date.setText(finaldate);
//        edit_time.setText(finaltime);
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
                    mTimePicker = new TimePickerDialog(EditVideoActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                mTimePicker = new TimePickerDialog(EditVideoActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

    }

    private void idClick() {
        txt_photo.setOnClickListener(this);
        txt_video.setOnClickListener(this);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edit_video_name.getText().toString())) {
                    edit_video_name.setError(getResources().getString(R.string.videoenter));
                    edit_video_name.requestFocus();
                } else {
                    datetimefull = edit_date.getText().toString() + " " + edit_time.getText().toString();
                    if (CommonMethod.isInternetConnected(EditVideoActivity.this)) {
                        new EditVideo().execute();
                    } else {
                        Toast.makeText(EditVideoActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }


    private void setContent() {
        edit_video_name.setText(CommonMethod.decodeEmoji(videoname));
        edit_category_name.setText(CommonMethod.decodeEmoji(categoryname));
        e_description.setText(CommonMethod.decodeEmoji(description));
        edit_videolink.setText(CommonMethod.decodeEmoji(videolink));

        img_category_icon.setVisibility(View.VISIBLE);
//        Picasso.with(EditVideoActivity.this).load(CommonURL.ImagePath + CommonAPI_Name.videoimage + photo.replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).resize(0, 200).into(img_category_icon);

        Glide.with(EditVideoActivity.this).load(CommonURL.ImagePath + CommonAPI_Name.videoimage + photo
                .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loading_bar).dontAnimate().into(img_category_icon);

        if (CommonMethod.isInternetConnected(EditVideoActivity.this)) {
//            new AddInformation().execute(e_title.getText().toString(), e_description.getText().toString(), e_date.getText().toString(), e_address.getText().toString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        lin_noti = (LinearLayout) findViewById(R.id.lin_noti);
        lin_noti.setVisibility(View.GONE);
        edit_category_name = (EditText) findViewById(R.id.edit_category_name);
        edit_video_name = (EditText) findViewById(R.id.edit_video_name);
        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_photo = (TextView) findViewById(R.id.txt_photo);
        txt_video = (TextView) findViewById(R.id.txt_video);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_category_icon = (ImageView) findViewById(R.id.img_category_icon);
        btn_add = (Button) findViewById(R.id.btn_add);
        txt_header.setText("Edit Video");
        btn_add.setText("Update");
        edit_time = (EditText) findViewById(R.id.edit_time);
        edit_date = (EditText) findViewById(R.id.edit_date);
        e_description = (EditText) findViewById(R.id.e_description);
        edit_videolink = (EditText) findViewById(R.id.edit_videolink);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContent();
            }
        });
    }

    private void openDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
//        mHour=c.get(Calendar.HOUR);
//        mMinute=c.get(Calendar.MINUTE);
        //launch datepicker modal
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditVideoActivity.this,
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_photo:
                checkPermissionImage();
                break;
            case R.id.txt_video:
                checkPermissionVideo();
                break;

        }
    }

    private void checkPermissionImage() {
        selectImage();

    }

    private void checkPermissionVideo() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!permission.checkPermissionForExternalStorage()) {
                permission.requestPermissionForExternalStorage();
            } else {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(EditVideoActivity.this);

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

                        startActivityForResult(intent, 3);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!permission.checkPermissionForCamera()) {
                            permission.requestPermissionForCamera();
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                            startActivityForResult(intent, 3);
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
                Cursor c = EditVideoActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                String imagepath = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + imagepath);
                txt_photo.setText("Selected photo : " + imagepath);
                //Log.w("path of image from gallery......******************.........", picturePath + "");
                try {
                    decodeFile(picturePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*thumbnail = (BitmapFactory.decodeFile(picturePath));
                //Log.w("path of image from gallery......******************.........", picturePath + "");
                img_category_icon.setVisibility(View.VISIBLE);
                img_category_icon.setImageBitmap(thumbnail);*/
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Video.Media.DATA};
                flag = true;
                Cursor c = EditVideoActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                videoPath = c.getString(columnIndex);
                c.close();
                String videopath = videoPath.substring(videoPath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + videopath);
                txt_video.setText("Selected video : " + videopath);
                //Log.w("path of image from gallery......******************.........", picturePath + "");
            } else if (requestCode == 3) {
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
                        String imagepath = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                        Log.e("result", "--------------" + imagepath);
                        txt_photo.setText("Selected photo : " + imagepath);

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

    private class EditVideo extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditVideoActivity.this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("method", "----------------" + "call");

            String categoryname = CommonMethod.encodeEmoji(edit_category_name.getText().toString());
            String videoname = CommonMethod.encodeEmoji(edit_video_name.getText().toString());

            Log.e("categoryname", "----------" + categoryname);
            String Photo = "";


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(CommonURL.Main_url + CommonAPI_Name.updatevideo);
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartEntity.addPart("VideoName", new StringBody(videoname));
                multipartEntity.addPart("cid", new StringBody(cid));
                multipartEntity.addPart("vid", new StringBody(vid));
                multipartEntity.addPart("duration", new StringBody("5:00"));
                multipartEntity.addPart("hiddenphoto", new StringBody(photo));

                multipartEntity.addPart("videodate", new StringBody(datetimefull));
                multipartEntity.addPart("Description", new StringBody(CommonMethod.encodeEmoji(e_description.getText().toString())));
                multipartEntity.addPart("VideoLink", new StringBody(CommonMethod.encodeEmoji(edit_videolink.getText().toString())));


                if (!video.equalsIgnoreCase("")){
                    multipartEntity.addPart("hiddenvideo", new StringBody(video));
                }

                if (picturePath == null) {
                    Log.e("if call", "-----------");
                } else {
                    Log.e(" else call", "-----------");
                    File file1 = new File(picturePath);
                    FileBody fileBody1 = new FileBody(file1);
                    multipartEntity.addPart("Photo", fileBody1);
                }
                if (videoPath == null) {
                    Log.e("if call", "-----------");
                } else {
                    File file2 = new File(videoPath);
                    FileBody fileBody2 = new FileBody(file2);
                    multipartEntity.addPart("Video", fileBody2);
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

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-----------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(EditVideoActivity.this, "Video edit successfully.", Toast.LENGTH_SHORT).show();
                    edit_category_name.setText("");
                    img_category_icon.setVisibility(View.GONE);
                    txt_photo.setText("Select Photo");
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(EditVideoActivity.this, "Video not edit.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
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
        File file = new File(picturePath);
        outFile = new FileOutputStream(file);
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 40, outFile);
        outFile.flush();
        outFile.close();
    }

}
