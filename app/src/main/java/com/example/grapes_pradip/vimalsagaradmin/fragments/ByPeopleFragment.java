package com.example.grapes_pradip.vimalsagaradmin.fragments;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.adapters.bypeople.RecyclerByPeopleAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.bypeople.ByPeopleItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


@SuppressWarnings("ALL")
public class ByPeopleFragment extends Fragment {
    private View rootview;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recyclerView_bypeople;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ByPeopleItem> byPeopleItemArrayList = new ArrayList<>();
    private RecyclerByPeopleAdapter recyclerByPeopleAdapter;
    private ImageView img_nodata;
    private ImageView delete_data;
    private ProgressDialog progressDialog;
    ProgressBar progress_load;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.bypeople_fragment, container, false);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        findID();

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CommonMethod.isInternetConnected(getActivity())) {
                    refreshContent();
                } else {
                    swipe_refresh.setRefreshing(false);
                }
            }
        });

        delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rootview;
    }

    private void refreshContent() {

        swipe_refresh.setRefreshing(false);
        new GetAllPost().execute();
    }

    private void findID() {
        swipe_refresh = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh);
        recyclerView_bypeople = (RecyclerView) rootview.findViewById(R.id.recyclerView_bypeople);
        img_nodata = (ImageView) rootview.findViewById(R.id.img_nodata);
        delete_data = (ImageView) rootview.findViewById(R.id.delete_data);
        recyclerView_bypeople.setLayoutManager(linearLayoutManager);
        progress_load = (ProgressBar) rootview.findViewById(R.id.progress_load);
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


    private class GetAllPost extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            progress_load.setVisibility(View.VISIBLE);
            recyclerView_bypeople.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallposts);
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
                    byPeopleItemArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        String title = jsonObject1.getString("Title");
                        String post = jsonObject1.getString("Post");
                        String Audio = jsonObject1.getString("Audio");
                        String audioimage = jsonObject1.getString("AudioImage");
                        String video = jsonObject1.getString("Video");
                        String videoimage = jsonObject1.getString("VideoImage");
                        String videolink = jsonObject1.getString("VideoLink");
                        String uid = jsonObject1.getString("UserID");
                        String is_approved = jsonObject1.getString("Is_Approved");
                        String date = jsonObject1.getString("Date");
                        String name = jsonObject1.getString("Name");
                        String photo = jsonObject1.getString("Photo");
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


                        byPeopleItemArrayList.add(new ByPeopleItem(post, title, videoimage, photo, fulldate, name, audioimage, videoimage, is_approved, video, uid, id, Audio, view));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
            progress_load.setVisibility(View.GONE);
            recyclerView_bypeople.setVisibility(View.VISIBLE);
            if (recyclerView_bypeople != null) {
                recyclerByPeopleAdapter = new RecyclerByPeopleAdapter(getActivity(), byPeopleItemArrayList);
                if (recyclerByPeopleAdapter.getItemCount() != 0) {
                    recyclerView_bypeople.setAdapter(recyclerByPeopleAdapter);
                    recyclerView_bypeople.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                } else {
                    recyclerView_bypeople.setVisibility(View.GONE);
                    img_nodata.setVisibility(View.VISIBLE);
                }
            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...

        if (CommonMethod.isInternetConnected(getActivity())) {
            new GetAllPost().execute();
        }
    }
}
