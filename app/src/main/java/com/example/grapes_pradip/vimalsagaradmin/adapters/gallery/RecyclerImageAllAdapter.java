package com.example.grapes_pradip.vimalsagaradmin.adapters.gallery;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.gallery.SlidingGalleryImage;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.model.gallery.AllImageItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Pradip on 1/2/17.
 */
@SuppressWarnings("ALL")
public class RecyclerImageAllAdapter extends RecyclerView.Adapter<RecyclerImageAllAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<AllImageItem> itemArrayList;
    String id;
    public static final ArrayList<String> imgid = new ArrayList<>();

    public RecyclerImageAllAdapter(Activity activity, ArrayList<AllImageItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_all, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        final AllImageItem imageItem = itemArrayList.get(i);
//        Picasso.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.Gallery + imageItem.getPhoto().replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).resize(0,200).into(holder.img_photos);

        Glide.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.Gallery + imageItem.getPhoto()
                .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loading_bar).into(holder.img_photos);

        holder.check_image.setChecked(imageItem.isSelected() ? true : false);
        holder.img_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SlidingGalleryImage.class);
                intent.putExtra("imagePath", CommonURL.ImagePath + CommonAPI_Name.Gallery + imageItem.getPhoto());
                String position=String.valueOf(i);
                intent.putExtra("position", position);
                activity.startActivity(intent);
            }
        });

        holder.check_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageItem.setSelected(!imageItem.isSelected());
                holder.check_image.setChecked(imageItem.isSelected() ? true : false);

                if (imageItem.isSelected()) {
                    imgid.add(imageItem.getID());
                    Log.e("array", "------------" + imgid);
                } else {
                    imgid.remove(imageItem.getID());
                    Log.e("array", "------------" + imgid);
                }

            }
        });

        /*holder.check_image.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked

                    imgid.add(imageItem.getID());
                    Log.e("array", "------------" + imgid);
                } else {
                    //not checked

                    imgid.remove(imageItem.getID());
                    Log.e("array", "------------" + imgid);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {

        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final ImageView img_photos;
        final CheckBox check_image;


        public ViewHolder(View itemView) {
            super(itemView);

            img_photos = (ImageView) itemView.findViewById(R.id.img_photos);
            check_image = (CheckBox) itemView.findViewById(R.id.check_image);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
           /* Toast.makeText(activity, "on Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(v.getContext(), AudioDetailActivity.class);
            intent.putExtra("aid", itemArrayList.get(getAdapterPosition()).getId());
            intent.putExtra("categoryname", itemArrayList.get(getAdapterPosition()).getCategoryname());
            v.getContext().startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }


    }


}
