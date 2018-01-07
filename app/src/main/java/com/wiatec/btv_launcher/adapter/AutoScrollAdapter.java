package com.wiatec.btv_launcher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.px.common.image.ImageMaster;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.custom_view.AutoScrollViewHolder;

import java.util.List;

/**
 * Created by xuchengpeng on 05/05/2017.
 * auto scroll view adapter
 */

public class AutoScrollAdapter extends RecyclerView.Adapter<AutoScrollViewHolder> {

    private List<ImageInfo> list;
    private Context context;

    public AutoScrollAdapter(Context context ,List<ImageInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public AutoScrollViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auto_scroll_recycle_view , parent ,false);
        return new AutoScrollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AutoScrollViewHolder holder, int position) {
        ImageInfo imageInfo = list.get(position);
        ImageMaster.load(imageInfo.getUrl(), holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
