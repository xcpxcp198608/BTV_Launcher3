package com.wiatec.btv_launcher.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.custom_view.RollOverAdapter;

import java.util.List;

/**
 * Created by patrick on 2017/2/25.
 */

public class RollOverViewAdapter extends RollOverAdapter {

    private List<ImageInfo> list;
    private ImageView imageView;
    private OnItemClickListener onItemClickListener;

    public RollOverViewAdapter(List<ImageInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        imageView = new ImageView(container.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setPadding(4,4,4,4);
        imageView.setBackgroundResource(R.drawable.roll_over_bg);
        imageView.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        Glide.with(container.getContext()).load(list.get(position).getUrl())
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .dontAnimate()
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position);
            }
        });
        imageView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Zoom.zoomIn09_10(v);
                }
            }
        });
        return imageView;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
