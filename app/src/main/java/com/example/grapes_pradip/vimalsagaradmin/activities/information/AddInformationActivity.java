package com.example.grapes_pradip.vimalsagaradmin.activities.information;

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
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.gallery.activities.AlbumSelectActivity;
import com.example.grapes_pradip.vimalsagaradmin.gallery.helpers.Constants;
import com.example.grapes_pradip.vimalsagaradmin.gallery.models.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

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
import id.zelory.compressor.Compressor;

@SuppressWarnings("ALL")
public class AddInformationActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private EditText e_title;
    private EditText e_description;
    private EditText e_address;
    private EditText e_date;
    private TextView txt_header;
    private ImageView img_back;
    private Button btn_add;
    private Switch notificationswitch;
    String notify = "0";

    private ImageView img_icon;
    private TextView txt_photo;
    private String picturePath;
    private Bitmap thumbnail;
    private Intent intent;
    private boolean flag = false;

    private EditText edit_date, edit_time;
    String fulltime;
    private String fulldate;
    String datetimefull;


    private File actualImage;
    private File compressedImage;
    ArrayList<String> photoarray = new ArrayList<>();
    private RecyclerView recyclerView_photos;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_information);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        findID();
        txt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoarray = new ArrayList<>();
                Intent intent = new Intent(AddInformationActivity.this, AlbumSelectActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 1);
                startActivityForResult(intent, Constants.REQUEST_CODE);
//                selectImage();
            }
        });

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
                    mTimePicker = new TimePickerDialog(AddInformationActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
                mTimePicker = new TimePickerDialog(AddInformationActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddInformationActivity.this,
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

    private void setContent() {

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
            edit_date.setError("Please enter date.");
            edit_date.requestFocus();
        } else if (TextUtils.isEmpty(edit_time.getText().toString())) {
            edit_time.setError("Please enter time");
            edit_time.requestFocus();
        } else {
            if (CommonMethod.isInternetConnected(AddInformationActivity.this)) {
//                new AddInformation().execute(CommonMethod.encodeEmoji(e_title.getText().toString()), CommonMethod.encodeEmoji(e_description.getText().toString()), CommonMethod.encodeEmoji(e_date.getText().toString()), CommonMethod.encodeEmoji(e_address.getText().toString()), notify);
                new AddInfo().execute();
            } else {
                Toast.makeText(AddInformationActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        linearLayoutManager = new LinearLayoutManager(AddInformationActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_photos = (RecyclerView) findViewById(R.id.recyclerView_photos);
        recyclerView_photos.setLayoutManager(linearLayoutManager);
        edit_date = (EditText) findViewById(R.id.edit_date);
        edit_time = (EditText) findViewById(R.id.edit_time);
        e_title = (EditText) findViewById(R.id.e_title);
        e_description = (EditText) findViewById(R.id.e_description);
        e_address = (EditText) findViewById(R.id.e_address);
        e_date = (EditText) findViewById(R.id.e_date);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        btn_add = (Button) findViewById(R.id.btn_add);

        img_icon = (ImageView) findViewById(R.id.img_icon);
        txt_photo = (TextView) findViewById(R.id.txt_photo);

        txt_header.setText("Add Information");

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
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContent();
            }
        });
    }

    private class AddInformation extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddInformationActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("Title", params[0]));
                nameValuePairs.add(new BasicNameValuePair("Description", params[1]));
                nameValuePairs.add(new BasicNameValuePair("Date", params[2]));
                nameValuePairs.add(new BasicNameValuePair("Address", params[3]));
                nameValuePairs.add(new BasicNameValuePair("Is_notify", params[4]));
                responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + CommonAPI_Name.addInformation, nameValuePairs, AddInformationActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseJSON;
        }

        @SuppressLint("ShowToast")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "-------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(AddInformationActivity.this, "Infromation added successfully.", Toast.LENGTH_SHORT).show();
                    e_title.setText("");
                    e_description.setText("");
                    e_address.setText("");
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(AddInformationActivity.this, "Infromation not added.", Toast.LENGTH_SHORT).show();
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
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    private class AddInfo extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddInformationActivity.this);
            progressDialog.setMessage("Please wait..");

            progressDialog.setCancelable(false);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setProgress(0);
            progressDialog.show();

        }


        //
        @Override
        protected String doInBackground(String... params) {

            Log.e("method", "----------------" + "call");

            String title = CommonMethod.encodeEmoji(e_title.getText().toString());
            String dec = CommonMethod.encodeEmoji(e_description.getText().toString());
            datetimefull = fulldate + " " + fulltime;
            String date = datetimefull;
            String loc = CommonMethod.encodeEmoji(e_address.getText().toString());


            Log.e("title", "----------" + CommonMethod.encodeEmoji(title));
            Log.e("dec", "----------" + CommonMethod.encodeEmoji(dec));
            Log.e("date", "----------" + datetimefull);
            Log.e("loc", "----------" + loc);

            String Photo = "";

            Log.e("picturePath add", "----------------" + "picturePath");

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(CommonURL.Main_url + CommonAPI_Name.addInformation);


                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                Log.e("date time", "---------" + datetimefull);
                multipartEntity.addPart("Title", new StringBody(title));
                multipartEntity.addPart("Description", new StringBody(dec));
                multipartEntity.addPart("infodate", new StringBody(datetimefull));
                multipartEntity.addPart("Address", new StringBody(loc));
                multipartEntity.addPart("Is_notify", new StringBody(notify));


                if (photoarray.isEmpty()) {
                    Log.e("if call", "-----------");
                } else {

                    String[] photoArr = new String[photoarray.size()];
                    for (int i = 0; i < photoarray.size(); i++) {
                        photoArr[i] = photoarray.get(i);
                        Log.e("photoArr", "--------" + photoArr[i]);
                        File file1 = new File(photoArr[i]);
                        FileBody fileBody1 = new FileBody(file1);
//                        multipartEntity.addPart("Photo", fileBody1);
                        multipartEntity.addPart("Photo", fileBody1);
//                        multipartEntity.addPart("Photo[" + i + "]", fileBody1);

                    }
                    /*Log.e(" else call", "-----------");
                    File file1 = new File(picturePath);
                    FileBody fileBody1 = new FileBody(file1);
                    multipartEntity.addPart("Photo", fileBody1);*/
                }

/*

                if (picturePath == null){

                }else {
                    File file1 = new File(picturePath);
                    FileBody fileBody1 = new FileBody(file1);
                    Log.e("file", "----------------------------" + fileBody1);
                    multipartEntity.addPart("Photo", fileBody1);
                }
*/


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
                    Toast.makeText(AddInformationActivity.this, "Infromation added successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddAudioCategoryActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    e_title.setText("");
                    img_icon.setVisibility(View.GONE);
                    txt_photo.setText("Select Photo");
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AddInformationActivity.this, "Infromation not added.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AddAudioCategoryActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
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

        img_icon.setVisibility(View.VISIBLE);
        img_icon.setImageBitmap(thumbnail);
        OutputStream outFile = null;
        File file = new File(picturePath);
        outFile = new FileOutputStream(file);
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 40, outFile);
        outFile.flush();
        outFile.close();
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddInformationActivity.this);

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

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                        startActivityForResult(intent, 2);

                    }


                } else if (options[item].equals("Choose from Gallery"))

                {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(intent, 1);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(intent, 1);

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

        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0, l = images.size(); i < l; i++) {
                stringBuffer.append(images.get(i).path + "\n");

                photoarray.add(images.get(i).path);

//                img_icon.setVisibility(View.VISIBLE);

                PhotoAdapter photoAdapter = new PhotoAdapter(AddInformationActivity.this, photoarray);
                recyclerView_photos.setVisibility(View.VISIBLE);
                recyclerView_photos.setAdapter(photoAdapter);


//                Glide.with(AddInformationActivity.this).load(images.get(i).path
//                        .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loading_bar).dontAnimate().into(img_icon);

            }


            Log.e("image list", "------------" + stringBuffer.toString());
//            selectedImage.add(stringBuffer.toString());
//            textView.setText(stringBuffer.toString());
        }


        if (resultCode == RESULT_OK) {
            flag = true;
            if (requestCode == 1) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                flag = true;
                Cursor c = AddInformationActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
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
//
//
                Log.e("path of image from gallery......******************.........", picturePath + "");
//                img_category_icon.setVisibility(View.VISIBLE);
//                img_category_icon.setImageBitmap(thumbnail);


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
                    img_icon.setVisibility(View.VISIBLE);
                    img_icon.setImageBitmap(bitmap);
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

            }
        }
    }


    public void customCompressImage(View view) {
        if (actualImage == null) {
        } else {
            // Compress image in main thread using custom Compressor
            try {
                compressedImage = new Compressor(this)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(actualImage);

                setCompressedImage();
            } catch (IOException e) {
                e.printStackTrace();
//                showError(e.getMessage());
            }

        }

    }


    private void setCompressedImage() {
        img_icon.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getAbsolutePath()));

        Log.e("size", "-------------------" + getReadableFileSize(compressedImage.length()));
//        compressedSizeTextView.setText(String.format("Size : %s", getReadableFileSize(compressedImage.length())));

        Toast.makeText(this, "Compressed image save in " + compressedImage.getPath(), Toast.LENGTH_LONG).show();
        Log.d("Compressor", "Compressed image save in " + compressedImage.getPath());
    }


    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    private int getRandomColor() {
        Random rand = new Random();
        return Color.argb(100, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
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

                    PopupMenu popup = new PopupMenu(AddInformationActivity.this, holder.photo);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(AddInformationActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
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


}
