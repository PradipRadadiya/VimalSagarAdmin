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
import com.bumptech.glide.Glide;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.audio.AllAudioActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.audio.EditAudioCategoryActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.audio.AllAudioCategoryItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerAudioCategoryAdapter extends RecyclerView.Adapter<RecyclerAudioCategoryAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<AllAudioCategoryItem> itemArrayList;
    private String id;
    public static final ArrayList<String> audio_cat = new ArrayList<>();

    public RecyclerAudioCategoryAdapter(Activity activity, ArrayList<AllAudioCategoryItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_audio_category, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final AllAudioCategoryItem audioCategoryItem = itemArrayList.get(i);
        holder.txt_title.setText(CommonMethod.decodeEmoji(audioCategoryItem.getName()));
        String path = CommonURL.ImagePath + CommonAPI_Name.audiocategory + audioCategoryItem.getCategoryicon().replaceAll(" ", "%20");
        if (audioCategoryItem.getCategoryicon().equalsIgnoreCase("")) {
//            Picasso.with(activity).load(R.drawable.noimageavailable);
        } else {
//            Picasso.with(activity).load(path).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).resize(0,200).into(holder.img_audio_category);

            Glide.with(activity).load(path
                    .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loading_bar).dontAnimate().into(holder.img_audio_category);

        }

        ((SwipeLayout) holder.itemView).setItemState(SwipeLayout.ITEM_STATE_COLLAPSED, true);

        holder.check_delete.setChecked(audioCategoryItem.isSelected() ? true : false);

        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditAudioCategoryActivity.class);
                intent.putExtra("cid", itemArrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("name", itemArrayList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("photo", itemArrayList.get(holder.getAdapterPosition()).getCategoryicon());
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
                        new DeleteAudioCategory().execute();
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


        holder.check_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioCategoryItem.setSelected(!audioCategoryItem.isSelected());
                holder.check_delete.setChecked(audioCategoryItem.isSelected() ? true : false);

                if (audioCategoryItem.isSelected()) {
                    audio_cat.add(audioCategoryItem.getId());
                    Log.e("array", "------------" + audio_cat);
                } else {
                    audio_cat.remove(audioCategoryItem.getId());
                    Log.e("array", "------------" + audio_cat);
                }
            }
        });

        /*holder.check_delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    //checked

                    audio_cat.add(audioCategoryItem.getId());
                    Log.e("array", "------------" + audio_cat);
                } else {
                    //not checked

                    audio_cat.remove(audioCategoryItem.getId());
                    Log.e("array", "------------" + audio_cat);
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
        final TextView txt_edit;
        final TextView txt_delete;
        final SwipeLayout swipeLayout;
        final CheckBox check_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            img_audio_category = (ImageView) itemView.findViewById(R.id.img_audio_category);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            check_delete = (CheckBox) itemView.findViewById(R.id.check_delete);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ((SwipeLayout) itemView).setOnSwipeItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), AllAudioActivity.class);
            intent.putExtra("audio_category_id", itemArrayList.get(getAdapterPosition()).getId());
            intent.putExtra("title", itemArrayList.get(getAdapterPosition()).getName());
            intent.putExtra("icon", itemArrayList.get(getAdapterPosition()).getCategoryicon());
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
                Intent intent = new Intent(activity, EditAudioCategoryActivity.class);
                intent.putExtra("cid", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("name", itemArrayList.get(getAdapterPosition()).getName());
                intent.putExtra("photo", itemArrayList.get(getAdapterPosition()).getCategoryicon());
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
                        new DeleteAudioCategory().execute();
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

    private class DeleteAudioCategory extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deletecategory + "?cid=" + id);
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
                    Toast.makeText(activity, "Audio category not delete.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
