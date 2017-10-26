package com.example.grapes_pradip.vimalsagaradmin.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.video.AddVideoCategoryActivity;
import com.example.grapes_pradip.vimalsagaradmin.adapters.video.RecyclerVideoCategoryAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.video.AllVideoCategoryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.grapes_pradip.vimalsagaradmin.adapters.video.RecyclerVideoCategoryAdapter.videiocatid;


/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class VideoFragment extends Fragment implements View.OnClickListener {
    private View rootview;
    private SwipeRefreshLayout swipe_refresh_information;
    private RecyclerView recyclerView_video_category;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<AllVideoCategoryItem> videoCategoryItems = new ArrayList<>();
    private RecyclerVideoCategoryAdapter recyclerVideoCategoryAdapter;
    private TextView txt_addnew;

    private ProgressDialog progressDialog;
    private ImageView img_nodata;
    private ImageView delete_data;
    ProgressBar progress_load;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.video_fragment, container, false);
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


        return rootview;
    }

    private void refreshContent() {
        videiocatid.clear();
        swipe_refresh_information.setRefreshing(false);
        new GetAllVideoCategory().execute();
    }

    private void findID() {
        swipe_refresh_information = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh_information);
        recyclerView_video_category = (RecyclerView) rootview.findViewById(R.id.recyclerView_video_category);
        recyclerView_video_category.setLayoutManager(linearLayoutManager);
        txt_addnew = (TextView) rootview.findViewById(R.id.txt_addnew);
        img_nodata = (ImageView) rootview.findViewById(R.id.img_nodata);
        delete_data = (ImageView) rootview.findViewById(R.id.delete_data);
        progress_load = (ProgressBar) rootview.findViewById(R.id.progress_load);
    }

    private void idClick() {
        txt_addnew.setOnClickListener(this);
        delete_data.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_addnew:
                Intent intent = new Intent(getActivity(), AddVideoCategoryActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.delete_data:
                if (videiocatid.size() > 0) {

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

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
                            Log.e("array delete", "-------------------" + videiocatid);
                            new DeleteVideoCategory().execute();

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
                    Toast.makeText(getActivity(), "Select information for delete.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class GetAllVideoCategory extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            progress_load.setVisibility(View.VISIBLE);
            recyclerView_video_category.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.getallcategory);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                videoCategoryItems = new ArrayList<>();
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.e("json array", "-------------------" + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("ID");
                        Log.e("id", "---------------" + id);
                        String name = jsonObject1.getString("Name");
                        String categoryIcon = jsonObject1.getString("CategoryIcon");
                        videoCategoryItems.add(new AllVideoCategoryItem(id, name, categoryIcon,false));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
            progress_load.setVisibility(View.GONE);
            recyclerView_video_category.setVisibility(View.VISIBLE);
            if (recyclerView_video_category != null) {
                recyclerVideoCategoryAdapter = new RecyclerVideoCategoryAdapter(getActivity(), videoCategoryItems);
                if (recyclerVideoCategoryAdapter.getItemCount() != 0) {
                    recyclerView_video_category.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_video_category.setAdapter(recyclerVideoCategoryAdapter);
                } else {
                    recyclerView_video_category.setVisibility(View.GONE);
                    img_nodata.setVisibility(View.VISIBLE);
                }
            }


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        videiocatid.clear();
        // put your code here...
        if (CommonMethod.isInternetConnected(getActivity())) {
            new GetAllVideoCategory().execute();
        }
    }

    private class DeleteVideoCategory extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            for (int i = 0; i < videiocatid.size(); i++) {
                String value = videiocatid.get(i);
                responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deleteVideocategory + "?cid=" + value);
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
                    videiocatid.clear();
                    onResume();
                } else {
                    Toast.makeText(getActivity(), "Video category not deleted.", Toast.LENGTH_SHORT).show();
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
