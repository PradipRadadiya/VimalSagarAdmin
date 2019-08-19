package com.example.grapes_pradip.vimalsagaradmin.activities.jainism;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.audio.AddAudioActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.FilePath;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
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
import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntity;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.entity.mime.content.StringBody;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

public class AddJainism extends AppCompatActivity {

    private EditText edit_title, edit_date, edit_time, edit_description;
    private Button btn_add;
    private String fulldate;
    private TextView txt_audio;
    private MarshMallowPermission permission;
    private String selectedFilePath="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jainism);
        permission = new MarshMallowPermission(this);
        bindId();
        toolbarClick();

        edit_date.setCursorVisible(false);
        edit_date.setFocusableInTouchMode(false);
        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                openDatePicker();
            }
        });


        edit_time.setCursorVisible(false);
        edit_time.setFocusableInTouchMode(false);
        edit_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                getCurrentTime();
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edit_title.getText().toString())) {
                    edit_title.setError("Please enter title.");
                    edit_title.requestFocus();
                } else if (TextUtils.isEmpty(edit_description.getText().toString())) {
                    edit_description.setError("Please enter descriotion.");
                    edit_description.requestFocus();
                } else {
                    new JainismAdd().execute(CommonMethod.encodeEmoji(edit_title.getText().toString()), CommonMethod.encodeEmoji(edit_description.getText().toString()));
                }
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddJainism.this,
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

    private void getCurrentTime() {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        final int[] mHour = {c.get(Calendar.HOUR_OF_DAY)};
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {


                        String status = "AM";

                        if (hourOfDay > 11) {
                            // If the hour is greater than or equal to 12
                            // Then the current AM PM status is PM
                            status = "PM";
                        }

                        // Initialize a new variable to hold 12 hour format hour value
                        int hour_of_12_hour_format;

                        if (hourOfDay > 11) {

                            // If the hour is greater than or equal to 12
                            // Then we subtract 12 from the hour to make it 12 hour format time
                            hour_of_12_hour_format = hourOfDay - 12;
                        } else {
                            hour_of_12_hour_format = hourOfDay;
                        }

                        edit_time.setText(hour_of_12_hour_format + ":" + minute + " " + status);
                    }
                }, mHour[0], mMinute, false);
        timePickerDialog.show();


    }

    @SuppressLint("SetTextI18n")
    private void toolbarClick() {
        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        TextView txt_header = (TextView) findViewById(R.id.txt_header);

        txt_header.setText("Add Jainism");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        txt_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAudio();
            }
        });

    }


    private void bindId() {
        txt_audio = findViewById(R.id.txt_audio);
        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_date = (EditText) findViewById(R.id.edit_date);
        edit_time = (EditText) findViewById(R.id.edit_time);
        edit_description = (EditText) findViewById(R.id.edit_description);
        btn_add = (Button) findViewById(R.id.btn_add);
    }

    @SuppressLint("StaticFieldLeak")
    private class JainismAdd extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddJainism.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(CommonURL.Main_url + "jainaism/add");

                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartEntity.addPart("title", new StringBody(strings[0]));
                multipartEntity.addPart("description", new StringBody(strings[1]));


                if (!selectedFilePath.equalsIgnoreCase("")) {
                    File file = new File(selectedFilePath);
                    FileBody fileBody = new FileBody(file);
                    Log.e("file", "----------------------------" + fileBody);
                    multipartEntity.addPart("Audio", fileBody);
                }

                httpPost.setEntity(multipartEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                String sResponse;
                StringBuilder s = new StringBuilder();
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                    responseJson = s.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

//
//            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
//            nameValuePairs.add(new BasicNameValuePair("title", strings[0]));
//            nameValuePairs.add(new BasicNameValuePair("description", strings[1]));
//            responseJson = JsonParser.postStringResponse(CommonURL.Main_url + "jainaism/add", nameValuePairs, AddJainism.this);
            return responseJson;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progressDialog.dismiss();
                    finish();
                } else {
                    progressDialog.dismiss();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkPermissionAudio() {
        if (!permission.checkPermissionForExternalStorage()) {
            permission.requestPermissionForExternalStorage();
        } else {
            Intent intent = new Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 2);
        }

    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {


                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this, selectedFileUri);
                Log.e("Selected File Path:", "-------------------" + selectedFilePath);


                if (selectedFilePath != null && !selectedFilePath.equals("")) {
                    txt_audio.setText(selectedFilePath);
                } else {
                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
