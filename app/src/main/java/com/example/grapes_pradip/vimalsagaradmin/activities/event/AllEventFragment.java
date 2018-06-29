package com.example.grapes_pradip.vimalsagaradmin.activities.event;

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
import com.example.grapes_pradip.vimalsagaradmin.adapters.event.RecyclerEventAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.event.AllIEventItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import static com.example.grapes_pradip.vimalsagaradmin.adapters.event.RecyclerEventAdapter.eventid;



@SuppressWarnings("ALL")
public class AllEventFragment extends AppCompatActivity implements View.OnClickListener {
    private View rootview;
    private SwipeRefreshLayout swipe_refresh_information;
    private RecyclerView recyclerView_event;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<AllIEventItem> allIEventItems = new ArrayList<>();
    private RecyclerEventAdapter recyclerEventAdapter;
    private TextView txt_addnew;
    //page count
    private int page_count = 1;
    int draw = 1;
    int total_Record;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private ProgressDialog progressDialog;
    private ImageView img_nodata;
    private ImageView delete_info;
    ProgressBar progress_load;
    private String cid;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_fragment);
        Intent intent = getIntent();
        cid = intent.getStringExtra("event_category_id");
        title = intent.getStringExtra("title");
        linearLayoutManager = new LinearLayoutManager(AllEventFragment.this);
        findID();
        idClick();

        TextView txt_header = (TextView) findViewById(R.id.txt_header);
        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        txt_header.setText(CommonMethod.decodeEmoji(title));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        swipe_refresh_information.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CommonMethod.isInternetConnected(AllEventFragment.this)) {
                    refreshContent();
                } else {
                    swipe_refresh_information.setRefreshing(false);
                }
            }
        });

    }

    /*   @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
           rootview = inflater.inflate(R.layout.event_fragment, container, false);
           linearLayoutManager = new LinearLayoutManager(getActivity());
           findID();
           idClick();

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

           *//*recyclerView_event.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                                               if (CommonMethod.isInternetConnected(getActivity())) {

                                                                   Log.e("total count", "--------------------" + page_count);

                                                                   page_count++;
                                                                   new GetAllEvent().execute();
                                                               } else {
                                                                   //internet not connected
                                                               }
                                                               loading = true;

                                                           }
                                                       }
                                                   }

                                               }

        );*//*
        return rootview;
    }
*/
    private void refreshContent() {
        eventid.clear();
        allIEventItems = new ArrayList<>();
        swipe_refresh_information.setRefreshing(false);
        new GetAllEvent().execute();
    }

    private void findID() {
        swipe_refresh_information = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_information);
        recyclerView_event = (RecyclerView) findViewById(R.id.recyclerView_event);
        recyclerView_event.setLayoutManager(linearLayoutManager);
        txt_addnew = (TextView) findViewById(R.id.txt_addnew);
        img_nodata = (ImageView) findViewById(R.id.img_nodata);
        delete_info = (ImageView) findViewById(R.id.delete_info);
        progress_load = (ProgressBar) findViewById(R.id.progress_load);
    }

    private void idClick() {
        txt_addnew.setOnClickListener(this);
        delete_info.setOnClickListener(this);
    }

/*

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
                        Fragment fr = null;
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
*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_addnew:
                Intent intent = new Intent(AllEventFragment.this, AddEventActivity.class);
                intent.putExtra("cid",cid);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.delete_info:
                if (eventid.size() > 0) {

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AllEventFragment.this);

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
                            Log.e("array delete", "-------------------" + eventid);
                            new DeleteEvent().execute();

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
                    Toast.makeText(AllEventFragment.this, "Select Information for delete.", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    private class GetAllEvent extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            progress_load.setVisibility(View.VISIBLE);
            recyclerView_event.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getAllevent +"?cid="+cid+"&page=" + page_count + "&psize=1000");
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
                 /*   if (jsonArray.length() < 30 || jsonArray.length() == 0) {
                        flag_scroll = true;
                        Log.e("length_array_news", flag_scroll + "" + "<30===OR(0)===" + jsonArray.length());
                    }*/
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        Log.e("id", "---------------" + id);
                        String title = jsonObject1.getString("Title");
                        String description = jsonObject1.getString("Description");
                        String address = jsonObject1.getString("Address");
                        String date = jsonObject1.getString("Date");
                        String audio = jsonObject1.getString("Audio");
                        String audioimage = jsonObject1.getString("AudioImage");
                        String videolink = jsonObject1.getString("VideoLink");
                        String video = jsonObject1.getString("Video");
                        String videoimage = jsonObject1.getString("VideoImage");
                        String photo = jsonObject1.getString("Photo");
                        String CategoryID = jsonObject1.getString("CategoryID");
                        String view = jsonObject1.getString("View");

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


                        allIEventItems.add(new AllIEventItem(id, title, description, address, fulldate, audio, audioimage, videolink, video, videoimage, photo, view, false));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }

            progress_load.setVisibility(View.GONE);
            recyclerView_event.setVisibility(View.VISIBLE);
            if (recyclerView_event != null) {
                recyclerEventAdapter = new RecyclerEventAdapter(AllEventFragment.this, allIEventItems);

                Log.e("size","---------------"+allIEventItems);
                if (recyclerEventAdapter.getItemCount() != 0) {
                    Log.e("if size","---------------"+allIEventItems);
                    recyclerView_event.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_event.setAdapter(recyclerEventAdapter);
                }

                else {
                    Log.e("else size","---------------"+allIEventItems);
                    recyclerView_event.setVisibility(View.GONE);
                    img_nodata.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    private class DeleteEvent extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AllEventFragment.this);
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < eventid.size(); i++) {
                String value = eventid.get(i);
                responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.delete_event + "?eid=" + value);
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
                    Toast.makeText(AllEventFragment.this, "Event deleted.", Toast.LENGTH_SHORT).show();
                    eventid.clear();
                    onResume();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AllEventFragment.this, "Event not deleted.", Toast.LENGTH_SHORT).show();
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
    public void onResume() {
        super.onResume();
        // put your code here...
        eventid.clear();
        allIEventItems = new ArrayList<>();
        page_count = 1;
        if (CommonMethod.isInternetConnected(AllEventFragment.this)) {
            new GetAllEvent().execute();
        }
    }
}
