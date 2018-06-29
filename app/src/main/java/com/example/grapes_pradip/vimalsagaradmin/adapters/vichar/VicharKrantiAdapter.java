package com.example.grapes_pradip.vimalsagaradmin.adapters.vichar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.VicharItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class VicharKrantiAdapter extends RecyclerView.Adapter<VicharKrantiAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<VicharItem> itemArrayList;
    private String id;
    private ProgressDialog progressDialog;


    public VicharKrantiAdapter(Activity activity, ArrayList<VicharItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vichar, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        final VicharItem usersItem = itemArrayList.get(i);


        holder.txt_vichar.setText(CommonMethod.decodeEmoji(usersItem.getContent()));
        holder.txt_date.setText(CommonMethod.decodeEmoji(usersItem.getDate()));



        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
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
                            itemArrayList.remove(holder.getAdapterPosition());
                            id = usersItem.getiD();
                            new DeleteUser().execute();
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

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final TextView txt_date;
        final TextView txt_vichar;
        final TextView txt_edit;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_vichar = (TextView) itemView.findViewById(R.id.txt_vichar);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit_name);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {

           /* Intent intent = new Intent(activity, UserDetailActivity.class);
            intent.putExtra("id", itemArrayList.get(getAdapterPosition()).getID());
            intent.putExtra("name", itemArrayList.get(getAdapterPosition()).getName());
            intent.putExtra("emailid", itemArrayList.get(getAdapterPosition()).getEmailID());
            intent.putExtra("phone", itemArrayList.get(getAdapterPosition()).getPhone());
            intent.putExtra("address", itemArrayList.get(getAdapterPosition()).getAddress());
            intent.putExtra("date", itemArrayList.get(getAdapterPosition()).getRegDate());
            activity.startActivity(intent);*/

        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }


    }

    private class DeleteUser extends AsyncTask<String, Void, String> {
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
            ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("id",id));
            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + "quote/deletevicharkranti",nameValuePairs,activity);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
//                    Toast.makeText(activity, "User delete successfully.", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(activity, "User not delete.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
        }
    }

    private class ApproveUser extends AsyncTask<String, Void, String> {
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
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.approveuser + "?uid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                    Toast.makeText(activity, "User approved.", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(activity, "User not approved.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    private class UpdateUser extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("uid",params[0]));
            nameValuePairs.add(new BasicNameValuePair("name",params[1]));

            responseJSON = JsonParser.postStringResponse(CommonURL.Main_url + "admin/editUser",nameValuePairs,activity);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);

        }
    }
}
