package com.wiatec.btv_launcher.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.custom_view.TranslationViewAdapter;

import java.util.List;

/**
 * Created by xuchengpeng on 03/05/2017.
 */

public class TranslationAdapter extends TranslationViewAdapter {

    private List<ImageInfo> list;

    public TranslationAdapter(List<ImageInfo> list) {
        this.list = list;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setCropToPadding(true);
        imageView.setBackgroundResource(R.color.colorBlue5);
        int index = position % list.size();
        if (!TextUtils.isEmpty(list.get(index).getUrl())) {
            Glide.with(container.getContext())
                    .load(list.get(index).getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .dontAnimate()
                    .into(imageView);
        }
        return imageView;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
