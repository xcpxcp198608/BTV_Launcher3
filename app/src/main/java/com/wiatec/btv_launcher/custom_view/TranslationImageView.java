package com.wiatec.btv_launcher.custom_view;

import android.content.Context;
import android.mtp.MtpConstants;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xuchengpeng on 29/04/2017.
 */

public class TranslationImageView extends ViewPager {

    private long interval = 18000;
    private int mTransformerDuration = 20000;
    private PagerAdapter mPagerAdapter;
    private Timer mTimer;

    public TranslationImageView(Context context) {
        this(context , null);
    }

    public TranslationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScrollDuration(mTransformerDuration);
    }

    private final static class TranslationHandler extends Handler {
        private WeakReference<TranslationImageView> weakReference;

        public TranslationHandler (TranslationImageView translationImageView){
            weakReference = new WeakReference<>(translationImageView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TranslationImageView translationImageView = weakReference.get();
            int currentItem = translationImageView.getCurrentItem()+1;
            if(currentItem >= translationImageView.mPagerAdapter.getCount()) {
                currentItem = 0;
            }
            translationImageView.setCurrentItem(currentItem);
            if(translationImageView.mPagerAdapter.getCount() <= 1){
                translationImageView.stop();
            }
        }
    }
    private TranslationHandler translationHandler = new TranslationHandler(this);

    private static class TranslationTimeTask extends TimerTask {

        private WeakReference<TranslationImageView> weakReference;

        public TranslationTimeTask (TranslationImageView translationImageView){
            weakReference = new WeakReference<>(translationImageView);
        }

        @Override
        public void run() {
            TranslationImageView translationImageView = weakReference.get();
            if(translationImageView !=null && translationImageView.isShown()){
                translationImageView.translationHandler.sendEmptyMessage(0);
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
        mTimer.schedule(new TranslationTimeTask(this) , interval ,interval);
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
        setPageTransformer(true , new TranslationPageTransformer());
        start();
    }

    public class TranslationPageTransformer implements PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            if(position <=0 ){//左边的view
                page.setTranslationX(0);
            }else if (position <=1){ //右边的view
                page.setTranslationX(position);
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
        private int mDuration = 20000;

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
