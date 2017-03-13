package com.wiatec.btv_launcher.custom_view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;


import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by patrick on 2017/2/20.
 */

public class RollOverView extends ViewPager{

    private boolean isRoll = true;
    private long interval = 6000;
    private int mTransformerDuration = 1800;
    private PagerAdapter mPagerAdapter;
    private Timer mTimer;

    public RollOverView(Context context) {
        this(context ,null);
    }

    public RollOverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScrollDuration(mTransformerDuration);
    }

    private final static class RollHandler extends Handler{
        private WeakReference<RollOverView> weakReference;

        public RollHandler (RollOverView rollOverView){
            weakReference = new WeakReference<>(rollOverView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RollOverView rollOverView = weakReference.get();
            int currentItem = rollOverView.getCurrentItem()+1;
            if(currentItem >= rollOverView.mPagerAdapter.getCount()) {
                currentItem = 0;
            }
            rollOverView.setCurrentItem(currentItem);
            if(rollOverView.mPagerAdapter.getCount() <= 1){
                rollOverView.stop();
            }
        }
    }
    private RollHandler rollHandler = new RollHandler(this);

    private static class RollTimeTask extends TimerTask{
        private WeakReference<RollOverView> weakReference;
        public RollTimeTask (RollOverView rollOverView){
            weakReference = new WeakReference<>(rollOverView);
        }

        @Override
        public void run() {
            RollOverView rollOverView = weakReference.get();
            if(rollOverView !=null && rollOverView.isShown()){
                rollOverView.rollHandler.sendEmptyMessage(0);
            }else{
                cancel();
            }
        }
    }

    public void start(){
        if(interval <= 0 || mPagerAdapter == null || mPagerAdapter.getCount()<=1 ){
            return;
        }
        if(mTimer != null){
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new RollTimeTask(this) ,interval,interval);
    }

    public void stop() {
        if (mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    public void setRollViewAdapter(PagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
        setAdapter(mPagerAdapter);
        setPageTransformer(true , new RollOverPageTransformer());
        start();
    }

    public class RollOverPageTransformer implements PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            if(position <=0 ){//左边的view
                //设置旋转中心点；
                page.setPivotX(page.getMeasuredWidth());
                page.setPivotY(page.getMeasuredHeight()*0.5f);
                page.setRotationY(90f*position);//只在Y轴做旋转操作
            }else if (position <=1){ //右边的view
                page.setPivotX(0);
                page.setPivotY(page.getMeasuredHeight() * 0.5f);
                page.setRotationY(90f*position);
            }
        }
    }

    public void setScrollDuration(int duration){
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller mScroller = new FixedSpeedScroller(getContext());
            mScroller.setmDuration(duration);
            field.set(this,mScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public class FixedSpeedScroller extends Scroller {
        private int mDuration = 1800;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }

    }
}
