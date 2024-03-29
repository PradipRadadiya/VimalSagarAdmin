package com.example.grapes_pradip.vimalsagaradmin.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.gallery.SlidingGalleryImage;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;

import java.util.ArrayList;

/**
 * Created by Grapes-Pradip on 04-Oct-17.
 */

public class PhotoAudioVideoAdapter extends RecyclerView.Adapter<PhotoAudioVideoAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<String> itemArrayList;
    private String id;
    Intent intent;

    public PhotoAudioVideoAdapter(Activity activity, ArrayList<String> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photoaudiovideoitem, viewGroup, false);
        return new ViewHolder(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Log.e("images path adapet", "---------------" + CommonURL.ImagePath + CommonAPI_Name.eventimage + itemArrayList.get(position));
//        Picasso.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.eventimage + itemArrayList.get(position).replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).into(holder.img_item);
        Glide.with(activity).load(CommonURL.ImagePath + CommonAPI_Name.eventimage + itemArrayList.get(position)
                .replaceAll(" ", "%20")).crossFade().placeholder(R.drawable.loading_bar).dontAnimate().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.img_item);


        holder.img_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, SlidingGalleryImage.class);
                intent.putExtra("position", String.valueOf(position));
                intent.putExtra("cid", "");
//                intent.putExtra("imagePath", CommonURL.ImagePath + CommonAPI_Name.eventimage + itemArrayList.get(position).replaceAll(" ", "%20"));

                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final ImageView img_item;
        final ImageView img_play;

        ViewHolder(View itemView) {
            super(itemView);

            img_item = itemView.findViewById(R.id.img_item);
            img_play = itemView.findViewById(R.id.img_play);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
//            Toast.makeText(activity, "long Click" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
