package com.example.grapes_pradip.vimalsagaradmin.adapters.bypeople;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.bypeople.ByPeopleDetailActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.bypeople.ByPeopleItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerByPeopleAdapter extends RecyclerView.Adapter<RecyclerByPeopleAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<ByPeopleItem> itemArrayList;
    private String id;
    private ProgressDialog progressDialog;
    ArrayList<String> peopleid = new ArrayList<>();

    public RecyclerByPeopleAdapter(Activity activity, ArrayList<ByPeopleItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bypeople, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final ByPeopleItem byPeopleItem = itemArrayList.get(i);

        if (byPeopleItem.getIsApproved().equalsIgnoreCase("R")) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

            // Setting Dialog Title
            alertDialog.setTitle("Confirm Delete...");

            // Setting Dialog Message
            alertDialog.setMessage("Are you sure you want to delete?.");

            // Setting Icon to Dialog
            alertDialog.setIcon(R.drawable.ic_warning);

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                    id = byPeopleItem.getID();
                    new DeleteByPeople().execute();
                    Log.e("Rejected", "-----------");
                    itemArrayList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());


                    // Write your code here to invoke YES event
//                            Toast.makeText(activity, "You clicked on YES", Toast.LENGTH_SHORT).show();
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


        }
        if (byPeopleItem.getIsApproved().equalsIgnoreCase("1")) {
            holder.lin_approve.setVisibility(View.GONE);
            holder.lin_delete.setVisibility(View.VISIBLE);
            holder.txt_title.setText(byPeopleItem.getTitle());
            holder.txt_post.setText(byPeopleItem.getPost());
            holder.txt_unm.setText(byPeopleItem.getName());
            holder.txt_date.setText(byPeopleItem.getDate());
            holder.txt_views.setText(byPeopleItem.getView());

            holder.lin_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonMethod.isInternetConnected(activity)) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                        // Setting Dialog Title
                        alertDialog.setTitle("Confirm Delete...");

                        // Setting Dialog Message
                        alertDialog.setMessage("Are you sure you want to delete?.");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.ic_warning);

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                id = byPeopleItem.getID();
                                new DeleteByPeople().execute();
                                itemArrayList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());

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
                    }
                }
            });

        }
        if (byPeopleItem.getIsApproved().equalsIgnoreCase("0")) {
            holder.lin_approve.setVisibility(View.VISIBLE);
            holder.lin_delete.setVisibility(View.VISIBLE);
            holder.txt_title.setText(byPeopleItem.getTitle());
            holder.txt_post.setText(byPeopleItem.getPost());
            holder.txt_unm.setText(byPeopleItem.getName());
            holder.txt_date.setText(byPeopleItem.getDate());
            holder.txt_views.setText(byPeopleItem.getView());

            holder.lin_approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonMethod.isInternetConnected(activity)) {
                        id = byPeopleItem.getID();
                        new ApprovePost().execute();
                        holder.lin_approve.setVisibility(View.GONE);
                        holder.lin_delete.setVisibility(View.VISIBLE);
                    }
                }
            });
            holder.lin_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                    // Setting Dialog Title
                    alertDialog.setTitle("Confirm Delete...");
                    // Setting Dialog Message
                    alertDialog.setMessage("Are you sure you want to delete?.");
                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.ic_warning);
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (CommonMethod.isInternetConnected(activity)) {
                                id = byPeopleItem.getID();
                                new DeleteByPeople().execute();
                                itemArrayList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());


                            }

                            // Write your code here to invoke YES event
//                            Toast.makeText(activity, "You clicked on YES", Toast.LENGTH_SHORT).show();
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


                }
            });

        }


    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final TextView txt_views;
        final TextView txt_title;
        final TextView txt_post;
        final TextView txt_date;
        final TextView txt_unm;
        final LinearLayout lin_approve;
        final LinearLayout lin_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_views = (TextView) itemView.findViewById(R.id.txt_views);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_post = (TextView) itemView.findViewById(R.id.txt_post);
            txt_unm = (TextView) itemView.findViewById(R.id.txt_unm);
            lin_approve = (LinearLayout) itemView.findViewById(R.id.lin_approve);
            lin_delete = (LinearLayout) itemView.findViewById(R.id.lin_delete);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(), ByPeopleDetailActivity.class);
            intent.putExtra("click_action", "");
            intent.putExtra("ID", itemArrayList.get(getAdapterPosition()).getID());
            intent.putExtra("Title", itemArrayList.get(getAdapterPosition()).getTitle());
            intent.putExtra("Post", itemArrayList.get(getAdapterPosition()).getPost());
            intent.putExtra("Audio", itemArrayList.get(getAdapterPosition()).getAudio());
            intent.putExtra("AudioImage", itemArrayList.get(getAdapterPosition()).getAudioImage());
            intent.putExtra("Video", itemArrayList.get(getAdapterPosition()).getVideo());
            intent.putExtra("VideoImage", itemArrayList.get(getAdapterPosition()).getVideoImage());
            intent.putExtra("VideoLink", itemArrayList.get(getAdapterPosition()).getVideoLink());
            intent.putExtra("UserID", itemArrayList.get(getAdapterPosition()).getUserID());
            intent.putExtra("Is_Approved", itemArrayList.get(getAdapterPosition()).getIsApproved());
            intent.putExtra("Date", itemArrayList.get(getAdapterPosition()).getDate());
            intent.putExtra("Name", itemArrayList.get(getAdapterPosition()).getName());
            intent.putExtra("Photo", itemArrayList.get(getAdapterPosition()).getPhoto());
            intent.putExtra("view", itemArrayList.get(getAdapterPosition()).getView());
            v.getContext().startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


        }


        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }


    }

    private class DeleteByPeople extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(activity);
//            progressDialog.setMessage(activity.getResources().getString(R.string.progressmsg));
//            progressDialog.setCancelable(false);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deletepost + "?pid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
//                    Toast.makeText(activity, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                    notifyDataSetChanged();

                } else {
                    Toast.makeText(activity, "Post not delete.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    private class ApprovePost extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(activity.getResources().getString(R.string.progressmsg));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.approvepost + "?pid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(activity, "Post approved successfully.", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(activity, "Post not approved.", Toast.LENGTH_SHORT).show();
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
