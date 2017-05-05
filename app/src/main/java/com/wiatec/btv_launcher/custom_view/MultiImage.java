package com.wiatec.btv_launcher.custom_view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by patrick on 2017/3/13.
 */

public class MultiImage extends AppCompatImageButton {

    private List<String> images;
    private int mInterval = 6000;
    private int mCurrentPosition = -1 ;
    private Timer timer;

    public MultiImage(Context context) {
        this(context , null);
    }

    public MultiImage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public MultiImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
        if(images ==null || images.size()<=0){
            return;
        }
        start();
    }

    public void start (){
        if(timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new MultiTask(this) , 0 , mInterval);
    }

    public void stop (){
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    private final static class TimerHandler extends Handler {
        private WeakReference<MultiImage> weakReference;

        public TimerHandler(MultiImage multiImage) {
            weakReference = new WeakReference<>(multiImage);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MultiImage multiImage = weakReference.get();
            multiImage.mCurrentPosition++;

            if( multiImage.mCurrentPosition >= multiImage.images.size()){
                multiImage.mCurrentPosition = 0;
            }
            Glide.with(multiImage.getContext())
                    .load(multiImage.images.get(multiImage.mCurrentPosition))
                    .dontAnimate()
                    .into(multiImage);
        }
    }

    private TimerHandler timerHandler = new TimerHandler(this);
    private final static class MultiTask extends TimerTask{

        private WeakReference<MultiImage> weakReference;

        public MultiTask(MultiImage multiImageView) {
            weakReference = new WeakReference<>(multiImageView);
        }
        @Override
        public void run() {
            MultiImage multiImageView = weakReference.get();
            multiImageView.timerHandler.sendEmptyMessage(0);
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(int mCurrentPosition) {
        this.mCurrentPosition = mCurrentPosition;
    }
}
