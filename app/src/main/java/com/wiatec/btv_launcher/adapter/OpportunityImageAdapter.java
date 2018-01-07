package com.wiatec.btv_launcher.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.px.common.image.ImageMaster;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.bean.ImageInfo;

import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public class OpportunityImageAdapter extends StaticPagerAdapter {
    private List<ImageInfo> list ;

    public OpportunityImageAdapter(List<ImageInfo> list) {
        this.list = list;
    }

    @Override
    public View getView(final ViewGroup container, final int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setFocusable(true);
        imageView.setClickable(true);
        imageView.setPadding(3,3,3,3);
        final ImageInfo imageInfo = list.get(position);
        if(imageInfo == null){
            return imageView;
        }
        ImageMaster.load(imageInfo.getUrl(), imageView, R.drawable.loading);
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
