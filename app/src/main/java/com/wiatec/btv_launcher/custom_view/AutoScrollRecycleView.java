package com.wiatec.btv_launcher.custom_view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xuchengpeng on 05/05/2017.
 */

public class AutoScrollRecycleView extends RecyclerView {

    private final long INTERVAL = 30;
    private ScrollTask mScrollTask;
    private boolean isRunning = false;
    private Timer timer;

    public AutoScrollRecycleView(Context context) {
        this(context , null);
    }

    public AutoScrollRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs ,0);
    }

    public AutoScrollRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private final static class TimerHandler extends Handler {

        private WeakReference<AutoScrollRecycleView> weakReference;
        private AutoScrollRecycleView autoScrollRecycleView;

        public TimerHandler (AutoScrollRecycleView autoScrollRecycleView){
            weakReference = new WeakReference<>(autoScrollRecycleView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(autoScrollRecycleView == null) {
                autoScrollRecycleView = weakReference.get();
            }
            if(autoScrollRecycleView != null && autoScrollRecycleView.isRunning ){
                autoScrollRecycleView.scrollBy(2,0);
            }
        }
    }

    private TimerHandler timerHandler = new TimerHandler(this);

    private static class ScrollTask extends TimerTask{

        private final WeakReference<AutoScrollRecycleView> weakReference;
        private AutoScrollRecycleView autoScrollRecycleView;

        public ScrollTask(AutoScrollRecycleView autoScrollRecycleView) {
            weakReference = new WeakReference<>(autoScrollRecycleView);
        }

        @Override
        public void run() {
            if (autoScrollRecycleView == null){
                autoScrollRecycleView = weakReference.get();
            }
            autoScrollRecycleView.timerHandler.sendEmptyMessage(0);
        }
    }

    public void start(){
        if(isRunning){
            stop();
            return;
        }
        isRunning = true;
        if(timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new ScrollTask(this) , 0 , INTERVAL);
    }

    public void stop(){
        isRunning = false;
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        if(timerHandler != null){
            timerHandler.removeCallbacks(mScrollTask);
        }
    }

    public boolean isScrollToBottom(){
        if(computeHorizontalScrollExtent() + computeHorizontalScrollOffset() >= computeHorizontalScrollRange()){
            return true;
        }else{
            return false;
        }
    }
}
