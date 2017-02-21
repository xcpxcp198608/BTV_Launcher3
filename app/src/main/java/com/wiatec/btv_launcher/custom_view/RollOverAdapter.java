package com.wiatec.btv_launcher.custom_view;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by patrick on 2017/2/20.
 */

public abstract class RollOverAdapter{
    public abstract int getCount();
    public abstract boolean isViewFromObject(View view, Object object);
    public abstract Object instantiateItem(ViewGroup container, int position);
    public abstract void destroyItem(ViewGroup container, int position, Object object);
}
