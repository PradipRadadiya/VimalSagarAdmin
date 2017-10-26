package com.example.grapes_pradip.vimalsagaradmin.activities.audio;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
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
    String notify="0";

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
                } else if (audioPath == null) {
                    Toast.makeText(AddAudioActivity.this, R.string.uploadaudio, Toast.LENGTH_SHORT).show();

                } else {
                    if (CommonMethod.isInternetConnected(AddAudioActivity.this)) {
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
        edit_category_name = (EditText) findViewById(R.id.edit_category_name);
        edit_audio_name = (EditText) findViewById(R.id.edit_audio_name);
        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_photo = (TextView) findViewById(R.id.txt_photo);
        txt_audio = (TextView) findViewById(R.id.txt_audio);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_category_icon = (ImageView) findViewById(R.id.img_category_icon);
        btn_add = (Button) findViewById(R.id.btn_add);
        txt_header.setText("Add Audio");
        edit_category_name.setText(categoryname);
        notificationswitch= (Switch) findViewById(R.id.notificationswitch);

        notificationswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Log.e("checked","----------------"+isChecked);
                    notify="0";
                }else{
                    Log.e("checked","----------------"+isChecked);
                    notify="1";
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
                //Log.w("path of image from gallery......******************.........", picturePath + "");
                thumbnail = (BitmapFactory.decodeFile(picturePath));
                //Log.w("path of image from gallery......******************.........", picturePath + "");
                img_category_icon.setVisibility(View.VISIBLE);
                img_category_icon.setImageBitmap(thumbnail);
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Audio.Media.DATA};
                Cursor c = AddAudioActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                audioPath = c.getString(columnIndex);
                c.close();
                String audiopath = audioPath.substring(audioPath.lastIndexOf("/") + 1);
                Log.e("result", "--------------" + audiopath);
                txt_audio.setText("Selected audio : " + audiopath);
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
            String audioname = edit_audio_name.getText().toString();

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
                File file2 = new File(audioPath);
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
