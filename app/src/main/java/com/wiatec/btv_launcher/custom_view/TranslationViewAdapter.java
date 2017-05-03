package com.wiatec.btv_launcher.custom_view;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuchengpeng on 29/04/2017.
 */

public abstract class TranslationViewAdapter extends PagerAdapter {

    private List<View> list = new ArrayList<>();

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = findViewByPosition(container ,position);
        container.addView(itemView);
        return itemView;
    }

    private View findViewByPosition(ViewGroup container,int position){
        for (View view : list) {
            if (((int)view.getTag()) == position && view.getParent() == null){
                return view;
            }
        }
        View view = getView(container,position);
        view.setTag(position);
        list.add(view);
        return view;
    }

    public abstract View getView(ViewGroup container, int position);
}
