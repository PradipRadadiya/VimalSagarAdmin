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
import com.example.grapes_pradip.vimalsagaradmin.activities.competition.AddCompetitionCategoryActivity;
import com.example.grapes_pradip.vimalsagaradmin.adapters.competition.RecyclerCompetitionCategoryAdapter;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.competition.CompetitionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

import static com.example.grapes_pradip.vimalsagaradmin.adapters.competition.RecyclerCompetitionCategoryAdapter.compcatid;


@SuppressWarnings("ALL")
public class CompetitionFragment extends Fragment implements View.OnClickListener {
    private View rootview;
    private SwipeRefreshLayout swipe_refresh_information;
    private RecyclerView recyclerView_comptition_category;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<CompetitionItem> competitionItemArrayList = new ArrayList<>();
    private RecyclerCompetitionCategoryAdapter recyclerCompetitionCategoryAdapter;
    private TextView txt_addnew;
    private ProgressDialog progressDialog;
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
    private ImageView img_nodata;
    private ImageView delete_data;
    ProgressBar progress_load;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.competition_fragment, container, false);
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

       /* recyclerView_comptition_category.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
//                                                                                 new GetAllCompetitionCategory().execute();
                                                                             } else {
                                                                                 //internet not connected
                                                                             }
                                                                             loading = true;
                                                                         }
                                                                     }
                                                                 }

                                                             }

        );
*/

        return rootview;
    }

    private void refreshContent() {
        compcatid.clear();
        swipe_refresh_information.setRefreshing(false);
        new GetAllCompetitionCategory().execute();
    }

    private void findID() {
        swipe_refresh_information = (SwipeRefreshLayout) rootview.findViewById(R.id.swipe_refresh);
        recyclerView_comptition_category = (RecyclerView) rootview.findViewById(R.id.recyclerView_comptition_category);
        recyclerView_comptition_category.setLayoutManager(linearLayoutManager);
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
                Intent intent = new Intent(getActivity(), AddCompetitionCategoryActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.delete_data:
                if (compcatid.size() > 0) {

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
                            Log.e("array delete", "-------------------" + compcatid);
                            new DeleteCompetitionCategory().execute();

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
                    Toast.makeText(getActivity(), "Select competition for delete.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class GetAllCompetitionCategory extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.show();
//            progressDialog.setCancelable(false);
            progress_load.setVisibility(View.VISIBLE);
            recyclerView_comptition_category.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("page", "1"));
            nameValuePairs.add(new BasicNameValuePair("psize", "1000"));


            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + "competition/getallcompetition/", nameValuePairs, getActivity());
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response competition", "---------------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                competitionItemArrayList = new ArrayList<>();
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                   /* if (jsonArray.length() < 30 || jsonArray.length() == 0) {
                        flag_scroll = true;
                        Log.e("length_array_news", flag_scroll + "" + "<30===OR(0)===" + jsonArray.length());
                    }*/
                    Log.e("json array", "-------------------" + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        Log.e("id", "---------------" + id);
                        String title = jsonObject1.getString("title");
                        String rules = jsonObject1.getString("rules");
                        String date = jsonObject1.getString("date");
                        String time = jsonObject1.getString("time");
                        String total_question = jsonObject1.getString("total_question");
                        String total_minute = jsonObject1.getString("total_minute");
                        String is_open = jsonObject1.getString("status");
                        String participated_users = jsonObject1.getString("participated_users");
                        String status = jsonObject1.getString("status");

                        competitionItemArrayList.add(new CompetitionItem(id, title, rules, date, time, total_question, total_minute, is_open, participated_users, false,status));

                    }

                    Log.e("size","--------------------"+competitionItemArrayList.size());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
            progress_load.setVisibility(View.GONE);
            recyclerView_comptition_category.setVisibility(View.VISIBLE);
            if (recyclerView_comptition_category != null) {
                recyclerCompetitionCategoryAdapter = new RecyclerCompetitionCategoryAdapter(getActivity(), competitionItemArrayList);
                if (recyclerCompetitionCategoryAdapter.getItemCount() != 0) {
                    recyclerView_comptition_category.setVisibility(View.VISIBLE);
                    img_nodata.setVisibility(View.GONE);
                    recyclerView_comptition_category.setAdapter(recyclerCompetitionCategoryAdapter);
                } else {
                    img_nodata.setVisibility(View.VISIBLE);
                    recyclerView_comptition_category.setVisibility(View.GONE);
                }
            }

        }
    }

    private class DeleteCompetitionCategory extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.progressmsg));
//            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < compcatid.size(); i++) {
                String value = compcatid.get(i);
                responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deleteCompetitioncategory + "?cid=" + value);
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
                    compcatid.clear();
                    onResume();
                } else {
                    Toast.makeText(getActivity(), "Competition not deleted.", Toast.LENGTH_SHORT).show();
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
//        compcatid.clear();
        /*competitionItemArrayList = new ArrayList<>();
        page_count = 1;
        if (CommonMethod.isInternetConnected(getActivity())) {
            new GetAllCompetitionCategory().execute();
        }*/

        competitionItemArrayList = new ArrayList<>();
        page_count = 1;
        if (CommonMethod.isInternetConnected(getActivity())) {
            new GetAllCompetitionCategory().execute();
        }

    }
}
