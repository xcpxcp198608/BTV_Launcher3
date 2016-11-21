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
import com.wiatec.btv_launcher.bean.RollImageInfo;

import java.util.List;

/**
 * Created by PX on 2016-11-21.
 */

public class RollImage2Adapter extends StaticPagerAdapter {

    private List<RollImageInfo> list;

    public RollImage2Adapter(List<RollImageInfo> list) {
        this.list = list;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setBackgroundResource(R.drawable.white_side);
        imageView.setFocusable(false);
        imageView.setClickable(false);
        imageView.setPadding(0,0,0,0);
        Glide.with(container.getContext()).load(list.get(position).getImageUrl()).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setCropToPadding(true);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT ,ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
