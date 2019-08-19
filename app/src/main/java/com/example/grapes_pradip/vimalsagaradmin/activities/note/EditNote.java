package com.example.grapes_pradip.vimalsagaradmin.activities.note;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

public class EditNote extends AppCompatActivity {

    private EditText edit_title, edit_date, edit_time, edit_description, edit_date_time;
    private Button btn_add;
    private String fulldate;

    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    int mHour;
    int mMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note_activity);
        bindId();
        toolbarClick();

        Intent intent = getIntent();

        final String id = intent.getStringExtra("id");
        edit_title.setText(CommonMethod.decodeEmoji(intent.getStringExtra("title")));
        edit_date.setText(CommonMethod.decodeEmoji(intent.getStringExtra("date")));
        edit_date_time.setText(CommonMethod.decodeEmoji(intent.getStringExtra("date")));
        edit_time.setText(CommonMethod.decodeEmoji(intent.getStringExtra("time")));
        edit_description.setText(CommonMethod.decodeEmoji(intent.getStringExtra("description")));

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

        edit_date_time.setCursorVisible(false);
        edit_date_time.setFocusableInTouchMode(false);
        edit_date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                datePicker();
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
                    new NoteEdit().execute(CommonMethod.encodeEmoji(edit_title.getText().toString()), edit_date_time.getText().toString(), CommonMethod.encodeEmoji(edit_time.getText().toString()), CommonMethod.encodeEmoji(edit_description.getText().toString()), id);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(EditNote.this,
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

        txt_header.setText("Edit Note");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void bindId() {
        edit_title = (EditText) findViewById(R.id.edit_title);
        edit_date = (EditText) findViewById(R.id.edit_date);
        edit_date_time = (EditText) findViewById(R.id.edit_date_time);
        edit_time = (EditText) findViewById(R.id.edit_time);
        edit_description = (EditText) findViewById(R.id.edit_description);
        btn_add = (Button) findViewById(R.id.btn_add);
    }

    @SuppressLint("StaticFieldLeak")
    private class NoteEdit extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String responseJson = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditNote.this);
            progressDialog.setMessage("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("Title", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("Datetime", strings[1]));
            nameValuePairs.add(new BasicNameValuePair("Time", strings[2]));
            nameValuePairs.add(new BasicNameValuePair("Description", strings[3]));
            nameValuePairs.add(new BasicNameValuePair("qid", strings[4]));
            responseJson = JsonParser.postStringResponse(CommonURL.Main_url + "competition/editcompetitionnote", nameValuePairs, EditNote.this);
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

    private void datePicker() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        //*************Call Time Picker Here ********************
                        tiemPicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void tiemPicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;


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



                        int length = String.valueOf(minute).length();
                        Log.e("length","----"+length);
                        String min;
                        if (length==1){
                            Log.e("if","----call");
                            Log.e("if","----call-----"+"0"+String.valueOf(minute));
                            min="0"+String.valueOf(minute);
                        }else{
                            Log.e("else","----call");
                            min=String.valueOf(minute);
                        }

                        if (hour_of_12_hour_format==0){
                            hour_of_12_hour_format=12;
                        }
                        edit_date_time.setText(date_time + " " + hour_of_12_hour_format + ":" + min + " " + status);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

}
