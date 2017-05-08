package com.wiatec.btv_launcher.custom_view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.logging.Logger;

/**
 * Created by xuchengpeng on 05/05/2017.
 */

public class AutoScrollRecycleView extends RecyclerView {

    private final long INTERVAL = 30;
    private ScrollTask scrollTask;
    private boolean isRunning = false;

    public AutoScrollRecycleView(Context context) {
        this(context , null);
    }

    public AutoScrollRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs ,0);
    }

    public AutoScrollRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        scrollTask = new ScrollTask(this);
    }

    static class ScrollTask implements Runnable{

        private final WeakReference<AutoScrollRecycleView> weakReference;

        public ScrollTask(AutoScrollRecycleView autoScrollRecycleView) {
            weakReference = new WeakReference<>(autoScrollRecycleView);
        }

        @Override
        public void run() {
            AutoScrollRecycleView autoScrollRecycleView = weakReference.get();
            if(autoScrollRecycleView != null && autoScrollRecycleView.isRunning ){
                autoScrollRecycleView.scrollBy(3,0);
                autoScrollRecycleView.postDelayed(autoScrollRecycleView.scrollTask , autoScrollRecycleView.INTERVAL);
            }
        }
    }

    public void start(){
        if(isRunning){
            stop();
        }
        isRunning = true;
        postDelayed(scrollTask , INTERVAL);
    }

    public void stop(){
        isRunning = false;
        removeCallbacks(scrollTask);
    }
}
