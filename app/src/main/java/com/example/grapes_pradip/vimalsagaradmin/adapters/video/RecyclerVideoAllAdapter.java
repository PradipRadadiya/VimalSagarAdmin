package com.example.grapes_pradip.vimalsagaradmin.adapters.video;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandrius.accordionswipelayout.library.SwipeLayout;
import com.bumptech.glide.Glide;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.video.EditVideoActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.video.VideoDetailActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.video.AllVideoItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerVideoAllAdapter extends RecyclerView.Adapter<RecyclerVideoAllAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<AllVideoItem> itemArrayList;
    private String id;
    public static final ArrayList<String> videoid = new ArrayList<>();

    public RecyclerVideoAllAdapter(Activity activity, ArrayList<AllVideoItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_audio_all, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final AllVideoItem videoItem = itemArrayList.get(i);
        holder.txt_title.setText(CommonMethod.decodeEmoji(videoItem.getAudioname()));
        holder.txt_date.setText(CommonMethod.decodeEmoji(videoItem.getDate()));
        holder.txt_views.setText(CommonMethod.decodeEmoji(videoItem.getView()));
//        Picasso.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.videoimage + videoItem.getPhoto().replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).resize(0,200).into(holder.img_audio_category);

        Glide.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.videoimage + videoItem.getPhoto()
                .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loading_bar).dontAnimate().into(holder.img_audio_category);


        holder.checkbox_audio.setChecked(videoItem.isSelected() ? true : false);

        ((SwipeLayout) holder.itemView).setItemState(SwipeLayout.ITEM_STATE_COLLAPSED, true);


        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditVideoActivity.class);
                intent.putExtra("vid", itemArrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("videoname", itemArrayList.get(holder.getAdapterPosition()).getAudioname());
                intent.putExtra("cid", itemArrayList.get(holder.getAdapterPosition()).getCategoryid());
                intent.putExtra("video", itemArrayList.get(holder.getAdapterPosition()).getAudio());
                intent.putExtra("photo", itemArrayList.get(holder.getAdapterPosition()).getPhoto());
                intent.putExtra("duration", itemArrayList.get(holder.getAdapterPosition()).getDuration());
                intent.putExtra("date", itemArrayList.get(holder.getAdapterPosition()).getDate());
                intent.putExtra("categoryname", itemArrayList.get(holder.getAdapterPosition()).getCategoryname());
                intent.putExtra("description", itemArrayList.get(holder.getAdapterPosition()).getDescription());
                intent.putExtra("videolink", itemArrayList.get(holder.getAdapterPosition()).getVideolink());
                intent.putExtra("isodate", itemArrayList.get(holder.getAdapterPosition()).getIsodate());
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
                        new DeleteVideo().execute();
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

        holder.checkbox_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoItem.setSelected(!videoItem.isSelected());
                holder.checkbox_audio.setChecked(videoItem.isSelected() ? true : false);

                if (videoItem.isSelected()) {
                    videoid.add(videoItem.getId());
                    Log.e("array", "------------" + videoid);
                } else {
                    videoid.remove(videoItem.getId());
                    Log.e("array", "------------" + videoid);
                }
            }
        });

       /* holder.checkbox_audio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked

                    videoid.add(videoItem.getId());
                    Log.e("array", "------------" + videoid);
                } else {
                    //not checked

                    videoid.remove(videoItem.getId());
                    Log.e("array", "------------" + videoid);
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

        final ImageView img_audio_category;
        final TextView txt_title;
        final TextView txt_date;
        final TextView txt_edit;
        final TextView txt_delete;
        final TextView txt_views;
        final SwipeLayout swipeLayout;
        final CheckBox checkbox_audio;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_views = (TextView) itemView.findViewById(R.id.txt_views);
            img_audio_category = (ImageView) itemView.findViewById(R.id.img_audio_category);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            checkbox_audio = (CheckBox) itemView.findViewById(R.id.checkbox_audio);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ((SwipeLayout) itemView).setOnSwipeItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), VideoDetailActivity.class);
            intent.putExtra("click_action", "");
            intent.putExtra("vid", itemArrayList.get(getAdapterPosition()).getId());
            intent.putExtra("videoname", itemArrayList.get(getAdapterPosition()).getAudioname());
            intent.putExtra("cid", itemArrayList.get(getAdapterPosition()).getCategoryid());
            intent.putExtra("video", itemArrayList.get(getAdapterPosition()).getAudio());
            intent.putExtra("photo", itemArrayList.get(getAdapterPosition()).getPhoto());
            intent.putExtra("duration", itemArrayList.get(getAdapterPosition()).getDuration());
            intent.putExtra("date", itemArrayList.get(getAdapterPosition()).getDate());
            intent.putExtra("categoryname", itemArrayList.get(getAdapterPosition()).getCategoryname());
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
                Intent intent = new Intent(activity, EditVideoActivity.class);
                intent.putExtra("vid", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("videoname", itemArrayList.get(getAdapterPosition()).getAudioname());
                intent.putExtra("cid", itemArrayList.get(getAdapterPosition()).getCategoryid());
                intent.putExtra("video", itemArrayList.get(getAdapterPosition()).getAudio());
                intent.putExtra("photo", itemArrayList.get(getAdapterPosition()).getPhoto());
                intent.putExtra("duration", itemArrayList.get(getAdapterPosition()).getDuration());
                intent.putExtra("date", itemArrayList.get(getAdapterPosition()).getDate());
                intent.putExtra("categoryname", itemArrayList.get(getAdapterPosition()).getCategoryname());
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
                        new DeleteVideo().execute();
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

    private class DeleteVideo extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deletevideo + "?vid=" + id);
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
                    Toast.makeText(activity, "Video not delete.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
