package com.example.grapes_pradip.vimalsagaradmin.activities.slide;

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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntity;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.entity.mime.content.StringBody;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;


@SuppressWarnings("ALL")
public class EditSlideImageActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;
    private EditText e_title;
    private TextView txt_header;
    private TextView txt_photo;
    private ImageView img_back;
    private ImageView img_category_icon;
    private Button btn_add;
    private Intent intent;
    private String picturePath = null;
    private boolean flag = false;
    private Bitmap thumbnail;
    private String id;
    private String name;
    private String photo;
    private MarshMallowPermission permission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_slide_gallery);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        permission = new MarshMallowPermission(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        photo = intent.getStringExtra("photo");
        findID();
        idClick();
        setContent();


    }

    private void idClick() {
        txt_photo.setOnClickListener(this);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommonMethod.isInternetConnected(EditSlideImageActivity.this)) {
                    new UpdateSlideImage().execute();
                } else {
                    Toast.makeText(EditSlideImageActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    private void setContent() {

        Log.e("photo", "----------" + CommonURL.ImagePath + CommonAPI_Name.bannerimage + photo);
        Glide.with(EditSlideImageActivity.this).load(CommonURL.ImagePath + CommonAPI_Name.bannerimage + photo
                .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loading_bar).dontAnimate().into(img_category_icon);


        if (CommonMethod.isInternetConnected(EditSlideImageActivity.this)) {
//            new AddInformation().execute(e_title.getText().toString(), e_description.getText().toString(), e_date.getText().toString(), e_address.getText().toString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_photo = (TextView) findViewById(R.id.txt_photo);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_category_icon = (ImageView) findViewById(R.id.img_category_icon);
        btn_add = (Button) findViewById(R.id.btn_add);
        txt_header.setText("Edit Slide Image");
        btn_add.setText("Update");
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
        }
    }

    private void checkPermissionImage() {
        selectImage();

    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(EditSlideImageActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                        startActivityForResult(intent, 2);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!permission.checkPermissionForCamera()) {
                            permission.requestPermissionForCamera();
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                            startActivityForResult(intent, 2);
                        }
                    }


                } else if (options[item].equals("Choose from Gallery"))

                {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(intent, 1);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!permission.checkPermissionForExternalStorage()) {
                            permission.requestPermissionForExternalStorage();
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

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
                Cursor c = EditSlideImageActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
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
               /* thumbnail = (BitmapFactory.decodeFile(picturePath));
                //Log.w("path of image from gallery......******************.........", picturePath + "");
                img_category_icon.setVisibility(View.VISIBLE);
                img_category_icon.setImageBitmap(thumbnail);*/
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
                    img_category_icon.setVisibility(View.VISIBLE);
                    img_category_icon.setImageBitmap(bitmap);
                    img_category_icon.setVisibility(View.VISIBLE);
                    img_category_icon.setImageBitmap(bitmap);
                    picturePath = Environment
                            .getExternalStorageDirectory() + "/VimalsagarjiImage"
                            + File.separator;


//                            + "Phoenix" + File.separator + "default";

                    f.delete();
                    OutputStream outFile = null;

                    File root = Environment.getExternalStorageDirectory();
                    File dir = new File(root.getAbsolutePath() + "/VimalsagarjiImage" + File.separator);
                    dir.mkdirs();
                    String pic = CommonMethod.getRandomString(30);
                    File file = new File(dir, String.valueOf(pic + ".jpg"));

                    if (file.exists()) {

                        Toast.makeText(this, "This name photo already exists.", Toast.LENGTH_SHORT).show();
                    } else {
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
//                            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    private class UpdateSlideImage extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditSlideImageActivity.this);
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


                multipartEntity.addPart("pid", new StringBody(id));
                Log.e("cid", "----------------" + id);

                multipartEntity.addPart("hiddenphoto", new StringBody(photo));
                Log.e("photo", "----------------" + photo);


                if (picturePath == null) {
                    Log.e("if click", "-----------");
                } else {
                    File file1 = new File(picturePath);
                    FileBody fileBody1 = new FileBody(file1);
                    Log.e("file", "----------------------------" + fileBody1);
                    multipartEntity.addPart("Photo", fileBody1);
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
                    Toast.makeText(EditSlideImageActivity.this, "Image upload successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(EditAudioCategoryActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(EditSlideImageActivity.this, "Image not upload.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(EditAudioCategoryActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
