package com.wiatec.btv_launcher.custom_view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.Utils.Logger;

import java.lang.reflect.Field;

/**
 * Created by patrick on 2017/2/20.
 */

public class RollOverView extends ViewPager{

    private boolean isRoll = true;
    private long interval = 6000;
    private int mTransformerDuration = 1800;

    public RollOverView(Context context) {
        this(context ,null);
    }

    public RollOverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScrollDuration(mTransformerDuration);
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int position = (int) msg.obj;
            setCurrentItem(position);
        }
    };

    public void start(){
        final int count = getChildCount();
        isRoll = true;
        Application.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                int i =0;
                while(isRoll){
                    Message message = handler.obtainMessage();
                    message.obj = i;
                    handler.sendMessage(message);
//                    Logger.d(count+"");
//                    Logger.d(i+"");
                    i++;
                    if(i >= count){
                        i=0;
                    }
                    try {
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void pause(){
        isRoll = false;
    }

    public void setRollViewAdapter(final RollOverAdapter rollOverAdapter) {
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return rollOverAdapter.getCount();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return rollOverAdapter.isViewFromObject(view,object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                return rollOverAdapter.instantiateItem(container,position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                rollOverAdapter.destroyItem(container,position,object);
            }
        };
        setAdapter(pagerAdapter);
        setPageTransformer(true , new RollOverPageTransformer());
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
