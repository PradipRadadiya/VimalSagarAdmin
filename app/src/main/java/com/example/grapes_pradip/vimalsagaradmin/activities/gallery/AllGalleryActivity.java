package com.example.grapes_pradip.vimalsagaradmin.activities.gallery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.adapters.gallery.RecyclerImageAllAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.gallery.AllImageItem;
import com.example.grapes_pradip.vimalsagaradmin.model.gallery.ImageItemSplash;
import com.example.grapes_pradip.vimalsagaradmin.util.MarshMallowPermission;

import org.json.JSONArray;
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

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntity;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.entity.mime.content.StringBody;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;

import static com.example.grapes_pradip.vimalsagaradmin.adapters.gallery.RecyclerImageAllAdapter.imgid;

/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class AllGalleryActivity extends AppCompatActivity implements View.OnClickListener {
    private SwipeRefreshLayout swipe_refresh_information;
    private RecyclerView recyclerView_all_images;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<AllImageItem> imageItems = new ArrayList<>();
    public static ArrayList<ImageItemSplash> itemSplashArrayList = new ArrayList<>();
    private RecyclerImageAllAdapter recyclerimageAllAdapter;
    private TextView txt_addnew;
    private TextView txt_header;
    private ProgressDialog progressDialog;
    private String cid;
    private String title;
    private ImageView img_back;
    private ImageView img_nodata;
    private Dialog dialog;
    private int page_count = 1;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;


    private String picturePath = null;
    private boolean flag = false;
    private Bitmap thumbnail;
    private TextView txt_photo;
    private ImageView img_category_icon;
    private ImageView delete_data;
    private MarshMallowPermission permission;
    private Intent intent;
    ProgressBar progress_load;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_all_activity);
        permission = new MarshMallowPermission(this);
        Intent intent = getIntent();
        cid = intent.getStringExtra("gallery_category_id");
        title = intent.getStringExtra("title");
        linearLayoutManager = new LinearLayoutManager(AllGalleryActivity.this);
        gridLayoutManager = new GridLayoutManager(AllGalleryActivity.this, 3);
        findID();
        idClick();

        swipe_refresh_information.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        recyclerView_all_images.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = gridLayoutManager.getItemCount();
                firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                if (flag_scroll) {
                    Log.e("flag-Scroll", flag_scroll + "");
                } else {
                    if (loading) {
                        Log.e("flag-Loading", loading + "");
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                            //Log.e("flag-IF", (totalItemCount > previousTotal) + "");
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        Log.e("flag-Loading_second_if", loading + "");
                        if (CommonMethod.isInternetConnected(AllGalleryActivity.this)) {

                            Log.e("total count", "--------------------" + page_count);

                            page_count++;
                            new GetAllImageCategory().execute();
                        } else {
                            Toast.makeText(AllGalleryActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                        }
                        loading = true;

                    }
                }
            }

        });

    }

    private void refreshContent() {
        imgid.clear();
        imageItems = new ArrayList<>();
        page_count = 1;
        swipe_refresh_information.setRefreshing(false);
        new GetAllImageCategory().execute();
    }

    private void findID() {
        swipe_refresh_information = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_information);
        recyclerView_all_images = (RecyclerView) findViewById(R.id.recyclerView_all_images);
        recyclerView_all_images.setLayoutManager(gridLayoutManager);
        txt_addnew = (TextView) findViewById(R.id.txt_addnew);
        txt_header = (TextView) findViewById(R.id.txt_header);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_header.setText(title);
        img_nodata = (ImageView) findViewById(R.id.img_nodata);
        delete_data = (ImageView) findViewById(R.id.delete_data);
        progress_load = (ProgressBar) findViewById(R.id.progress_load);
    }

    private void idClick() {
        txt_addnew.setOnClickListener(this);
        img_back.setOnClickListener(this);
        delete_data.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_addnew:
                dialog = new Dialog(AllGalleryActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_image);
                dialog.show();

                txt_photo = (TextView) dialog.findViewById(R.id.txt_photo);
                img_category_icon = (ImageView) dialog.findViewById(R.id.img_category_icon);
                final Button btn_add = (Button) dialog.findViewById(R.id.btn_add);
                txt_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkPermissionImage();
                    }
                });
                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (picturePath == null) {
                            Toast.makeText(AllGalleryActivity.this, "Please upload image.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (CommonMethod.isInternetConnected(AllGalleryActivity.this)) {
                                new AddImage().execute();
                            } else {
                                Toast.makeText(AllGalleryActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                break;
            case R.id.img_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.delete_data:
                if (imgid.size() > 0) {

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllGalleryActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Confirm Delete...");

                    // Setting Dialog Message
                    alertDialog.setMessage(" Are you sure you want to delete?.");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_warning);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            // Write your code here to invoke YES event
                            Log.e("array delete", "-------------------" + imgid);
                            new DeleteGalleryImage().execute();

                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
//                            Toast.makeText(activity, "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();

                        }
                    });

                    alertDialog.show();
                } else {
                    Toast.makeText(AllGalleryActivity.this, "Select image for delete.", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void checkPermissionImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            selectImage();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!permission.checkPermissionForExternalStorage()) {
                permission.requestPermissionForExternalStorage();
            } else {
                selectImage();
            }
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(AllGalleryActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 1);


                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();


    }

    private class GetAllImageCategory extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(AllGalleryActivity.this);
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            progress_load.setVisibility(View.VISIBLE);
            recyclerView_all_images.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallimagesbycidgallery + "?cid=" + cid + "&page=" + page_count + "&psize=20");
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("json array", "-------------------" + jsonArray);

                    if (jsonArray.length() < 20 || jsonArray.length() == 0) {
                        flag_scroll = true;
                        Log.e("length_array_news", flag_scroll + "" + "<30===OR(0)===" + jsonArray.length());
                    }
                    itemSplashArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        Log.e("id", "---------------" + id);
                        String categoryid = jsonObject1.getString("CID");
                        String photo = jsonObject1.getString("Photo");
                        String date = jsonObject1.getString("Date");
                        imageItems.add(new AllImageItem(id, categoryid, photo, date,false));
                        itemSplashArrayList.add(new ImageItemSplash(CommonURL.ImagePath + CommonAPI_Name.Gallery + photo, CommonURL.ImagePath + CommonAPI_Name.Gallery + photo,false));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
            progress_load.setVisibility(View.GONE);
            recyclerView_all_images.setVisibility(View.VISIBLE);
            if (recyclerView_all_images != null) {
                recyclerimageAllAdapter = new RecyclerImageAllAdapter(AllGalleryActivity.this, imageItems);
                if (recyclerimageAllAdapter.getItemCount() != 0) {
                    recyclerView_all_images.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_all_images.setAdapter(recyclerimageAllAdapter);
                } else {
                    img_nodata.setVisibility(View.VISIBLE);
                    recyclerView_all_images.setVisibility(View.GONE);
                }
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        imgid.clear();
        imageItems = new ArrayList<>();
        // put your code here...
        if (CommonMethod.isInternetConnected(AllGalleryActivity.this)) {
            page_count = 1;
            new GetAllImageCategory().execute();
        } else {
            Toast.makeText(AllGalleryActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
        }
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
                Cursor c = AllGalleryActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
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
               /* thumbnail = (BitmapFactory.decodeFile(picturePath));
                img_category_icon.setVisibility(View.VISIBLE);
                img_category_icon.setImageBitmap(thumbnail);*/


//                txt_photo.setText(picturePath);
//                //Log.w("path of image from gallery......******************.........", picturePath + "");
//                thumbnail = (BitmapFactory.decodeFile(picturePath));
//                //Log.w("path of image from gallery......******************.........", picturePath + "");
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
            }
        }
    }

    private class AddImage extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AllGalleryActivity.this);
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
                File file1 = new File(picturePath);

                FileBody fileBody1 = new FileBody(file1);

                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                multipartEntity.addPart("cid", new StringBody(cid));
                Log.e("file", "----------------------------" + fileBody1);
                multipartEntity.addPart("Photo", fileBody1);

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
                    Toast.makeText(AllGalleryActivity.this, "Image added successfully.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AllGalleryActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(AllGalleryActivity.this, "Image not added.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AllGalleryActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
                dialog.dismiss();
                if (CommonMethod.isInternetConnected(AllGalleryActivity.this)) {
                    imageItems = new ArrayList<>();
                    new GetAllImageCategory().execute();
                } else {
                    Toast.makeText(AllGalleryActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private class DeleteGalleryImage extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AllGalleryActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < imgid.size(); i++) {
                String value = imgid.get(i);
                responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deleteimage + "?imageid=" + value);
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            imgid.clear();
            if (progressDialog != null) {
                progressDialog.dismiss();
                onResume();
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
        File file=new File(picturePath);
        outFile = new FileOutputStream(file);
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 40, outFile);
        outFile.flush();
        outFile.close();
    }
}
