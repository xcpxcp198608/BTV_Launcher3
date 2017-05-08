package com.wiatec.btv_launcher.custom_view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.wiatec.btv_launcher.R;

/**
 * Created by xuchengpeng on 05/05/2017.
 */

public class AutoScrollViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;

    public AutoScrollViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.iv_auto_scroll);
    }
}
