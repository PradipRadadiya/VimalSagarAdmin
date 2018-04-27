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
import com.example.grapes_pradip.vimalsagaradmin.adapters.users.RecyclerUsersAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.user.UsersItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class UserFragment extends Fragment {
    private View rootview;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recyclerView_users;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<UsersItem> usersItems = new ArrayList<>();
    private RecyclerUsersAdapter recyclerUsersAdapter;
    //page count
    private int page_count = 1;
    private boolean flag_scroll = false;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private final int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem;
    private int visibleItemCount;
    private int totalItemCount;
    private ProgressDialog progressDialog;
    private ImageView img_nodata;
    ProgressBar progress_load;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.user_fragment, container, false);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        findID();
//        linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView_users = (RecyclerView) rootview.findViewById(R.id.recyclerView_users);
        recyclerView_users.setLayoutManager(linearLayoutManager);

        recyclerUsersAdapter = new RecyclerUsersAdapter(getActivity(),usersItems);
        recyclerView_users.setAdapter(recyclerUsersAdapter);

//        usersItems = new ArrayList<>();

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

        recyclerView_users.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                   @Override
                                                   public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                                       super.onScrolled(recyclerView, dx, dy);

                                                       visibleItemCount = recyclerView.getChildCount();
                                                       totalItemCount = linearLayoutManager.getItemCount();
                                                       firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                                                       if (flag_scroll) {
//                                                                 Log.e("flag-Scroll", flag_scroll + "");
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
                                                                   new GetAllUser().execute();
                                                               } else {
                                                                   //internet not connected
                                                               }
                                                               loading = true;


                                                           }

                                                       }
                                                   }

                                               }

        );


        if (CommonMethod.isInternetConnected(getActivity())) {
            new GetAllUser().execute();
        }
        return rootview;
    }

    private void refreshContent() {

        page_count = 1;
        swipe_refresh.setRefreshing(false);
        new GetAllUser().execute();
    }

    private void findID() {
        swipe_refresh = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh);
//        recyclerView_users = (RecyclerView) rootview.findViewById(R.id.recyclerView_users);
//        recyclerView_users.setLayoutManager(linearLayoutManager);
        img_nodata = (ImageView) rootview.findViewById(R.id.img_nodata);
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


    private class GetAllUser extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            progress_load.setVisibility(View.VISIBLE);
//            recyclerView_users.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallusers + "?page=" + page_count + "&psize=20");
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
//                    usersItems=new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("json array", "-------------------" + jsonArray);
                   /* if (jsonArray.length() < 20 || jsonArray.length() == 0) {
                        flag_scroll = true;
                        Log.e("length_array_news", flag_scroll + "" + "<30===OR(0)===" + jsonArray.length());
                    }*/
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        Log.e("id", "---------------" + id);
                        String deviceid = jsonObject1.getString("DeviceID");
                        String devicetoken = jsonObject1.getString("DeviceToken");
                        String name = jsonObject1.getString("Name");
                        String email = jsonObject1.getString("EmailID");
                        String phone = jsonObject1.getString("Phone");
                        String address = jsonObject1.getString("Address");
                        String is_active = jsonObject1.getString("Is_Active");
                        String date = jsonObject1.getString("RegDate");

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

                        String fulldate = dayOfTheWeek + ", " + day + "/" + intMonth + "/" + year + " " + string[1];
                        usersItems.add(new UsersItem(email, address, is_active, deviceid, devicetoken, phone, fulldate, id, name));
                    }
                    recyclerUsersAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
            progress_load.setVisibility(View.GONE);
            /*recyclerView_users.setVisibility(View.VISIBLE);
            recyclerView_users.setAdapter(recyclerUsersAdapter);
            if (recyclerView_users != null) {

                if (recyclerUsersAdapter.getItemCount() != 0) {
                    recyclerView_users.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
//                    recyclerView_users.setAdapter(recyclerUsersAdapter);
                } else {
                    recyclerView_users.setVisibility(View.GONE);
                    img_nodata.setVisibility(View.VISIBLE);
                }
            }*/

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...

    }
}
