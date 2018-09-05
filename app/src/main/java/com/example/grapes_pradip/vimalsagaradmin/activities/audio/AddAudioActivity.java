package com.example.grapes_pradip.vimalsagaradmin.activities.audio;

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
import com.example.grapes_pradip.vimalsagaradmin.common.FilePath;
import com.example.grapes_pradip.vimalsagaradmin.util.MarshMallowPermission;
import com.kaopiz.kprogresshud.KProgressHUD;

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

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntity;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.entity.mime.content.StringBody;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;

@SuppressWarnings("ALL")
public class AddAudioActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private EditText edit_category_name;
    private EditText edit_audio_name;
    private TextView txt_header;
    private TextView txt_photo;
    private TextView txt_audio;
    private ImageView img_back;
    private ImageView img_category_icon;
    private Button btn_add;
    private Intent intent;
    private String picturePath = null;
    private String audioPath = null;
    private Bitmap thumbnail;
    private String cid;
    private String categoryname;
    private MarshMallowPermission permission;
    long totalSize = 0;
    KProgressHUD hud;
    private Switch notificationswitch;
    String notify = "0";

    private EditText edit_date, edit_time;
    String fulltime;
    private String fulldate;
    String datetimefull;
    private EditText e_description;

    private String selectedFilePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_audio);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        permission = new MarshMallowPermission(this);
        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");
        categoryname = intent.getStringExtra("categoryname");
        findID();
        idClick();

        edit_date.setCursorVisible(false);
        edit_date.setFocusableInTouchMode(false);
//        edit_date.setFocusable(false);

        edit_time.setCursorVisible(false);
        edit_time.setFocusableInTouchMode(false);

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
                    mTimePicker = new TimePickerDialog(AddAudioActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                mTimePicker = new TimePickerDialog(AddAudioActivity.this, new TimePickerDialog.OnTimeSetListener() {
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


    }

    private void openDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
//        mHour=c.get(Calendar.HOUR);
//        mMinute=c.get(Calendar.MINUTE);
        //launch datepicker modal
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddAudioActivity.this,
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

    private void idClick() {
        txt_photo.setOnClickListener(this);
        txt_audio.setOnClickListener(this);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_audio_name.getText().toString())) {
                    edit_audio_name.setError(getResources().getString(R.string.addaudio));
                    edit_audio_name.requestFocus();
                } else if (picturePath == null) {
                    Toast.makeText(AddAudioActivity.this, R.string.addaudiophoto, Toast.LENGTH_SHORT).show();
                } else if (selectedFilePath == null) {
                    Toast.makeText(AddAudioActivity.this, R.string.uploadaudio, Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(edit_date.getText().toString())) {
                    edit_date.setError(getResources().getString(R.string.selectdate));
                    edit_audio_name.requestFocus();
                } else if (TextUtils.isEmpty(edit_time.getText().toString())) {
                    edit_time.setError(getResources().getString(R.string.selecttime));
                    edit_audio_name.requestFocus();
                } else if (TextUtils.isEmpty(e_description.getText().toString())) {
                    e_description.setError("Please enter description.");
                    e_description.requestFocus();
                } else {
                    if (CommonMethod.isInternetConnected(AddAudioActivity.this)) {
                        datetimefull = fulldate + " " + fulltime;
                        new AddAudio().execute();
                    } else {
                        Toast.makeText(AddAudioActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void setContent() {
        if (CommonMethod.isInternetConnected(AddAudioActivity.this)) {
//            new AddInformation().execute(e_title.getText().toString(), e_description.getText().toString(), e_date.getText().toString(), e_address.getText().toString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        edit_date = (EditText) findViewById(R.id.edit_date);
        edit_time = (EditText) findViewById(R.id.edit_time);
        e_description = (EditText) findViewById(R.id.e_description);
        edit_category_name = (EditText) findViewById(R.id.edit_category_name);
        edit_audio_name = (EditText) findViewById(R.id.edit_audio_name);
        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_photo = (TextView) findViewById(R.id.txt_photo);
        txt_audio = (TextView) findViewById(R.id.txt_audio);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_category_icon = (ImageView) findViewById(R.id.img_category_icon);
        btn_add = (Button) findViewById(R.id.btn_add);
        txt_header.setText("Add Audio");
        edit_category_name.setText(CommonMethod.decodeEmoji(categoryname));
        notificationswitch = (Switch) findViewById(R.id.notificationswitch);

        notificationswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.e("checked", "----------------" + isChecked);
                    notify = "0";
                } else {
                    Log.e("checked", "----------------" + isChecked);
                    notify = "1";
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

        }
    }

    private void checkPermissionImage() {
        selectImage();
    }

    private void checkPermissionAudio() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//
//            intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(intent, 2);
//
////
////            Intent intent = new Intent()
////                    .setType("*/*")
////                    .setAction(Intent.ACTION_GET_CONTENT);
////
////            startActivityForResult(Intent.createChooser(intent, "Select a file"), 2);
//
//
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!permission.checkPermissionForExternalStorage()) {
                permission.requestPermissionForExternalStorage();
            } else {
                Intent intent = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 2);
//                intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 2);
            }

    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddAudioActivity.this);

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

                } else if (options[item].equals("Choose from Gallery")) {

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
            if (requestCode == 1) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = AddAudioActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                String imagepath = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + imagepath);
                txt_photo.setText("Selected photo : " + imagepath);
                try {
                    decodeFile(picturePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Log.w("path of image from gallery......******************.........", picturePath + "");
//                thumbnail = (BitmapFactory.decodeFile(picturePath));
                //Log.w("path of image from gallery......******************.........", picturePath + "");
//                img_category_icon.setVisibility(View.VISIBLE);
//                img_category_icon.setImageBitmap(thumbnail);

            } else if (requestCode == 2) {


                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this,selectedFileUri);
                Log.e("Selected File Path:","-------------------" + selectedFilePath);


                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    txt_audio.setText(selectedFilePath);
                }else{
                    Toast.makeText(this,"Cannot upload file to server",Toast.LENGTH_SHORT).show();
                }

//                selectedFilePath = FilePath.getPath(this,selectedFileUri);





                /*Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Audio.Media.DATA};
                Cursor c = AddAudioActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                audioPath = c.getString(columnIndex);
                c.close();
                String audiopath = audioPath.substring(audioPath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + audiopath);
                txt_audio.setText("Selected audio : " + audiopath);*/
                //Log.w("path of image from gallery......******************.........", picturePath + "");
            } else if (requestCode == 3) {

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
                Log.e("audioPath", "--------------" + audioPath);
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
        File file = new File(picturePath);
        outFile = new FileOutputStream(file);
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 40, outFile);
        outFile.flush();
        outFile.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

   /* private void simulateProgressUpdate() {
        hud.setMaxProgress(100);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            int currentProgress;

            @Override
            public void run() {
                currentProgress += 1;
                hud.setProgress(currentProgress);
                if (currentProgress == 80) {
                    hud.setLabel("Almost finish...");
                }
                if (currentProgress < 100) {
                    handler.postDelayed(this, 50);
                }
            }
        }, 100);
    }*/

    private class AddAudio extends AsyncTask<String, Integer, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(AddAudioActivity.this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);
//            hud = KProgressHUD.create(AddAudioActivity.this)
//                    .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
//                    .setLabel("Please wait")
//                    .setDetailsLabel("Downloading data");
//            hud.show();
//            simulateProgressUpdate();

        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//            Log.e("progrss","----------"+values+" %");
//            Toast.makeText(getApplicationContext(),""+values,Toast.LENGTH_SHORT).show();
//        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("method", "----------------" + "call");

            String categoryname = edit_category_name.getText().toString();
            String audioname = CommonMethod.encodeEmoji(edit_audio_name.getText().toString());

            Log.e("categoryname", "----------" + categoryname);
            String Photo = "";


            try {
                int i = 0;
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(CommonURL.Main_url + CommonAPI_Name.addaudio);

//                AndroidMultiPartEntity entity=new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
//                    @Override
//                    public void transferred(long num) {
//                        publishProgress((int) ((num / (float) totalSize) * 100));
//                    }
//                });


                File file1 = new File(picturePath);
                File file2 = new File(selectedFilePath);
                FileBody fileBody1 = new FileBody(file1);
                FileBody fileBody2 = new FileBody(file2);
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartEntity.addPart("AudioName", new StringBody(audioname));
                multipartEntity.addPart("cid", new StringBody(cid));
                multipartEntity.addPart("duration", new StringBody("5:00"));
                Log.e("file", "----------------------------" + fileBody1);
                multipartEntity.addPart("Photo", fileBody1);
                multipartEntity.addPart("Audio", fileBody2);
                multipartEntity.addPart("Is_notify", new StringBody(notify));
                multipartEntity.addPart("audiodate", new StringBody(datetimefull));
                multipartEntity.addPart("Description", new StringBody(CommonMethod.encodeEmoji(e_description.getText().toString())));

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
                    progressDialog.dismiss();
                    edit_category_name.setText("");
                    img_category_icon.setVisibility(View.GONE);
                    txt_photo.setText("Select Photo");
                    Toast.makeText(AddAudioActivity.this, "Audio added successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddAudioActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AddAudioActivity.this, "Audio not added.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddAudioActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
//            hud.dismiss();
        }

    }
}
