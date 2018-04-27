package com.example.grapes_pradip.vimalsagaradmin.adapters.gallery;

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
import com.example.grapes_pradip.vimalsagaradmin.activities.gallery.AllGalleryActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.gallery.EditGalleryCategoryActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonMethod;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.gallery.GalleryCategoryItem;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerGalleryCategoryAdapter extends RecyclerView.Adapter<RecyclerGalleryCategoryAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<GalleryCategoryItem> itemArrayList;
    private String id;
    public static final ArrayList<String> galcatid = new ArrayList<>();

    public RecyclerGalleryCategoryAdapter(Activity activity, ArrayList<GalleryCategoryItem> itemArrayList) {
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

        final GalleryCategoryItem galleryCategoryItem = itemArrayList.get(i);
        holder.txt_title.setText(CommonMethod.decodeEmoji(galleryCategoryItem.getName()));
//        Picasso.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.gallerycategory + galleryCategoryItem.getCategoryIcon().replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).resize(0,200).into(holder.img_audio_category);

        Glide.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.gallerycategory + galleryCategoryItem.getCategoryIcon().replaceAll(" ", "%20")).crossFade().into(holder.img_audio_category);

        Log.e("image","------------------"+CommonURL.ImagePath + CommonAPI_Name.gallerycategory + galleryCategoryItem.getCategoryIcon().replaceAll(" ", "%20"));
        ((SwipeLayout) holder.itemView).setItemState(SwipeLayout.ITEM_STATE_COLLAPSED, true);
        holder.check_delete.setChecked(galleryCategoryItem.isSelected() ? true : false);
        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditGalleryCategoryActivity.class);
                intent.putExtra("cid", itemArrayList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("name", itemArrayList.get(holder.getAdapterPosition()).getName());
                intent.putExtra("photo", itemArrayList.get(holder.getAdapterPosition()).getCategoryIcon());
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
                        new DeleteGalleryCategory().execute();
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
                galleryCategoryItem.setSelected(!galleryCategoryItem.isSelected());
                holder.check_delete.setChecked(galleryCategoryItem.isSelected() ? true : false);

                if (galleryCategoryItem.isSelected()) {
                    galcatid.add(galleryCategoryItem.getId());
                    Log.e("array", "------------" + galcatid);
                } else {
                    galcatid.remove(galleryCategoryItem.getId());
                    Log.e("array", "------------" + galcatid);
                }
            }
        });

       /* holder.check_delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked

                    galcatid.add(galleryCategoryItem.getId());
                    Log.e("array", "------------" + galcatid);
                } else {
                    //not checked

                    galcatid.remove(galleryCategoryItem.getId());
                    Log.e("array", "------------" + galcatid);
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
            txt_edit = (TextView) itemView.findViewById(R.id.txt_edit);
            txt_delete = (TextView) itemView.findViewById(R.id.txt_delete);
            img_audio_category = (ImageView) itemView.findViewById(R.id.img_audio_category);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            check_delete = (CheckBox) itemView.findViewById(R.id.check_delete);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ((SwipeLayout) itemView).setOnSwipeItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), AllGalleryActivity.class);
            intent.putExtra("gallery_category_id", itemArrayList.get(getAdapterPosition()).getId());
            intent.putExtra("title", itemArrayList.get(getAdapterPosition()).getName());
            intent.putExtra("icon", itemArrayList.get(getAdapterPosition()).getCategoryIcon());
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
                Intent intent = new Intent(activity, EditGalleryCategoryActivity.class);
                intent.putExtra("cid", itemArrayList.get(getAdapterPosition()).getId());
                intent.putExtra("name", itemArrayList.get(getAdapterPosition()).getName());
                intent.putExtra("photo", itemArrayList.get(getAdapterPosition()).getCategoryIcon());
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
                        new DeleteGalleryCategory().execute();
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

    private class DeleteGalleryCategory extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deletecategorygallery + "?cid=" + id);
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
                    Toast.makeText(activity, "Gallery category not delete.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
