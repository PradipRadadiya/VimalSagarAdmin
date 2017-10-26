package com.example.grapes_pradip.vimalsagaradmin.adapters.thought;

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
import com.example.grapes_pradip.vimalsagaradmin.activities.thought.EditThoughtActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.thought.ThoughtDetailActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.thought.AllThoughtItem;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerThoughtAdapter extends RecyclerView.Adapter<RecyclerThoughtAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<AllThoughtItem> itemArrayList;
    private String id;
    public static final ArrayList<String> thoughtid = new ArrayList<>();

    public RecyclerThoughtAdapter(Activity activity, ArrayList<AllThoughtItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_thought, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final AllThoughtItem thoughtItem = itemArrayList.get(i);
        holder.txt_title.setText(thoughtItem.getTitle());
        holder.txt_des.setText(thoughtItem.getDescription());
        holder.txt_date.setText(thoughtItem.getDate());
        holder.txt_views.setText(thoughtItem.getView());
        ((SwipeLayout) holder.itemView).setItemState(SwipeLayout.ITEM_STATE_COLLAPSED, true);
        holder.checkbox_thought.setChecked(thoughtItem.isSelected() ? true : false);
        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditThoughtActivity.class);
                intent.putExtra("tid", itemArrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("title", itemArrayList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("description", itemArrayList.get(holder.getAdapterPosition()).getDescription());
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
                        new DeleteThought().execute();
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
        holder.checkbox_thought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thoughtItem.setSelected(!thoughtItem.isSelected());
                holder.checkbox_thought.setChecked(thoughtItem.isSelected() ? true : false);

                if (thoughtItem.isSelected()) {
                    thoughtid.add(thoughtItem.getId());
                    Log.e("array", "------------" + thoughtid);
                } else {
                    thoughtid.remove(thoughtItem.getId());
                    Log.e("array", "------------" + thoughtid);
                }
            }
        });

        /*holder.checkbox_thought.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked

                    thoughtid.add(thoughtItem.getId());
                    Log.e("array", "------------" + thoughtid);
                } else {
                    //not checked

                    thoughtid.remove(thoughtItem.getId());
                    Log.e("array", "------------" + thoughtid);
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
        final TextView txt_views;
        final TextView txt_date;
        final TextView txt_des;
        final TextView txt_edit;
        final TextView txt_delete;
        final SwipeLayout swipeLayout;
        final CheckBox checkbox_thought;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_des = (TextView) itemView.findViewById(R.id.txt_des);
            txt_views = (TextView) itemView.findViewById(R.id.txt_views);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            checkbox_thought = (CheckBox) itemView.findViewById(R.id.checkbox_thought);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ((SwipeLayout) itemView).setOnSwipeItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), ThoughtDetailActivity.class);
            intent.putExtra("click_action", "");
            intent.putExtra("tid", itemArrayList.get(getAdapterPosition()).getId());
            intent.putExtra("title", itemArrayList.get(getAdapterPosition()).getTitle());
            intent.putExtra("descrption", itemArrayList.get(getAdapterPosition()).getDescription());
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
                Intent intent = new Intent(activity, EditThoughtActivity.class);
                intent.putExtra("tid", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("title", itemArrayList.get(getAdapterPosition()).getTitle());
                intent.putExtra("description", itemArrayList.get(getAdapterPosition()).getDescription());
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
                        new DeleteThought().execute();
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

    private class DeleteThought extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deletethought + "?tid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
        }
    }
}
