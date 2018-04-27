package com.example.grapes_pradip.vimalsagaradmin.activities.video;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.adapters.video.RecyclerVideoAllAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.video.AllVideoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import static com.example.grapes_pradip.vimalsagaradmin.adapters.video.RecyclerVideoAllAdapter.videoid;

/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class AllVideoActivity extends AppCompatActivity implements View.OnClickListener {
    private SwipeRefreshLayout swipe_refresh_information;
    private RecyclerView recyclerView_all_audio;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<AllVideoItem> allVideoItems = new ArrayList<>();
    private RecyclerVideoAllAdapter recyclerVideoAllAdapter;
    private TextView txt_addnew;
    private TextView txt_header;
    private TextView txt_h;
    private ProgressDialog progressDialog;
    private String cid;
    private String title;
    private ImageView img_back;
    private ImageView img_nodata;
    private ImageView delete_data;

    private int page_count = 1;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    ProgressBar progress_load;

    public static String description;
    public static String videolink;
    public static String isodate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_all_activity);
        Intent intent = getIntent();
        cid = intent.getStringExtra("video_category_id");
        title = intent.getStringExtra("title");
        linearLayoutManager = new LinearLayoutManager(AllVideoActivity.this);
        findID();
        idClick();

        swipe_refresh_information.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CommonMethod.isInternetConnected(AllVideoActivity.this)) {
                    refreshContent();
                } else {
                    swipe_refresh_information.setRefreshing(false);
                }
            }
        });
        recyclerView_all_audio.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

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
                        if (CommonMethod.isInternetConnected(AllVideoActivity.this)) {

                            Log.e("total count", "--------------------" + page_count);

                            page_count++;
                            new GetAllVideoCategory().execute();
                        } else {
                            Toast.makeText(AllVideoActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
                        }
                        loading = true;

                    }
                }
            }

        });

    }

    private void refreshContent() {
        videoid.clear();
        allVideoItems = new ArrayList<>();
        page_count = 1;
        swipe_refresh_information.setRefreshing(false);
        new GetAllVideoCategory().execute();
    }

    @SuppressLint("SetTextI18n")
    private void findID() {
        swipe_refresh_information = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_information);
        recyclerView_all_audio = (RecyclerView) findViewById(R.id.recyclerView_all_audio);
        recyclerView_all_audio.setLayoutManager(linearLayoutManager);
        txt_addnew = (TextView) findViewById(R.id.txt_addnew);
        txt_header = (TextView) findViewById(R.id.txt_header);
        txt_h = (TextView) findViewById(R.id.txt_h);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_header.setText(CommonMethod.decodeEmoji(title));
        txt_h.setText("All Video");
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
                Intent intent = new Intent(AllVideoActivity.this, AddVideoActivity.class);
                intent.putExtra("cid", cid);
                intent.putExtra("categoryname", title);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.img_back:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case R.id.delete_data:
                if (videoid.size() > 0) {

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllVideoActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Confirm Delete...");

                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want to delete?.");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_warning);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            // Write your code here to invoke YES event
                            Log.e("array delete", "-------------------" + videoid);
                            new DeleteVideo().execute();

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
                    Toast.makeText(AllVideoActivity.this, "Select video for delete.", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private class GetAllVideoCategory extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(AllVideoActivity.this);
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            progress_load.setVisibility(View.VISIBLE);
            recyclerView_all_audio.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getvideobycategoryid + "?cid=" + cid + "&page=" + page_count + "&psize=30");
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
                    if (jsonArray.length() < 30 || jsonArray.length() == 0) {
                        flag_scroll = true;
                        Log.e("length_array_news", flag_scroll + "" + "<30===OR(0)===" + jsonArray.length());
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        Log.e("id", "---------------" + id);
                        String videoname = jsonObject1.getString("VideoName");
                        String categoryid = jsonObject1.getString("CategoryID");
                        String audio = jsonObject1.getString("Video");
                        String photo = jsonObject1.getString("Photo");
                        String duration = jsonObject1.getString("Duration");
                        String date = jsonObject1.getString("Date");
                        isodate=jsonObject1.getString("Date");
                        String categoryName = jsonObject1.getString("Name");
                        String view = jsonObject1.getString("View");
                        description = jsonObject1.getString("Description");
                        videolink = jsonObject1.getString("video_link");

                        String[] string = date.split(" ");
                        Log.e("str1", "--------" + string[0]);
                        Log.e("str2", "--------" + string[1]);

                        Date dt = CommonMethod.convert_date(date);
                        Log.e("Convert date is", "------------------" + dt);
                        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", dt);//Thursday
                        String stringMonth = (String) android.text.format.DateFormat.format("MMM", dt); //Jun
                        String intMonth = (String) android.text.format.DateFormat.format("MM", dt); //06
                        String year = (String) android.text.format.DateFormat.format("yyyy", dt); //2013
                        String day = (String) android.text.format.DateFormat.format("dd", dt); //20

                        Log.e("dayOfTheWeek", "-----------------" + dayOfTheWeek);
                        Log.e("stringMonth", "-----------------" + stringMonth);
                        Log.e("intMonth", "-----------------" + intMonth);
                        Log.e("year", "-----------------" + year);
                        Log.e("day", "-----------------" + day);

                        String fulldate = dayOfTheWeek + ", " + day + "/" + intMonth + "/" + year + ", " + string[1];
                        allVideoItems.add(new AllVideoItem(id, videoname, categoryid, audio, photo, duration, fulldate, categoryName, view,false,description,videolink,isodate));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
            progress_load.setVisibility(View.GONE);
            recyclerView_all_audio.setVisibility(View.VISIBLE);
            if (recyclerView_all_audio != null) {
                recyclerVideoAllAdapter = new RecyclerVideoAllAdapter(AllVideoActivity.this, allVideoItems);
                if (recyclerVideoAllAdapter.getItemCount() != 0) {
                    recyclerView_all_audio.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_all_audio.setAdapter(recyclerVideoAllAdapter);
                } else {
                    img_nodata.setVisibility(View.VISIBLE);
                    recyclerView_all_audio.setVisibility(View.GONE);
                }
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        videoid.clear();
        allVideoItems = new ArrayList<>();
        if (CommonMethod.isInternetConnected(AllVideoActivity.this)) {
            page_count = 1;
            new GetAllVideoCategory().execute();
        } else {
            Toast.makeText(AllVideoActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
        }
    }

    private class DeleteVideo extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AllVideoActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < videoid.size(); i++) {
                String value = videoid.get(i);
                responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deletevideo + "?vid=" + value);
            }
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    progressDialog.dismiss();
                    videoid.clear();
                    onResume();
                } else {
                    Toast.makeText(AllVideoActivity.this, "Video not delete.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        }
    }

}
