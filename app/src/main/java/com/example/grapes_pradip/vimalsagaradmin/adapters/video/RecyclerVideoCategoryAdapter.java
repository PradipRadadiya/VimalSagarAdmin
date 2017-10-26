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
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.video.AllVideoActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.video.EditVideoCategoryActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.common.JsonParser;
import com.example.grapes_pradip.vimalsagaradmin.model.video.AllVideoCategoryItem;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerVideoCategoryAdapter extends RecyclerView.Adapter<RecyclerVideoCategoryAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<AllVideoCategoryItem> itemArrayList;
    private String id;
    public static final ArrayList<String> videiocatid = new ArrayList<>();

    public RecyclerVideoCategoryAdapter(Activity activity, ArrayList<AllVideoCategoryItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_category, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {

        final AllVideoCategoryItem videoCategoryItem = itemArrayList.get(i);
        holder.txt_title.setText(videoCategoryItem.getName());
        Picasso.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.videocategory + videoCategoryItem.getCategoryicon().replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).into(holder.img_video_category);
        ((SwipeLayout) holder.itemView).setItemState(SwipeLayout.ITEM_STATE_COLLAPSED, true);
        holder.checkbox_delete.setChecked(videoCategoryItem.isSelected() ? true : false);
        holder.txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditVideoCategoryActivity.class);
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
                        new DeleteVideoCategory().execute();
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
                videoCategoryItem.setSelected(!videoCategoryItem.isSelected());
                holder.checkbox_delete.setChecked(videoCategoryItem.isSelected() ? true : false);

                if (videoCategoryItem.isSelected()) {
                    videiocatid.add(videoCategoryItem.getId());
                    Log.e("array", "------------" + videiocatid);
                } else {
                    videiocatid.remove(videoCategoryItem.getId());
                    Log.e("array", "------------" + videiocatid);
                }
            }
        });


        /*holder.checkbox_delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked

                    videiocatid.add(videoCategoryItem.getId());
                    Log.e("array", "------------" + videiocatid);
                } else {
                    //not checked

                    videiocatid.remove(videoCategoryItem.getId());
                    Log.e("array", "------------" + videiocatid);
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

        final ImageView img_video_category;
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
            img_video_category = (ImageView) itemView.findViewById(R.id.img_video_category);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            checkbox_delete = (CheckBox) itemView.findViewById(R.id.checkbox_delete);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            ((SwipeLayout) itemView).setOnSwipeItemClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), AllVideoActivity.class);
            intent.putExtra("video_category_id", itemArrayList.get(getAdapterPosition()).getId());
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
                Intent intent = new Intent(activity, EditVideoCategoryActivity.class);
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
                        new DeleteVideoCategory().execute();
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

    private class DeleteVideoCategory extends AsyncTask<String, Void, String> {
        String responseJSON = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            responseJSON = JsonParser.getStringResponse(CommonURL.Main_url + CommonAPI_Name.deleteVideocategory + "?cid=" + id);
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
                    Toast.makeText(activity, "Video category not delete.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
