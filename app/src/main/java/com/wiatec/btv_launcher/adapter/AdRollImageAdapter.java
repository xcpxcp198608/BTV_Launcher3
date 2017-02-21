package com.wiatec.btv_launcher.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.ImageInfo;

import java.util.List;

/**
 * Created by patrick on 2017/1/14.
 */

public class AdRollImageAdapter extends StaticPagerAdapter {

    private List<ImageInfo> list;

    @Override
    public View getView(final ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setBackgroundResource(R.drawable.item_ad1_bg);
        imageView.setPadding(3,3,3,3);
        imageView.setFocusable(true);
        imageView.setClickable(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setCropToPadding(true);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT));
        final ImageInfo imageInfo = list.get(position);
        Glide.with(container.getContext())
                .load(imageInfo.getUrl())
                .placeholder(R.drawable.bksound_icon_3)
                .dontAnimate()
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                container.getContext().startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(imageInfo.getLink())));
            }
        });
        imageView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Zoom.zoomIn10_11(v);
                }
            }
        });
        return imageView;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
