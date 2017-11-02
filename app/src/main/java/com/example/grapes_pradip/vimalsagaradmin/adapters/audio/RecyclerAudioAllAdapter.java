package com.example.grapes_pradip.vimalsagaradmin.adapters.audio;

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
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.audio.AudioDetailActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.audio.EditAudioActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.audio.AllAudioItem;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerAudioAllAdapter extends RecyclerView.Adapter<RecyclerAudioAllAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<AllAudioItem> itemArrayList;
    private String id;
    public static final ArrayList<String> allaudioid = new ArrayList<>();

    public RecyclerAudioAllAdapter(Activity activity, ArrayList<AllAudioItem> itemArrayList) {
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

        final AllAudioItem audioItem = itemArrayList.get(i);
        holder.txt_title.setText(audioItem.getAudioname());
        holder.txt_date.setText(audioItem.getDate());
        holder.txt_views.setText(audioItem.getView());
        Picasso.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.audioimage + audioItem.getPhoto().replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).resize(0,200).into(holder.img_audio_category);
        holder.check_delete.setChecked(audioItem.isSelected() ? true : false);

        ((SwipeLayout) holder.itemView).setItemState(SwipeLayout.ITEM_STATE_COLLAPSED, true);



        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditAudioActivity.class);
                intent.putExtra("aid", itemArrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("audioname", itemArrayList.get(holder.getAdapterPosition()).getAudioname());
                intent.putExtra("cid", itemArrayList.get(holder.getAdapterPosition()).getCategoryid());
                intent.putExtra("audio", itemArrayList.get(holder.getAdapterPosition()).getAudio());
                intent.putExtra("photo", itemArrayList.get(holder.getAdapterPosition()).getPhoto());
                intent.putExtra("duration", itemArrayList.get(holder.getAdapterPosition()).getDuration());
                intent.putExtra("date", itemArrayList.get(holder.getAdapterPosition()).getDate());
                intent.putExtra("categoryname", itemArrayList.get(holder.getAdapterPosition()).getCategoryname());
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
                        new DeleteAudio().execute();
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

        holder.check_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioItem.setSelected(!audioItem.isSelected());
                holder.check_delete.setChecked(audioItem.isSelected() ? true : false);

                if (audioItem.isSelected()) {
                    allaudioid.add(audioItem.getId());
                    Log.e("array", "------------" + allaudioid);
                } else {
                    allaudioid.remove(audioItem.getId());
                    Log.e("array", "------------" + allaudioid);
                }
            }
        });

        /*holder.check_delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked

                    allaudioid.add(audioItem.getId());
                    Log.e("array", "------------" + allaudioid);
                } else {
                    //not checked

                    allaudioid.remove(audioItem.getId());
                    Log.e("array", "------------" + allaudioid);
                }
            }
        });
*/
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
        final CheckBox check_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            img_audio_category = (ImageView) itemView.findViewById(R.id.img_audio_category);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            txt_views = (TextView) itemView.findViewById(R.id.txt_views);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            check_delete = (CheckBox) itemView.findViewById(R.id.checkbox_audio);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ((SwipeLayout) itemView).setOnSwipeItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), AudioDetailActivity.class);
            intent.putExtra("click_action", "");
            intent.putExtra("aid", itemArrayList.get(getAdapterPosition()).getId());
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
                Intent intent = new Intent(activity, EditAudioActivity.class);
                intent.putExtra("aid", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("audioname", itemArrayList.get(getAdapterPosition()).getAudioname());
                intent.putExtra("cid", itemArrayList.get(getAdapterPosition()).getCategoryid());
                intent.putExtra("audio", itemArrayList.get(getAdapterPosition()).getAudio());
                intent.putExtra("photo", itemArrayList.get(getAdapterPosition()).getPhoto());
                intent.putExtra("duration", itemArrayList.get(getAdapterPosition()).getDuration());
                intent.putExtra("date", itemArrayList.get(getAdapterPosition()).getDate());
                intent.putExtra("categoryname", itemArrayList.get(getAdapterPosition()).getCategoryname());
                intent.putExtra("view", itemArrayList.get(getAdapterPosition()).getView());
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
                        new DeleteAudio().execute();
                        Log.e("pos", "----------" + index);
                        int pos = getAdapterPosition();
                        itemArrayList.remove(pos);
                        notifyItemRemoved(pos);
//                        Toast.makeText(itemView.getContext(), "Trash" + id, Toast.LENGTH_SHORT).show();
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

//                    Toast.makeText(itemView.getContext(), "Trash" + id, Toast.LENGTH_SHORT).show();
            }

        }

    }

    private class DeleteAudio extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deleteaudio + "?aid=" + id);
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
                    Toast.makeText(activity, "Audio not delete." , Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
