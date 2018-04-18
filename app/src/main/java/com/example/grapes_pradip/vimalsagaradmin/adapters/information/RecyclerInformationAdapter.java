package com.example.grapes_pradip.vimalsagaradmin.adapters.information;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexandrius.accordionswipelayout.library.SwipeLayout;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.information.EditInformationActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.information.InfomationDetailActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.information.AllInformationItem;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerInformationAdapter extends RecyclerView.Adapter<RecyclerInformationAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<AllInformationItem> itemArrayList;
    private String id;
    public static final ArrayList<String> infoid = new ArrayList<>();

    public RecyclerInformationAdapter(Activity activity, ArrayList<AllInformationItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_information, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final AllInformationItem orderItem = itemArrayList.get(i);
        holder.txt_title.setText(CommonMethod.decodeEmoji(orderItem.getTitle()));
        holder.txt_description.setText(CommonMethod.decodeEmoji(orderItem.getDiscription()));
        holder.txt_date.setText(CommonMethod.decodeEmoji(orderItem.getDate()));
        holder.txt_location.setText(CommonMethod.decodeEmoji(orderItem.getAddress()));
        holder.txt_views.setText(CommonMethod.decodeEmoji(orderItem.getView()));
        ((SwipeLayout) holder.itemView).setItemState(SwipeLayout.ITEM_STATE_COLLAPSED, true);
        holder.check_delete.setChecked(orderItem.isSelected() ? true : false);


        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditInformationActivity.class);
                intent.putExtra("click_action", "");
                intent.putExtra("info_id", itemArrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("title", itemArrayList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("description", itemArrayList.get(holder.getAdapterPosition()).getDiscription());
                intent.putExtra("address", itemArrayList.get(holder.getAdapterPosition()).getAddress());
                intent.putExtra("date", itemArrayList.get(holder.getAdapterPosition()).getDate());
                intent.putExtra("view", itemArrayList.get(holder.getAdapterPosition()).getView());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        holder.txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

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
                        new DeleteInformation().execute();
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
                        notifyDataSetChanged();
                    }
                });

                alertDialog.show();

            }
        });
//        holder.check_delete.setChecked(orderItem.isSelected() ? true : false);

        holder.check_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderItem.setSelected(!orderItem.isSelected());
                holder.check_delete.setChecked(orderItem.isSelected() ? true : false);

                if (orderItem.isSelected()) {
                    infoid.add(orderItem.getId());
                    Log.e("array", "------------" + infoid);
                } else {
                    infoid.remove(orderItem.getId());
                    Log.e("array", "------------" + infoid);
                }

            }
        });

       /* holder.check_delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                orderItem.setSelected(!orderItem.isSelected());
                holder.check_delete.setChecked(orderItem.isSelected() ? true : false);


                if (isChecked) {
                    //checked
                    infoid.add(orderItem.getId());
                    Log.e("array", "------------" + infoid);
                } else {
                    //not checked
                    infoid.remove(orderItem.getId());
                    Log.e("array", "------------" + infoid);
                }
            }
        });*/


    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    @SuppressWarnings("unused")
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, SwipeLayout.OnSwipeItemClickListener {

        final TextView txt_title;
        final TextView txt_description;
        final TextView txt_date;
        final TextView txt_location;
        final TextView txt_edit;
        final TextView txt_delete;
        final TextView txt_views;
        final SwipeLayout swipeLayout;
        final LinearLayout lin_delete;
        final CheckBox check_delete;
//        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_description = (TextView) itemView.findViewById(R.id.txt_description);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_location = (TextView) itemView.findViewById(R.id.txt_location);
            txt_views = (TextView) itemView.findViewById(R.id.txt_views);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            lin_delete = (LinearLayout) itemView.findViewById(R.id.lin_delete);
            check_delete = (CheckBox) itemView.findViewById(R.id.check_delete);
//            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ((SwipeLayout) itemView).setOnSwipeItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(v.getContext(), InfomationDetailActivity.class);
            intent.putExtra("click_action", "");
            intent.putExtra("info_id", itemArrayList.get(getAdapterPosition()).getId());
            intent.putExtra("title", itemArrayList.get(getAdapterPosition()).getTitle());
            intent.putExtra("description", itemArrayList.get(getAdapterPosition()).getDiscription());
            intent.putExtra("address", itemArrayList.get(getAdapterPosition()).getAddress());
            intent.putExtra("date", itemArrayList.get(getAdapterPosition()).getDate());
            intent.putExtra("view", itemArrayList.get(getAdapterPosition()).getView());
            v.getContext().startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();

            return false;
        }

        @Override
        public void onSwipeItemClick(boolean left, final int index) {
            if (index == 0) {
//                    Toast.makeText(itemView.getContext(), "Edit", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, EditInformationActivity.class);
                intent.putExtra("info_id", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("title", itemArrayList.get(getAdapterPosition()).getTitle());
                intent.putExtra("description", itemArrayList.get(getAdapterPosition()).getDiscription());
                intent.putExtra("address", itemArrayList.get(getAdapterPosition()).getAddress());
                intent.putExtra("date", itemArrayList.get(getAdapterPosition()).getDate());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else if (index == 1) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

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
                        new DeleteInformation().execute();
                        Log.e("pos", "----------" + index);
                        int pos = getAdapterPosition();
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
                        notifyDataSetChanged();
                    }
                });

                alertDialog.show();


//                    Toast.makeText(itemView.getContext(), "Trash" + id, Toast.LENGTH_SHORT).show();
            }

        }

    }

    private class DeleteInformation extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deleteInformation + "?infoid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
        }
    }

    public ArrayList<String> getDeleteData() {
        return infoid;
    }
}
