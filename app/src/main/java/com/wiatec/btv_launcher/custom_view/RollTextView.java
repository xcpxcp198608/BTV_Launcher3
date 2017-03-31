package com.wiatec.btv_launcher.custom_view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wiatec.btv_launcher.bean.Message1Info;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by patrick on 2017/3/31.
 */

public class RollTextView extends AppCompatTextView {

    private long interval = 6000;
    private Timer mTimer;
    private List<Message1Info> list;
    private int currentItem = -1;

    public RollTextView(Context context) {
        this(context , null);
    }

    public RollTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public RollTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(List<Message1Info> list){
        this.list = list;
        start();
    }

    private RollHandler rollHandler = new RollHandler(this);
    private final static class RollHandler extends Handler{
        private WeakReference<RollTextView> weakReference;

        public RollHandler(RollTextView rollTextView) {
            weakReference = new WeakReference<>(rollTextView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RollTextView rollTextView = weakReference.get();
            int current = rollTextView.currentItem ++;
            if(current >= rollTextView.list.size()){
                current = 0;
            }
            if(rollTextView.list != null && rollTextView.list.size() >0){
                Message1Info message1Info = rollTextView.list.get(current);
                rollTextView.setText(message1Info.getContent());
                rollTextView.setTextColor(Color.rgb(message1Info.getColorR(),message1Info.getColorR() , message1Info.getColorB()));
            }
        }
    }

    private static class RollTask extends TimerTask {
        private WeakReference<RollTextView> weakReference;
        public RollTask (RollTextView rollTextView){
            weakReference = new WeakReference<>(rollTextView);
        }

        @Override
        public void run() {
            RollTextView rollTextView = weakReference.get();
            if(rollTextView !=null && rollTextView.isShown()){
                rollTextView.rollHandler.sendEmptyMessage(0);
            }else{
                cancel();
            }
        }
    }

    public void start(){
        if(interval <=0 && list == null && list.size() <=0){
            return;
        }
        if(mTimer != null){
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new RollTask(this) ,interval,interval);
    }

    public void stop() {
        if (mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }
}
