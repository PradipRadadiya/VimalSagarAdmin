package com.example.grapes_pradip.vimalsagaradmin.adapters.event;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.grapes_pradip.vimalsagaradmin.R;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;


class ImageSlideEventAdapter extends android.support.v4.view.PagerAdapter {
    private final Context mcontext;
    private final LayoutInflater layoutInflater;
    private final ArrayList<String> imageItemArrayList;

    public ImageSlideEventAdapter(Context context, ArrayList<String> imageItemArrayList) {
        mcontext = context;
        this.imageItemArrayList = imageItemArrayList;
        layoutInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageItemArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.splash_pager_item, container, false);
        Log.e("imahes","------------------"+imageItemArrayList.get(position));

        final SubsamplingScaleImageView imageView = itemView.findViewById(R.id.imageView);
        final ProgressWheel progress_wheel = itemView.findViewById(R.id.progress_wheel);
        if (imageItemArrayList.size() == 0) {
//            imageView.setImageResource(R.drawable.no_image);
            imageView.setImage(ImageSource.resource(R.drawable.noimageavailable));
        }
        Log.e("image url", "------------------" + imageItemArrayList.get(position));
        if (!imageItemArrayList.get(position).equalsIgnoreCase("")) {
            try {
                Glide.with(mcontext).load(imageItemArrayList.get(position))
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                                imageView.setImageBitmap(resource);
                                imageView.setImage(ImageSource.bitmap(resource));
                                progress_wheel.setVisibility(View.GONE);

                            }
                        });
            } catch (Exception e) {
                Log.e("error", e.toString());
            }
        } else {
//            imageView.setImageResource(R.drawable.no_image);
            imageView.setImage(ImageSource.resource(R.drawable.noimageavailable));
            progress_wheel.setVisibility(View.GONE);
        }
        imageView.setTag(imageItemArrayList.get(position));
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
