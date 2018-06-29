package com.example.grapes_pradip.vimalsagaradmin.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.note.AddNote;
import com.example.grapes_pradip.vimalsagaradmin.adapters.note.RecyclerNoteAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.note.NoteItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class Notes extends Fragment {
    private View rootview;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recyclerView_users;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<NoteItem> usersItems = new ArrayList<>();
    private RecyclerNoteAdapter recyclerUsersAdapter;
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
    private TextView txt_addnew;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.notes_fragment, container, false);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        findID();
//        linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView_users = (RecyclerView) rootview.findViewById(R.id.recyclerView_users);
        recyclerView_users.setLayoutManager(linearLayoutManager);

        recyclerUsersAdapter = new RecyclerNoteAdapter(getActivity(), usersItems);
        recyclerView_users.setAdapter(recyclerUsersAdapter);

//        usersItems = new ArrayList<>();

        txt_addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNote.class);
                startActivity(intent);
            }
        });

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


        return rootview;
    }

    private void refreshContent() {

        usersItems = new ArrayList<>();
        page_count = 1;
        swipe_refresh.setRefreshing(false);
        new GetAllNotes().execute();

    }

    private void findID() {
        txt_addnew = (TextView) rootview.findViewById(R.id.txt_addnew);

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


    private class GetAllNotes extends AsyncTask<String, Void, String> {
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
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + "competition/getcompetitionnote");
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
//                    usersItems=new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        Log.e("id", "---------------" + id);
                        String title = jsonObject1.getString("title");
                        String description = jsonObject1.getString("description");
                        String date = jsonObject1.getString("date");
                        String time = jsonObject1.getString("time");


                        usersItems.add(new NoteItem(id, title, description, date, time));

                    }
//                    recyclerUsersAdapter.notifyDataSetChanged();

                    recyclerUsersAdapter = new RecyclerNoteAdapter(getActivity(), usersItems);
                    recyclerView_users.setAdapter(recyclerUsersAdapter);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progress_load.setVisibility(View.GONE);
            if (usersItems.isEmpty()){
                img_nodata.setVisibility(View.VISIBLE);
            }else{
                img_nodata.setVisibility(View.GONE);
            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        usersItems.clear();
        if (CommonMethod.isInternetConnected(getActivity())) {
            new GetAllNotes().execute();
        }

    }
}
