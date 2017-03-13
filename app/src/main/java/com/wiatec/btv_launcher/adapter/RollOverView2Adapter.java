package com.wiatec.btv_launcher.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.custom_view.RollOverAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 2017/3/10.
 */

public class RollOverView2Adapter extends RollOverAdapter {

    private List<Integer> list = new ArrayList<>();
    private ImageView imageView;

    public RollOverView2Adapter() {
        list.add(R.drawable.ld_shuiyin);
        list.add(R.drawable.shuiyin);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(ViewGroup container, int position) {
        imageView = new ImageView(container.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(list.get(position));
        return imageView;
    }

}
