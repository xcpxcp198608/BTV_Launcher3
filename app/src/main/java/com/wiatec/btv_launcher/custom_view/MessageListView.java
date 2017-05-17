package com.wiatec.btv_launcher.custom_view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ListView;
import com.wiatec.btv_launcher.Utils.Logger;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xuchengpeng on 24/04/2017.
 */

public class MessageListView extends ListView {

    private Timer timer;
    private int interval = 15000;
    private int currentPosition = -1;
    private ScrollTask mScrollTask;
    private OnScrollFinishedListener onScrollFinishedListener;

    public MessageListView(Context context) {
        this(context , null);
    }

    public MessageListView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public MessageListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setItemsCanFocus(false);
    }

    private TimerHandler timerHandler = new TimerHandler(this);

    private final static class TimerHandler extends Handler{

        private WeakReference<MessageListView> weakReference;

        public TimerHandler (MessageListView messageListView){
            weakReference = new WeakReference<>(messageListView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MessageListView messageListView = weakReference.get();
            messageListView.currentPosition = messageListView.currentPosition + 1 ;
            int totalSize = messageListView.getCount();
            if(messageListView.currentPosition >= totalSize){
                messageListView.stop();
                if(messageListView.onScrollFinishedListener != null){
                    messageListView.onScrollFinishedListener.onFinished(true , totalSize);
                }
                return;
            }
            messageListView.setSelection(messageListView.currentPosition);
        }
    }

    public void setOnScrollFinishedListener (OnScrollFinishedListener onScrollFinishedListener){
        this.onScrollFinishedListener = onScrollFinishedListener;
    }

    public interface OnScrollFinishedListener{
        void onFinished(boolean isFinished ,int position);
    }

    private static final class ScrollTask extends TimerTask {

        private WeakReference<MessageListView> weakReference ;

        public ScrollTask (MessageListView messageListView){
            weakReference = new WeakReference<>(messageListView);
        }

        @Override
        public void run() {
            MessageListView messageListView = weakReference.get();
            if(messageListView != null){
                messageListView.timerHandler.sendEmptyMessage(0);
            }else{
                cancel();
            }
        }
    }

    public void start(){
        if(timer != null){
            timer.cancel();
        }
        currentPosition = 0;
        timer = new Timer();
        mScrollTask = new ScrollTask(this);
        timer.schedule(mScrollTask , interval , interval);
    }

    public void stop (){
        currentPosition = 0;
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        if(timerHandler != null){
            timerHandler.removeCallbacks(mScrollTask);
        }
    }

}
