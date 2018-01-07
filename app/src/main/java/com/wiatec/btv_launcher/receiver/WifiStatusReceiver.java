package com.wiatec.btv_launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.px.common.utils.NetUtil;

/**
 * Created by PX on 2016-11-12.
 */

public class WifiStatusReceiver extends BroadcastReceiver {

    private OnWifiStatusListener onWifiStatusListener;

    public void setOnWifiStatusListener (OnWifiStatusListener onWifiStatusListener){
        this.onWifiStatusListener = onWifiStatusListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.RSSI_CHANGED_ACTION.equals(intent.getAction())){
            if(onWifiStatusListener != null) {
                onWifiStatusListener.onWifiLevelChange(NetUtil.getWifiLevel());
            }
        }
    }
}
