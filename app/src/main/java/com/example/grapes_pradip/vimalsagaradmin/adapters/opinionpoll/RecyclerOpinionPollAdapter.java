package com.example.grapes_pradip.vimalsagaradmin.adapters.opinionpoll;

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
import android.widget.TextView;

import com.alexandrius.accordionswipelayout.library.SwipeLayout;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.opinionpoll.OpinionPollDetailActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.opinionpoll.OpinionPollEditActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.opinionpoll.OpinionItem;

import java.util.ArrayList;


@SuppressWarnings("ALL")
public class RecyclerOpinionPollAdapter extends RecyclerView.Adapter<RecyclerOpinionPollAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<OpinionItem> itemArrayList;
    private String id;
    public static final ArrayList<String> pollid = new ArrayList<>();

    public RecyclerOpinionPollAdapter(Activity activity, ArrayList<OpinionItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_opinionpoll, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final OpinionItem opinionItem = itemArrayList.get(i);
        holder.txt_title.setText(opinionItem.getQues());
        ((SwipeLayout) holder.itemView).setItemState(SwipeLayout.ITEM_STATE_COLLAPSED, true);
        holder.checkbox_delete.setChecked(opinionItem.isSelected() ? true : false);

        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, OpinionPollEditActivity.class);
                intent.putExtra("ID", itemArrayList.get(holder.getAdapterPosition()).getID());
                intent.putExtra("Ques", itemArrayList.get(holder.getAdapterPosition()).getQues());
                intent.putExtra("total_polls", itemArrayList.get(holder.getAdapterPosition()).getTotalPolls());
                intent.putExtra("yes_polls", itemArrayList.get(holder.getAdapterPosition()).getYesPolls());
                intent.putExtra("no_polls", itemArrayList.get(holder.getAdapterPosition()).getNoPolls());
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
                        id = itemArrayList.get(holder.getAdapterPosition()).getID();
                        Log.e("id", "-----------------------" + id);
                        new DeletePolls().execute();
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

        holder.checkbox_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opinionItem.setSelected(!opinionItem.isSelected());
                holder.checkbox_delete.setChecked(opinionItem.isSelected() ? true : false);

                if (opinionItem.isSelected()) {
                    pollid.add(opinionItem.getID());
                    Log.e("array", "------------" + pollid);
                } else {
                    pollid.remove(opinionItem.getID());
                    Log.e("array", "------------" + pollid);
                }
            }
        });

        /*holder.checkbox_delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    pollid.add(opinionItem.getID());
                    Log.e("array", "------------" + pollid);
                } else {
                    //not checked
                    pollid.remove(opinionItem.getID());
                    Log.e("array", "------------" + pollid);
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
        final TextView txt_edit;
        final TextView txt_delete;
        final SwipeLayout swipeLayout;
        final CheckBox checkbox_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            checkbox_delete = (CheckBox) itemView.findViewById(R.id.checkbox_delete);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ((SwipeLayout) itemView).setOnSwipeItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), OpinionPollDetailActivity.class);
            intent.putExtra("ID", itemArrayList.get(getAdapterPosition()).getID());
            intent.putExtra("Ques", itemArrayList.get(getAdapterPosition()).getQues());
            intent.putExtra("total_polls", itemArrayList.get(getAdapterPosition()).getTotalPolls());
            intent.putExtra("yes_polls", itemArrayList.get(getAdapterPosition()).getYesPolls());
            intent.putExtra("no_polls", itemArrayList.get(getAdapterPosition()).getNoPolls());
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
                Intent intent = new Intent(activity, OpinionPollEditActivity.class);
                intent.putExtra("ID", itemArrayList.get(getAdapterPosition()).getID());
                intent.putExtra("Ques", itemArrayList.get(getAdapterPosition()).getQues());
                intent.putExtra("total_polls", itemArrayList.get(getAdapterPosition()).getTotalPolls());
                intent.putExtra("yes_polls", itemArrayList.get(getAdapterPosition()).getYesPolls());
                intent.putExtra("no_polls", itemArrayList.get(getAdapterPosition()).getNoPolls());
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
                        id = itemArrayList.get(getAdapterPosition()).getID();
                        Log.e("id", "-----------------------" + id);
                        new DeletePolls().execute();
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

    private class DeletePolls extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deletepoll + "?qid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
        }
    }
}
