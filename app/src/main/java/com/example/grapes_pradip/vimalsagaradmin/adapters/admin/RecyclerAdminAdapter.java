package com.example.grapes_pradip.vimalsagaradmin.adapters.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexandrius.accordionswipelayout.library.SwipeLayout;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.admin.EditAdminActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.common.SharedPreferencesClass;
import com.example.grapes_pradip.vimalsagaradmin.model.admin.AllAdminItem;

import java.util.ArrayList;


@SuppressWarnings("ALL")
public class RecyclerAdminAdapter extends RecyclerView.Adapter<RecyclerAdminAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<AllAdminItem> itemArrayList;
    private String id;
    SharedPreferencesClass sharedPreferencesClass;

    public RecyclerAdminAdapter(Activity activity, ArrayList<AllAdminItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sub_admin, viewGroup, false);
        sharedPreferencesClass = new SharedPreferencesClass(activity);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final AllAdminItem adminItem = itemArrayList.get(i);

        if (sharedPreferencesClass.getRole().equalsIgnoreCase("main")) {

            holder.txt_usename.setText(adminItem.getUsername());
            holder.txt_date.setText(adminItem.getDate());
            holder.txt_email.setText(adminItem.getEmail());
            holder.txt_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, EditAdminActivity.class);
                    intent.putExtra("role", adminItem.getRole());
                    intent.putExtra("name", adminItem.getName());
                    intent.putExtra("email", adminItem.getEmail());
                    intent.putExtra("username", adminItem.getUsername());
                    intent.putExtra("mobile", adminItem.getMobile());
                    intent.putExtra("hiddenpwd", adminItem.getPassword());
                    intent.putExtra("adminid", adminItem.getId());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            holder.txt_delete.setOnClickListener(new View.OnClickListener() {
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

                            id = itemArrayList.get(holder.getAdapterPosition()).getId();
                            Log.e("id", "-----------------------" + id);
                            new DeleteAdmin().execute();
                            int pos = holder.getAdapterPosition();
                            itemArrayList.remove(pos);
                            notifyItemRemoved(pos);

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
        } else {
            holder.txt_usename.setText(adminItem.getUsername());
            holder.txt_date.setText(adminItem.getDate());
            holder.txt_email.setText(adminItem.getEmail());
            holder.txt_edit.setVisibility(View.GONE);
            holder.txt_delete.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, SwipeLayout.OnSwipeItemClickListener {

        final TextView txt_usename;
        final TextView txt_email;
        final TextView txt_date;
        final TextView txt_edit;
        final TextView txt_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_usename = (TextView) itemView.findViewById(R.id.txt_usename);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_email = (TextView) itemView.findViewById(R.id.txt_email);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ((SwipeLayout) itemView).setOnSwipeItemClickListener(this);
        }

        @Override
        public void onClick(View v) {

            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_subadmin_detail);
            dialog.show();
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            final TextView txt_id = (TextView) dialog.findViewById(R.id.txt_id);
            final TextView txt_name = (TextView) dialog.findViewById(R.id.txt_name);
            final TextView txt_email = (TextView) dialog.findViewById(R.id.txt_email);
            final TextView txt_mobile = (TextView) dialog.findViewById(R.id.txt_mobile);
            final TextView txt_usename = (TextView) dialog.findViewById(R.id.txt_usename);
            final TextView txt_password = (TextView) dialog.findViewById(R.id.txt_password);
            final TextView last_update = (TextView) dialog.findViewById(R.id.last_update);
            final TextView txt_role = (TextView) dialog.findViewById(R.id.txt_role);
            final ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
            txt_id.setText(itemArrayList.get(getAdapterPosition()).getId());
            txt_name.setText(itemArrayList.get(getAdapterPosition()).getName());
            txt_email.setText(itemArrayList.get(getAdapterPosition()).getEmail());
            txt_mobile.setText(itemArrayList.get(getAdapterPosition()).getMobile());
            txt_usename.setText(itemArrayList.get(getAdapterPosition()).getUsername());
            txt_password.setText(itemArrayList.get(getAdapterPosition()).getPassword());
            last_update.setText(itemArrayList.get(getAdapterPosition()).getDate());
            txt_role.setText(itemArrayList.get(getAdapterPosition()).getRole());
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onSwipeItemClick(boolean left, final int index) {
            if (left) {
//                Toast.makeText(itemView.getContext(), "Left", Toast.LENGTH_SHORT).show();
            } else {
                if (index == 0) {
//                    Toast.makeText(itemView.getContext(), "Edit", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity, EditAdminActivity.class);
                    intent.putExtra("role", itemArrayList.get(getAdapterPosition()).getRole());
                    intent.putExtra("name", itemArrayList.get(getAdapterPosition()).getName());
                    intent.putExtra("email", itemArrayList.get(getAdapterPosition()).getEmail());
                    intent.putExtra("username", itemArrayList.get(getAdapterPosition()).getUsername());
                    intent.putExtra("mobile", itemArrayList.get(getAdapterPosition()).getMobile());
                    intent.putExtra("hiddenpwd", itemArrayList.get(getAdapterPosition()).getPassword());
                    intent.putExtra("adminid", itemArrayList.get(getAdapterPosition()).getId());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (index == 1) {

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
                            id = itemArrayList.get(getAdapterPosition()).getId();
                            Log.e("id", "-----------------------" + id);
                            new DeleteAdmin().execute();
                            Log.e("pos", "----------" + index);
                            int pos = getAdapterPosition();
                            itemArrayList.remove(pos);
                            notifyItemRemoved(pos);
//                            Toast.makeText(itemView.getContext(), "Trash" + id, Toast.LENGTH_SHORT).show();
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
            }
        }
    }

    private class DeleteAdmin extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deleteAdmin + "?adminid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
        }
    }
}
