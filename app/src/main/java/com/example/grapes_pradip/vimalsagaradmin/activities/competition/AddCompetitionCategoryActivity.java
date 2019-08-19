package com.example.grapes_pradip.vimalsagaradmin.activities.competition;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.util.MarshMallowPermission;
import com.mcsoft.timerangepickerdialog.RangeTimePickerDialog;

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
import ch.boye.httpclientandroidlib.entity.mime.content.StringBody;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;

@SuppressWarnings("ALL")
public class AddCompetitionCategoryActivity extends AppCompatActivity implements View.OnClickListener, RangeTimePickerDialog.ISelectedTime {
    private ProgressDialog progressDialog;
    private EditText e_title, e_rule, e_date, e_time, e_total_q, e_total_minute;
    private TextView txt_header;
    private ImageView img_back;
    //    private ImageView img_category_icon;
    private Button btn_add;
    private Intent intent;
    private String picturePath = null;
    private boolean flag = false;
    private Bitmap thumbnail;
    private MarshMallowPermission permission;
    private String fulldate;
    private EditText e_description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_competition_category);
        permission = new MarshMallowPermission(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        findID();
        idClick();

       /* e_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    openDatePicker();
                }
            }
        });*/

        e_date.setCursorVisible(false);
        e_date.setFocusableInTouchMode(false);
        e_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                openDatePicker();
            }
        });


        e_time.setCursorVisible(false);
        e_time.setFocusableInTouchMode(false);
        e_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                getCurrentTime();

                showCustomDialogTimePicker();


            }
        });

    }

    private void idClick() {
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(e_title.getText().toString())) {
                    e_title.setError("Please enter category name.");
                    e_title.requestFocus();
                } else if (TextUtils.isEmpty(e_rule.getText().toString())) {
                    e_rule.setError("Please enter rules.");
                    e_rule.requestFocus();
                } else if (TextUtils.isEmpty(e_date.getText().toString())) {
                    e_date.setError("Please enter date.");
                    e_date.requestFocus();
                } else if (TextUtils.isEmpty(e_total_q.getText().toString())) {
                    e_total_q.setError("Please enter total question.");
                    e_total_q.requestFocus();
                } else if (TextUtils.isEmpty(e_total_minute.getText().toString())) {
                    e_total_minute.setError("Please enter total minute.");
                    e_total_minute.requestFocus();
                } else {
                    if (CommonMethod.isInternetConnected(AddCompetitionCategoryActivity.this)) {
                        Log.e("date","--------"+e_date.getText().toString());
                        Log.e("date","--------"+e_time.getText().toString());
                        new AddACompetitionCategory().execute(CommonMethod.encodeEmoji(e_title.getText().toString()), CommonMethod.encodeEmoji(e_rule.getText().toString()), e_date.getText().toString(), e_time.getText().toString(), CommonMethod.encodeEmoji(e_total_q.getText().toString()), CommonMethod.encodeEmoji(e_total_minute.getText().toString()),CommonMethod.encodeEmoji(e_description.getText().toString()));
                    } else {
                        Toast.makeText(AddCompetitionCategoryActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                    }

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddCompetitionCategoryActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.e("Date---", "DATE SELECTED " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                        fulldate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        fulldate = year + "-" +(monthOfYear + 1)  + "-" + dayOfMonth;
//                        e_date.setText(fulldate);
                        showCustomDialogTimePicker();

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

                        e_time.setText(hour_of_12_hour_format + ":" + minute + " " + status);
                    }
                }, mHour[0], mMinute, false);
        timePickerDialog.show();


    }

    private void setContent() {
        if (CommonMethod.isInternetConnected(AddCompetitionCategoryActivity.this)) {
//            new AddInformation().execute(e_title.getText().toString(), e_description.getText().toString(), e_date.getText().toString(), e_address.getText().toString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        e_description = (EditText) findViewById(R.id.e_description);
        e_title = (EditText) findViewById(R.id.e_title);
        e_rule = (EditText) findViewById(R.id.e_rule);
        e_date = (EditText) findViewById(R.id.e_date);
        e_time = (EditText) findViewById(R.id.e_time);
        e_total_q = (EditText) findViewById(R.id.e_total_q);
        e_total_minute = (EditText) findViewById(R.id.e_total_minute);

        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_add = (Button) findViewById(R.id.btn_add);
        txt_header.setText("Add Competition Category");
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

        }
    }

    private void checkPermissionImage() {
        selectImage();

    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(AddCompetitionCategoryActivity.this);

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
                        startActivityForResult(intent, 2);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!permission.checkPermissionForCamera()) {
                            permission.requestPermissionForCamera();
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                            startActivityForResult(intent, 2);
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
                Cursor c = AddCompetitionCategoryActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                String imagepath = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + imagepath);
                //Log.w("path of image from gallery......******************.........", picturePath + "");
                thumbnail = (BitmapFactory.decodeFile(picturePath));
                //Log.w("path of image from gallery......******************.........", picturePath + "");

            } else if (requestCode == 2) {
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
                        String imagepath = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                        Log.e("result", "--------------" + imagepath);

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
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onSelectedTime(int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
        Toast.makeText(AddCompetitionCategoryActivity.this, "\"Start: \"+hourStart+" + hourStart + "\":\"+minuteStart+\"\\nEnd: \"+hourEnd+\":\"+minuteEnd", Toast.LENGTH_SHORT).show();
        CommonMethod.logPrint("hourStart", "----------------" + hourStart);
        CommonMethod.logPrint("minuteStart", "----------------" + minuteStart);
        CommonMethod.logPrint("hourEnd", "----------------" + hourEnd);
        CommonMethod.logPrint("minuteEnd", "----------------" + minuteEnd);

        final Calendar c = Calendar.getInstance();
        final int[] mHour = {c.get(Calendar.HOUR_OF_DAY)};
        int mMinute = c.get(Calendar.MINUTE);

        String status = "AM";
        if (hourStart > 11) {
            // If the hour is greater than or equal to 12
            // Then the current AM PM status is PM
            status = "PM";
        }
        // Initialize a new variable to hold 12 hour format hour value
        int hour_of_12_hour_format;

        if (hourStart > 11) {

            // If the hour is greater than or equal to 12
            // Then we subtract 12 from the hour to make it 12 hour format time
            hour_of_12_hour_format = hourStart - 12;
        } else {
            hour_of_12_hour_format = hourStart;
        }


        CommonMethod.logPrint("start time", "----------------" + hour_of_12_hour_format + ":" + minuteStart + " " + status);



        int length = String.valueOf(minuteStart).length();
        Log.e("length", "----" + length);
        String min;
        if (length == 1) {
            Log.e("if", "----call");
            Log.e("if", "----call-----" + "0" + String.valueOf(minuteStart));
            min = "0" + String.valueOf(minuteStart);
        } else {
            Log.e("else", "----call");
            min = String.valueOf(minuteStart);
        }



        //End Time
        String status2 = "AM";
        if (hourEnd > 11) {
            // If the hour is greater than or equal to 12
            // Then the current AM PM status is PM
            status2 = "PM";
        }
        // Initialize a new variable to hold 12 hour format hour value
        int hour_of_12_hour_format2;

        if (hourEnd > 11) {

            // If the hour is greater than or equal to 12
            // Then we subtract 12 from the hour to make it 12 hour format time
            hour_of_12_hour_format2 = hourEnd - 12;
        } else {
            hour_of_12_hour_format2 = hourEnd;
        }


        if (hour_of_12_hour_format == 0) {
            hour_of_12_hour_format = 12;
        }

        if (hour_of_12_hour_format2 == 0) {
            hour_of_12_hour_format2 = 12;
        }

        int length2 = String.valueOf(minuteEnd).length();
        Log.e("length", "----" + length);
        String min2;
        if (length2 == 1) {
            Log.e("if", "----call");
            Log.e("if", "----call-----" + "0" + String.valueOf(minuteEnd));
            min2 = "0" + String.valueOf(minuteEnd);
        } else {
            Log.e("else", "----call");
            min2 = String.valueOf(minuteEnd);
        }

//        e_date.setText(fulldate+" "+hour_of_12_hour_format + ":" + minuteStart + " " + status + "-" + hour_of_12_hour_format2 + ":" + minuteEnd + " " + status2);
        e_date.setText(fulldate + " " + hour_of_12_hour_format + ":" + min + " " + status);
        e_time.setText(fulldate + " " + hour_of_12_hour_format2 + ":" + min2 + " " + status2);
        e_time.setEnabled(false);

    }

    private class AddACompetitionCategory extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddCompetitionCategoryActivity.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.show();
            progressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("method", "----------------" + "call");

            String title = e_title.getText().toString();

            Log.e("title", "----------" + title);
            String Photo = "";

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(CommonURL.Main_url + "competition/addcompetition");
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartEntity.addPart("Title", new StringBody(params[0]));
                multipartEntity.addPart("Rules", new StringBody(params[1]));
                multipartEntity.addPart("Start_Time", new StringBody(params[2]));
                multipartEntity.addPart("End_Time", new StringBody(params[3]));
                multipartEntity.addPart("Total_question", new StringBody(params[4]));
                multipartEntity.addPart("Total_minute", new StringBody(params[5]));
                multipartEntity.addPart("Description", new StringBody(params[6]));

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
                    Toast.makeText(AddCompetitionCategoryActivity.this, "Competition category added successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddCompetitionCategoryActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    e_title.setText("");
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(AddCompetitionCategoryActivity.this, "Competition category not added.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddCompetitionCategoryActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }

    public void showCustomDialogTimePicker() {

        // Create an instance of the dialog fragment and show it
        RangeTimePickerDialog dialog = new RangeTimePickerDialog();
        dialog.newInstance();
        dialog.setIs24HourView(false);
        dialog.setRadiusDialog(20);
        dialog.setTextTabStart("Start");
        dialog.setTextTabEnd("End");
        dialog.setTextBtnPositive("Accept");
        dialog.setTextBtnNegative("Close");
        dialog.setValidateRange(false);
        dialog.setColorBackgroundHeader(R.color.colorPrimary);
        dialog.setColorBackgroundTimePickerHeader(R.color.colorPrimary);
        dialog.setColorTextButton(R.color.colorPrimaryDark);
        dialog.enableMinutes(true);
        FragmentManager fragmentManager = getFragmentManager();
        dialog.show(fragmentManager, "");

    }

}
