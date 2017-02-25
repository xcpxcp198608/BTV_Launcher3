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
import com.wiatec.btv_launcher.custom_view.RollOverView;

import java.util.List;

/**
 * Created by patrick on 2017/2/25.
 */

public class RollOverViewAdapter extends RollOverAdapter {

    private List<ImageInfo> list;
    private ImageButton imageButton;
    private OnItemClickListener onItemClickListener;

    public RollOverViewAdapter(List<ImageInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        imageButton = new ImageButton(container.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        imageButton.setLayoutParams(layoutParams);
        imageButton.setPadding(3,3,3,3);
        imageButton.setBackgroundResource(R.drawable.roll_over_bg);
        imageButton.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        Glide.with(container.getContext()).load(list.get(position).getUrl())
                .error(R.drawable.bksound_icon_3)
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
        container.addView(imageButton);
        return imageButton;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageButton);
    }

    public interface OnItemClickListener{
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
