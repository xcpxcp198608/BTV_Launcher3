package com.wiatec.btv_launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.wiatec.btv_launcher.OnNetworkStatusListener;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.SystemConfig;

/**
 * Created by PX on 2016-11-12.
 */

public class NetworkStatusReceiver extends BroadcastReceiver {

    private ImageView imageView;
    private OnNetworkStatusListener onNetworkStatusListener;

    public NetworkStatusReceiver(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setOnNetworkStatusListener (OnNetworkStatusListener onNetworkStatusListener){
        this.onNetworkStatusListener = onNetworkStatusListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(SystemConfig.isNetworkConnected(context)){
            showNetworkStatus(context);
            if(onNetworkStatusListener !=null){
                onNetworkStatusListener.onConnected(true);
            }
        }else {
            onNetworkStatusListener.onDisconnect(true);
        }
    }

    private void showNetworkStatus(Context context){
        if(imageView ==null){
            return;
        }
        int i = SystemConfig.networkConnectType(context);
        switch (i) {
            case 0:
                imageView.setImageResource(R.drawable.disconnect);
                break;
            case 1:
                imageView.setImageResource(R.drawable.wifi4);
                break;
            case 3:
                imageView.setImageResource(R.drawable.ethernet);
                break;
        }
    }
}
