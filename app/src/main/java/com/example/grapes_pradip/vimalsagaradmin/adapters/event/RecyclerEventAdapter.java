package com.example.grapes_pradip.vimalsagaradmin.adapters.event;

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
import android.widget.Toast;

import com.alexandrius.accordionswipelayout.library.SwipeLayout;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.event.EditEventActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.event.EventDetailActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.event.AllIEventItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class RecyclerEventAdapter extends RecyclerView.Adapter<RecyclerEventAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<AllIEventItem> itemArrayList;
    private String id;
    public static final ArrayList<String> eventid = new ArrayList<>();

    public RecyclerEventAdapter(Activity activity, ArrayList<AllIEventItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final AllIEventItem eventItem = itemArrayList.get(i);
        holder.txt_title.setText(CommonMethod.decodeEmoji(eventItem.getTitle()));
        holder.txt_description.setText(CommonMethod.decodeEmoji(eventItem.getDescription()));
        holder.txt_date.setText(CommonMethod.decodeEmoji(eventItem.getDate()));
        holder.txt_location.setText(CommonMethod.decodeEmoji(eventItem.getAddress()));
        holder.txt_views.setText(CommonMethod.decodeEmoji(eventItem.getView()));
        ((SwipeLayout) holder.itemView).setItemState(SwipeLayout.ITEM_STATE_COLLAPSED, true);

        holder.delete_data.setChecked(eventItem.isSelected() ? true : false);
        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditEventActivity.class);

                intent.putExtra("event_id", itemArrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("title", itemArrayList.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("description", itemArrayList.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("address", itemArrayList.get(holder.getAdapterPosition()).getAddress());
                intent.putExtra("date", itemArrayList.get(holder.getAdapterPosition()).getDate());
                intent.putExtra("audio", itemArrayList.get(holder.getAdapterPosition()).getAudio());
                intent.putExtra("audio_img", itemArrayList.get(holder.getAdapterPosition()).getAudioimage());
                intent.putExtra("videoLink", itemArrayList.get(holder.getAdapterPosition()).getVideolink());
                intent.putExtra("video", itemArrayList.get(holder.getAdapterPosition()).getVideo());
                intent.putExtra("video_img", itemArrayList.get(holder.getAdapterPosition()).getVideoimage());
                intent.putExtra("photo", itemArrayList.get(holder.getAdapterPosition()).getPhoto());
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
                        new DeleteEvent().execute();
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
        holder.delete_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventItem.setSelected(!eventItem.isSelected());
                holder.delete_data.setChecked(eventItem.isSelected() ? true : false);

                if (eventItem.isSelected()) {
                    eventid.add(eventItem.getId());
                    Log.e("array", "------------" + eventid);
                } else {
                    eventid.remove(eventItem.getId());
                    Log.e("array", "------------" + eventid);
                }

            }
        });
       /* holder.delete_data.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    //checked

                    eventid.add(eventItem.getId());
                    Log.e("array", "------------" + eventid);
                } else {
                    //not checked

                    eventid.remove(eventItem.getId());
                    Log.e("array", "------------" + eventid);
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
        SwipeLayout swipeLayout;
        final CheckBox delete_data;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_description = (TextView) itemView.findViewById(R.id.txt_description);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_location = (TextView) itemView.findViewById(R.id.txt_location);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            txt_views = (TextView) itemView.findViewById(R.id.txt_views);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            delete_data = (CheckBox) itemView.findViewById(R.id.delete_data);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ((SwipeLayout) itemView).setOnSwipeItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), EventDetailActivity.class);
            intent.putExtra("click_action", "");
            intent.putExtra("event_id", itemArrayList.get(getAdapterPosition()).getId());
            intent.putExtra("title", itemArrayList.get(getAdapterPosition()).getTitle());
            intent.putExtra("description", itemArrayList.get(getAdapterPosition()).getDescription());
            intent.putExtra("address", itemArrayList.get(getAdapterPosition()).getAddress());
            intent.putExtra("date", itemArrayList.get(getAdapterPosition()).getDate());
            intent.putExtra("audio", itemArrayList.get(getAdapterPosition()).getAudio());
            intent.putExtra("audio_img", itemArrayList.get(getAdapterPosition()).getAudioimage());
            intent.putExtra("videoLink", itemArrayList.get(getAdapterPosition()).getVideolink());
            intent.putExtra("video", itemArrayList.get(getAdapterPosition()).getVideo());
            intent.putExtra("video_img", itemArrayList.get(getAdapterPosition()).getVideoimage());
            intent.putExtra("photo", itemArrayList.get(getAdapterPosition()).getPhoto());
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
                Intent intent = new Intent(activity, EditEventActivity.class);
                intent.putExtra("event_id", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("title", itemArrayList.get(getAdapterPosition()).getTitle());
                intent.putExtra("description", itemArrayList.get(getAdapterPosition()).getDescription());
                intent.putExtra("address", itemArrayList.get(getAdapterPosition()).getAddress());
                intent.putExtra("date", itemArrayList.get(getAdapterPosition()).getDate());
                intent.putExtra("audio", itemArrayList.get(getAdapterPosition()).getAudio());
                intent.putExtra("audio_img", itemArrayList.get(getAdapterPosition()).getAudioimage());
                intent.putExtra("videoLink", itemArrayList.get(getAdapterPosition()).getVideolink());
                intent.putExtra("video", itemArrayList.get(getAdapterPosition()).getVideo());
                intent.putExtra("video_img", itemArrayList.get(getAdapterPosition()).getVideoimage());
                intent.putExtra("photo", itemArrayList.get(getAdapterPosition()).getPhoto());
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
                        new DeleteEvent().execute();
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

    private class DeleteEvent extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.delete_event + "?eid=" + id);
            return responseJSON;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("response", "--------------" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                } else {
                    Toast.makeText(activity, "Event not delete.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
