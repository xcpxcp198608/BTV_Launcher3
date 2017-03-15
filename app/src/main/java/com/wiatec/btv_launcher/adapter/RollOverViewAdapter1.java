package com.wiatec.btv_launcher.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class RollOverViewAdapter1 extends RollOverAdapter {

    private List<ImageInfo> list;
    private ImageButton imageButton;
    private OnItemClickListener onItemClickListener;

    public RollOverViewAdapter1(List<ImageInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        imageButton = new ImageButton(container.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        imageButton.setLayoutParams(layoutParams);
        imageButton.setPadding(3,3,3,3);
        imageButton.setBackgroundResource(R.drawable.roll_over_bg);
        imageButton.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        Glide.with(container.getContext()).load(list.get(position).getUrl())
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .dontAnimate()
                .into(imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position);
            }
        });
        imageButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Zoom.zoomIn09_10(v);
                }
            }
        });
        return imageButton;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
