package com.example.grapes_pradip.vimalsagaradmin.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.adapters.slide.SlideImageAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.common.SharedPreferencesClass;
import com.example.grapes_pradip.vimalsagaradmin.gallery.activities.AlbumSelectActivity;
import com.example.grapes_pradip.vimalsagaradmin.gallery.helpers.Constants;
import com.example.grapes_pradip.vimalsagaradmin.gallery.models.Image;
import com.example.grapes_pradip.vimalsagaradmin.model.SlideImageItem;

import org.json.JSONArray;
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
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;

import static android.app.Activity.RESULT_OK;


public class SlideImageFragment extends Fragment implements View.OnClickListener {
    private View rootview;
    private SwipeRefreshLayout swipe_refresh_information;
    private RecyclerView recyclerView_information;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<SlideImageItem> slideImageItems = new ArrayList<>();
    private TextView txt_addnew;
    private ProgressDialog progressDialog;
    private ImageView img_nodata;
    private ProgressBar progress_load;
    private SharedPreferencesClass sharedPreferencesClass;
    private ArrayList<String> selectedImage = new ArrayList<>();
    private TextView txt_addquote;
    private Dialog dialog;
    private String textquote;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_slideimage, container, false);
        sharedPreferencesClass = new SharedPreferencesClass(getActivity());
        linearLayoutManager = new LinearLayoutManager(getActivity());
        findID();
        idClick();

       /* if (sharedPreferencesClass.getRole().equalsIgnoreCase("main")) {
            txt_addnew.setVisibility(View.VISIBLE);
        } else {
            txt_addnew.setVisibility(View.GONE);
        }*/

        swipe_refresh_information.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CommonMethod.isInternetConnected(getActivity())) {
                    refreshContent();
                } else {
                    swipe_refresh_information.setRefreshing(false);
                }
            }
        });

        return rootview;

    }

    private void refreshContent() {

        swipe_refresh_information.setRefreshing(false);
        new GetSlideImage().execute();
    }

    private void findID() {
        swipe_refresh_information = rootview.findViewById(R.id.swipe_refresh_information);
        recyclerView_information = rootview.findViewById(R.id.recyclerView_information);
        recyclerView_information.setLayoutManager(linearLayoutManager);
        txt_addnew = rootview.findViewById(R.id.txt_addnew);
        txt_addquote = rootview.findViewById(R.id.txt_addquote);
        img_nodata = rootview.findViewById(R.id.img_nodata);
        progress_load = rootview.findViewById(R.id.progress_load);
    }

    private void idClick() {
        txt_addnew.setOnClickListener(this);
        txt_addquote.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                        Fragment fr;
                        fr = new DesktopFragment();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_content, fr);
                        fragmentTransaction.commit();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_addnew:
                selectedImage = new ArrayList<>();
                Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 5);
                startActivityForResult(intent, Constants.REQUEST_CODE);
                break;

            case R.id.txt_addquote:
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_text);
                dialog.show();
                final Button btn_add = dialog.findViewById(R.id.btn_add);
                final EditText txt_text = dialog.findViewById(R.id.txt_text);

                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (CommonMethod.isInternetConnected(getActivity())) {
                            textquote = txt_text.getText().toString();
//                            new AddImage().execute();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), R.string.internet, Toast.LENGTH_SHORT).show();
                        }
                    }

                });

                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetSlideImage extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            progress_load.setVisibility(View.VISIBLE);
            recyclerView_information.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallbanner);
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
                    slideImageItems = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String url = jsonObject1.getString("image");
                        slideImageItems.add(new SlideImageItem(id, url));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
            progress_load.setVisibility(View.GONE);
            recyclerView_information.setVisibility(View.VISIBLE);
            if (recyclerView_information != null) {
                SlideImageAdapter slideImageAdapter = new SlideImageAdapter(getActivity(), slideImageItems);
                if (slideImageAdapter.getItemCount() != 0) {
                    recyclerView_information.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_information.setAdapter(slideImageAdapter);
                } else {
                    recyclerView_information.setVisibility(View.GONE);
                    img_nodata.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            StringBuilder stringBuffer = new StringBuilder();
            for (int i = 0, l = images.size(); i < l; i++) {
                stringBuffer.append(images.get(i).path).append("\n");
                selectedImage.add(images.get(i).path);

                if (i + 1 == images.size()) {
                    new AddImage().execute();
                }
            }


            Log.e("image list", "------------" + stringBuffer.toString());
//            selectedImage.add(stringBuffer.toString());
//            textView.setText(stringBuffer.toString());
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AddImage extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
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
                String[] photoArr = new String[selectedImage.size()];

                Log.e("image size", "-------------" + selectedImage.size());
/*
                if (textquote.equalsIgnoreCase("")){

                }else{

                    Log.e("textquote","-------------"+textquote);
                    multipartEntity.addPart("description",new StringBody(CommonMethod.encodeEmoji(textquote)));
                }*/

//                if (selectedImage.isEmpty()) {
//
//                } else {

                    for (int i = 0; i < selectedImage.size(); i++) {
                        photoArr[i] = selectedImage.get(i);
                        Log.e("photoArr", "--------" + photoArr[i]);
                        File file1 = new File(photoArr[i]);
                        FileBody fileBody1 = new FileBody(file1);
                        multipartEntity.addPart("Photo[" + i + "]", fileBody1);

                    }
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

                    Toast.makeText(getActivity(), "Image added successfully.", Toast.LENGTH_SHORT).show();
                    selectedImage.clear();
//                    finish();
//                    Toast.makeText(AllGalleryActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "Image not added.", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(AllGalleryActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        if (CommonMethod.isInternetConnected(getActivity())) {
            new GetSlideImage().execute();
        }
    }


}
