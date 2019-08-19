package com.example.grapes_pradip.vimalsagaradmin.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.yalantis.ucrop.view.UCropView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.List;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntity;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.entity.mime.content.StringBody;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static com.example.grapes_pradip.vimalsagaradmin.adapters.slide.SlideImageAdapter.hiddenPhoto;
import static com.example.grapes_pradip.vimalsagaradmin.adapters.slide.SlideImageAdapter.imageId;

/**
 * Created by Oleksii Shliama (https://github.com/shliama).
 */
public class ResultActivity extends BaseActivity {

    private static final String TAG = "ResultActivity";
    private static final String CHANNEL_ID = "3000";
    private static final int DOWNLOAD_NOTIFICATION_ID_DONE = 911;
    private String picturePath="";

    ProgressDialog progressDialog;

    File saveFile;
    public static void startWithUri(@NonNull Context context, @NonNull Uri uri) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.setData(uri);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Uri uri = getIntent().getData();


        Log.e("picturepath","-------------------"+uri);

//        String path = getRealPathFromUri(ResultActivity.this, uri);
//        Log.e("picturepath","-------------------"+path);




        if (uri != null) {
            try {
                UCropView uCropView = findViewById(R.id.ucrop);
                uCropView.getCropImageView().setImageUri(uri, null);
                uCropView.getOverlayView().setShowCropFrame(false);
                uCropView.getOverlayView().setShowCropGrid(false);
                uCropView.getOverlayView().setDimmedColor(Color.TRANSPARENT);
            } catch (Exception e) {
                Log.e(TAG, "setImageUri", e);
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(getIntent().getData().getPath()).getAbsolutePath(), options);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.format_crop_result_d_d, options.outWidth, options.outHeight));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_download) {
            saveCroppedImage();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public  String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try{
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveCroppedImage();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void saveCroppedImage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            Uri imageUri = getIntent().getData();
            if (imageUri != null && imageUri.getScheme().equals("file")) {
                try {
                    copyFileToDownloads(getIntent().getData());
                } catch (Exception e) {
                    Toast.makeText(ResultActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, imageUri.toString(), e);
                }
            } else {
                Toast.makeText(ResultActivity.this, getString(R.string.toast_unexpected_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void copyFileToDownloads(Uri croppedFileUri) throws Exception {


        String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        Log.e("path","---------------"+downloadsDirectoryPath);


        String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(), croppedFileUri.getLastPathSegment());

        saveFile = new File(downloadsDirectoryPath, filename);

        FileInputStream inStream = new FileInputStream(new File(croppedFileUri.getPath()));
        FileOutputStream outStream = new FileOutputStream(saveFile);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();

        showNotification(saveFile);


        if (imageId.equalsIgnoreCase("")) {
            new AddImage().execute();
        }else{
            new UpdateImage().execute();
        }
//        Toast.makeText(this, R.string.notification_image_saved, Toast.LENGTH_SHORT).show();


//        finish();


    }

    private void showNotification(@NonNull File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri fileUri = FileProvider.getUriForFile(
                this,
                getString(R.string.file_provider_authorities),
                file);

        intent.setDataAndType(fileUri, "image/*");

        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : resInfoList) {
            grantUriPermission(
                    info.activityInfo.packageName,
                    fileUri, FLAG_GRANT_WRITE_URI_PERMISSION | FLAG_GRANT_READ_URI_PERMISSION);
        }


        NotificationCompat.Builder notificationBuilder;
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(createChannel());
            }
            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }

        notificationBuilder
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_image_saved_click_to_preview))
                .setTicker(getString(R.string.notification_image_saved))
                .setSmallIcon(R.drawable.ic_done)
                .setOngoing(false)
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
                .setAutoCancel(true);
        if (notificationManager != null) {
            notificationManager.notify(DOWNLOAD_NOTIFICATION_ID_DONE, notificationBuilder.build());
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public NotificationChannel createChannel() {
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.channel_name), importance);
        channel.setDescription(getString(R.string.channel_description));
        channel.enableLights(true);
        channel.setLightColor(Color.YELLOW);
        return channel;
    }

    @SuppressLint("StaticFieldLeak")
    private class AddImage extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ResultActivity.this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("method", "----------------" + "call");


            String Photo = "";


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(CommonURL.Main_url + CommonAPI_Name.addbanner);

                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//                String[] photoArr = new String[selectedImage.size()];

//                Log.e("image size", "-------------" + selectedImage.size());
/*
                if (textquote.equalsIgnoreCase("")){

                }else{

                    Log.e("textquote","-------------"+textquote);
                    multipartEntity.addPart("description",new StringBody(CommonMethod.encodeEmoji(textquote)));
                }*/

//                if (selectedImage.isEmpty()) {
//
//                } else {

//                for (int i = 0; i < selectedImage.size(); i++) {
//                    photoArr[i] = selectedImage.get(i);
//                    Log.e("photoArr", "--------" + photoArr[i]);
//                    File file1 = new File(photoArr[i]);
                    FileBody fileBody1 = new FileBody(saveFile);
                    multipartEntity.addPart("Photo[0]", fileBody1);

//                }
//                }


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

                    Toast.makeText(ResultActivity.this, "Image added successfully.", Toast.LENGTH_SHORT).show();
                    finish();



                } else {
                    Toast.makeText(ResultActivity.this, "Image not added.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AllGalleryActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateImage extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ResultActivity.this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {

            Log.e("method", "----------------" + "call");


            String Photo = "";


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(CommonURL.Main_url + CommonAPI_Name.updatebanner);

                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//                String[] photoArr = new String[selectedImage.size()];

//                Log.e("image size", "-------------" + selectedImage.size());
/*
                if (textquote.equalsIgnoreCase("")){

                }else{

                    Log.e("textquote","-------------"+textquote);
                    multipartEntity.addPart("description",new StringBody(CommonMethod.encodeEmoji(textquote)));
                }*/

//                if (selectedImage.isEmpty()) {
//
//                } else {

//                for (int i = 0; i < selectedImage.size(); i++) {
//                    photoArr[i] = selectedImage.get(i);
//                    Log.e("photoArr", "--------" + photoArr[i]);
//                    File file1 = new File(photoArr[i]);
                FileBody fileBody1 = new FileBody(saveFile);
                multipartEntity.addPart("pid", new StringBody(imageId));
                Log.e("imageId", "----------------" + "");

                multipartEntity.addPart("hiddenphoto", new StringBody(hiddenPhoto));
                Log.e("photo", "----------------" + "");
                multipartEntity.addPart("Photo", fileBody1);






//                }
//                }


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
                    imageId="";
                    hiddenPhoto="";

                    Toast.makeText(ResultActivity.this, "Image Edit successfully.", Toast.LENGTH_SHORT).show();
                    finish();


                } else {
                    Toast.makeText(ResultActivity.this, "Image not added.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AllGalleryActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
