package com.wiatec.btv_launcher.custom_view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.wiatec.btv_launcher.Utils.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by xuchengpeng on 05/05/2017.
 */

public class AutoScrollRecycleView extends RecyclerView {

    private final long INTERVAL = 6000;
    private ScrollTask scrollTask;
    private boolean isRunning = false;
    private int currentPosition = 0;

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

    private static class ScrollTask implements Runnable{

        private final WeakReference<AutoScrollRecycleView> weakReference;

        private ScrollTask(AutoScrollRecycleView autoScrollRecycleView) {
            weakReference = new WeakReference<>(autoScrollRecycleView);
        }

        @Override
        public void run() {
            AutoScrollRecycleView autoScrollRecycleView = weakReference.get();
            if(autoScrollRecycleView != null && !autoScrollRecycleView.isRunning ){
                autoScrollRecycleView.isRunning = true;
                autoScrollRecycleView.currentPosition += 1;
                Logger.d(autoScrollRecycleView.currentPosition+"");
                autoScrollRecycleView.smoothScrollToPosition(autoScrollRecycleView.currentPosition);
                autoScrollRecycleView.postDelayed(autoScrollRecycleView.scrollTask , autoScrollRecycleView.INTERVAL);
            }
        }
    }

    public void start(){
        if(isRunning){
            stop();
        }
        postDelayed(scrollTask , INTERVAL);
    }

    public void stop(){
        isRunning = false;
        removeCallbacks(scrollTask);
    }
}
