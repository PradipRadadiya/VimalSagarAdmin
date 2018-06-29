package com.example.grapes_pradip.vimalsagaradmin.activities.gallery;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntity;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.entity.mime.content.StringBody;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;

import static com.example.grapes_pradip.vimalsagaradmin.activities.gallery.AllGalleryActivity.gallerycid;


public class CustomGallery_Activity extends AppCompatActivity implements View.OnClickListener {
    private static Button selectImages;
    @SuppressLint("StaticFieldLeak")
    private static GridView galleryImagesGridView;
    private static ArrayList<String> galleryImageUrls;
    private static GridView_Adapter imagesAdapter;
    private ProgressDialog progressDialog;
    private ImageView img_back;
    private TextView txt_header;
    private ArrayList<String> selectedImage = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customgallery_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initViews();
        setListeners();
        fetchGalleryImages();
        setUpGridView();
        txt_header.setText("Custom Gallery");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Init all views
    private void initViews() {
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_header = (TextView) findViewById(R.id.txt_header);
        selectImages = (Button) findViewById(R.id.selectImagesBtn);
        galleryImagesGridView = (GridView) findViewById(R.id.galleryImagesGridView);

    }

    //fetch all images from gallery
    private void fetchGalleryImages() {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};//get all columns of type images
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;//order data by date
        @SuppressWarnings("deprecation") Cursor imagecursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy + " DESC");//get all data in Cursor by sorting in DESC order

        galleryImageUrls = new ArrayList<>();//Init array


        //Loop to cursor count
        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);//get column index
            galleryImageUrls.add(imagecursor.getString(dataColumnIndex));//get Image from column index
            System.out.println("Array path" + galleryImageUrls.get(i));
        }


    }

    //Set Up GridView method
    private void setUpGridView() {
        imagesAdapter = new GridView_Adapter(CustomGallery_Activity.this, galleryImageUrls, true);
        galleryImagesGridView.setAdapter(imagesAdapter);
    }

    //Set Listeners method
    private void setListeners() {
        selectImages.setOnClickListener(this);
    }

    //Show hide select button if images are selected or deselected
    @SuppressLint("SetTextI18n")
    public void showSelectButton() {
        ArrayList<String> selectedItems = imagesAdapter.getCheckedItems();
        if (selectedItems.size() > 0) {
//            selectImages.setText(selectedItems.size() + " - Images Selected");
            txt_header.setText(selectedItems.size() + " - Image Selected");
            selectImages.setVisibility(View.VISIBLE);
        } else {
            selectImages.setVisibility(View.GONE);
            txt_header.setText("Custom Gallery");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectImagesBtn:

                //When button is clicked then fill array with selected images
//                ArrayList<String> selectedItems = imagesAdapter.getCheckedItems();
                selectedImage = imagesAdapter.getCheckedItems();
//
//                Log.e("images select path", "----------------------" + selectedItems);
//
//                for (int i=0;i<selectedItems.size();i++){
//                    selectedImage.add(selectedItems.get(i));
//                }
                //Send back result to MainActivity with selected images
                new AddImage().execute();
//                Intent intent = new Intent();
//                intent.putExtra(AllGalleryActivity.CustomGalleryIntentKey, selectedItems.toString());//Convert Array into string to pass data
//                setResult(RESULT_OK, intent);//Set result OK
//                finish();//finish activity
                break;

        }

    }

    @SuppressLint("StaticFieldLeak")
    private class AddImage extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CustomGallery_Activity.this);
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
                HttpPost httpPost = new HttpPost(CommonURL.Main_url + CommonAPI_Name.addphotogallery);

                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartEntity.addPart("cid", new StringBody(gallerycid));
                String[] photoArr = new String[selectedImage.size()];

                Log.e("image size","-------------"+selectedImage.size());

                for (int i = 0; i < selectedImage.size(); i++) {
                    photoArr[i] = selectedImage.get(i);
                    Log.e("photoArr", "--------" + photoArr[i]);
                    File file1 = new File(photoArr[i]);
                    FileBody fileBody1 = new FileBody(file1);
                    multipartEntity.addPart("Photo[" + i + "]", fileBody1);

                }


//                File file1 = new File(picturePath);
//
//                FileBody fileBody1 = new FileBody(file1);
//                Log.e("file", "----------------------------" + fileBody1);
//                multipartEntity.addPart("Photo", fileBody1);

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

                    Toast.makeText(CustomGallery_Activity.this, "Image added successfully.", Toast.LENGTH_SHORT).show();
                    finish();
//                    Toast.makeText(AllGalleryActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(CustomGallery_Activity.this, "Image not added.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AllGalleryActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
