package com.example.grapes_pradip.vimalsagaradmin.adapters.event;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.information.CommentList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerEventCommentAdapter extends RecyclerView.Adapter<RecyclerEventCommentAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<CommentList> itemArrayList;
    private String id;
    private ProgressDialog progressDialog;

    public RecyclerEventCommentAdapter(Activity activity, ArrayList<CommentList> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final CommentList commentList = itemArrayList.get(i);


        if (commentList.getIsApproved().equalsIgnoreCase("1")) {
            holder.lin_approve.setVisibility(View.GONE);
            holder.lin_delete.setVisibility(View.GONE);
            holder.rejected.setVisibility(View.GONE);

            if (commentList.getName().equalsIgnoreCase("null")) {
                holder.txt_unm.setText("Admin");
            } else {
                holder.txt_unm.setText(commentList.getName());
            }
            holder.txt_post.setText(commentList.getComment());
            holder.txt_date.setText(commentList.getDate());

            holder.lin_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonMethod.isInternetConnected(activity)) {
                        itemArrayList.remove(holder.getAdapterPosition());
                        id = commentList.getID();
//                        new RejectPost().execute();
                    }
                }
            });

        }
        if (commentList.getIsApproved().equalsIgnoreCase("0")) {

            holder.lin_approve.setVisibility(View.VISIBLE);
            holder.lin_delete.setVisibility(View.VISIBLE);
            holder.rejected.setVisibility(View.GONE);
            holder.txt_unm.setText(commentList.getName());
            holder.txt_post.setText(commentList.getComment());
            holder.txt_date.setText(commentList.getDate());
            holder.lin_approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonMethod.isInternetConnected(activity)) {
                        id = commentList.getID();
                        new ApprovePost().execute();
                        holder.lin_approve.setVisibility(View.GONE);
                        holder.lin_delete.setVisibility(View.GONE);
                        holder.rejected.setVisibility(View.GONE);
                    }
                }
            });
            holder.lin_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CommonMethod.isInternetConnected(activity)) {
//                        itemArrayList.remove(holder.getAdapterPosition());
                        id = commentList.getID();
                        new RejectPost().execute();
                        holder.lin_approve.setVisibility(View.GONE);
                        holder.lin_delete.setVisibility(View.GONE);
                        holder.rejected.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
        if (commentList.getIsApproved().equalsIgnoreCase("R")) {
            Log.e("Rejected", "-----------");
            holder.lin_approve.setVisibility(View.GONE);
            holder.lin_delete.setVisibility(View.GONE);
            holder.rejected.setVisibility(View.VISIBLE);
            holder.txt_unm.setText(commentList.getName());
            holder.txt_post.setText(commentList.getComment());
            holder.txt_date.setText(commentList.getDate());
        }

    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView txt_unm;
        final TextView txt_post;
        final TextView txt_date;
        final TextView rejected;
        final LinearLayout lin_approve;
        final LinearLayout lin_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_unm = (TextView) itemView.findViewById(R.id.txt_unm);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_post = (TextView) itemView.findViewById(R.id.txt_post);
            txt_unm = (TextView) itemView.findViewById(R.id.txt_unm);
            rejected = (TextView) itemView.findViewById(R.id.rejected);
            lin_approve = (LinearLayout) itemView.findViewById(R.id.lin_approve);
            lin_delete = (LinearLayout) itemView.findViewById(R.id.lin_delete);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }


        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
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
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.approvecommentevent + "?commentid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(activity, "Comment approved." , Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(activity, "Comment not approved." , Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    private class RejectPost extends AsyncTask<String, Void, String> {
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
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.rejectcommentevent + "?commentid=" + id);
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
                    Toast.makeText(activity, "Comment not rejected.", Toast.LENGTH_SHORT).show();
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
