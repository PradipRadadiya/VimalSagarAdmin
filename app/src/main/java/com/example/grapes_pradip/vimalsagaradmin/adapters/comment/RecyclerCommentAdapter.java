package com.example.grapes_pradip.vimalsagaradmin.adapters.comment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.comment.CommentDetailActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.AllCommentItem;
import com.example.grapes_pradip.vimalsagaradmin.retrofit.APIClient;
import com.example.grapes_pradip.vimalsagaradmin.retrofit.ApiInterface;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class RecyclerCommentAdapter extends RecyclerView.Adapter<RecyclerCommentAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<AllCommentItem> itemArrayList;
    private String id;
    private ProgressDialog progressDialog;

    public RecyclerCommentAdapter(Activity activity, ArrayList<AllCommentItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment_fragment, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        final AllCommentItem usersItem = itemArrayList.get(i);
        holder.txt_name.setText(CommonMethod.decodeEmoji(usersItem.getName()));
        holder.txt_email.setText(CommonMethod.decodeEmoji(usersItem.getComment()));
        holder.txt_mobile.setText(CommonMethod.decodeEmoji(usersItem.getTitle()));
        holder.txt_address.setText(CommonMethod.decodeEmoji(usersItem.getModule_name()));
        holder.txt_date.setText(CommonMethod.decodeEmoji(usersItem.getDate()));



        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.update_option);
                dialog.show();
                final EditText edit_old_pwd = (EditText) dialog.findViewById(R.id.edit_option);
                final Button btn_update = (Button) dialog.findViewById(R.id.btn_update);
                edit_old_pwd.setText(usersItem.getComment());
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        usersItem.setComment(edit_old_pwd.getText().toString());
                        notifyItemChanged(i);
                        editCommentPost(usersItem.getCid(),usersItem.getModule_name(),CommonMethod.decodeEmoji(edit_old_pwd.getText().toString()));
                        dialog.dismiss();
                    }
                });
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            }
        });


        holder.txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                alertDialog.setTitle("Confirm Delete...");

                alertDialog.setMessage("Are you sure you want to delete?.");

                alertDialog.setIcon(R.drawable.ic_warning);

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (CommonMethod.isInternetConnected(activity)) {
                            itemArrayList.remove(holder.getAdapterPosition());
//
                            new DeleteComment().execute(usersItem.getCid(),usersItem.getModule_name());

                            notifyItemRemoved(holder.getAdapterPosition());

                        }
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final TextView txt_name;
        final TextView txt_date;
        final TextView txt_email;
        final TextView txt_mobile;
        final TextView txt_address;
        final TextView txt_delete;
        final TextView txt_edit;
        final LinearLayout lin_approve;
        final LinearLayout lin_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_email = (TextView) itemView.findViewById(R.id.txt_email);
            txt_mobile = (TextView) itemView.findViewById(R.id.txt_mobile);
            txt_address = (TextView) itemView.findViewById(R.id.txt_address);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            lin_approve = (LinearLayout) itemView.findViewById(R.id.lin_approve);
            lin_delete = (LinearLayout) itemView.findViewById(R.id.lin_delete);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(activity, CommentDetailActivity.class);
            intent.putExtra("id", itemArrayList.get(getAdapterPosition()).getId());
            intent.putExtra("Title", itemArrayList.get(getAdapterPosition()).getTitle());
            intent.putExtra("Comment", itemArrayList.get(getAdapterPosition()).getComment());
            intent.putExtra("Name", itemArrayList.get(getAdapterPosition()).getName());
            intent.putExtra("module_name", itemArrayList.get(getAdapterPosition()).getModule_name());
            activity.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    private class DeleteComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("cid", params[0]));
            nameValuePairs.add(new BasicNameValuePair("module", params[1]));

            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + "admin/deletecomment", nameValuePairs, activity);
            return responseJSON;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);

        }

    }


    private class EditComment extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("cid", params[0]));
            nameValuePairs.add(new BasicNameValuePair("module", params[1]));

            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + "admin/editcomment", nameValuePairs, activity);
            return responseJSON;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);

        }

    }


    private void editCommentPost(String cid,String module,String comment) {
        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<JsonObject> callApi = apiInterface.editComment(cid,module,comment);
        callApi.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Log.e("reponse", "-----------------" + response.body());
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
            }

        });

    }



}
