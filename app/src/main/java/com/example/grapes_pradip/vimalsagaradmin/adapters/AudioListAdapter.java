package com.example.grapes_pradip.vimalsagaradmin.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.activities.AudioPlayActivity;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonAPI_Name;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.grapes_pradip.vimalsagaradmin.model.PhotoAudioVideoItem;

import java.util.ArrayList;

/**
 * Created by Grapes-Pradip on 04-Oct-17.
 */

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.ViewHolder> {


    private final Activity activity;
    private final ArrayList<PhotoAudioVideoItem> itemArrayList;
    private String id;
    Intent intent;

    public AudioListAdapter(Activity activity, ArrayList<PhotoAudioVideoItem> itemArrayList) {
        super();
        this.activity = activity;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.audioitem, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Log.e("audio", "---------------" + itemArrayList.get(position));
        final PhotoAudioVideoItem photoAudioVideoItem = itemArrayList.get(position);
//        Picasso.with(activity).load(CommonURL.AudioPath + CommonAPI_Name.eventaudio + photoAudioVideoItem.getUrl().replaceAll(" ", "%20")).error(R.drawable.noimageavailable).placeholder(R.drawable.loading_bar).into(holder.img_item);
        holder.txt_audioname.setText(photoAudioVideoItem.getName());
        holder.img_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AudioPlayActivity.class);
                Log.e("audiopath","----------------"+CommonURL.AudioPath + CommonAPI_Name.eventaudio + photoAudioVideoItem.getUrl().replaceAll(" ", "%20"));
                intent.putExtra("audiopath", CommonURL.AudioPath + CommonAPI_Name.eventaudio + photoAudioVideoItem.getUrl().replaceAll(" ", "%20"));
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        /*if (photoAudioVideoItem.getSpecification().equalsIgnoreCase("photo")) {
            holder.img_play.setVisibility(View.GONE);
            Log.e("url","-----------"+photoAudioVideoItem.getUrl());
//            intent=new Intent(activity, ImageViewActivity.class);
//            intent.putExtra("imagePath",photoAudioVideoItem.getUrl());
//            activity.startActivity(intent);
        } else if (photoAudioVideoItem.getSpecification().equalsIgnoreCase("audio")) {


        } else if (photoAudioVideoItem.getSpecification().equalsIgnoreCase("video")) {
//            intent=new Intent(activity, VideoFullActivity.class);
//            intent.putExtra("videopath",photoAudioVideoItem.getUrl());
//            activity.startActivity(intent);

        }*/


    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView img_item, img_play;
        TextView txt_audioname;

        public ViewHolder(View itemView) {
            super(itemView);

            img_item = (ImageView) itemView.findViewById(R.id.img_item);
            img_play = (ImageView) itemView.findViewById(R.id.img_play);
            txt_audioname = (TextView) itemView.findViewById(R.id.txt_audioname);
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