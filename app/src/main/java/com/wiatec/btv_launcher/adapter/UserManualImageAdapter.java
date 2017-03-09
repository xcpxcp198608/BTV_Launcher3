package com.wiatec.btv_launcher.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.bean.ImageInfo;

import java.util.List;

/**
 * Created by patrick on 2017/3/4.
 */

public class UserManualImageAdapter extends StaticPagerAdapter {

    private List<ImageInfo> list;

    public UserManualImageAdapter(List<ImageInfo> list) {
        this.list = list;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setFocusable(true);
        imageView.setClickable(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        final ImageInfo imageInfo = list.get(position);
        if(imageInfo == null){
            return imageView;
        }
        Glide.with(container.getContext()).load(imageInfo.getUrl())
                .placeholder(R.drawable.loading)
                .dontAnimate()
                .into(imageView);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT ,ViewGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
