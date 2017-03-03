package com.wiatec.btv_launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.wiatec.btv_launcher.Activity.WeekUpAdActivity;
import com.wiatec.btv_launcher.Utils.Logger;

/**
 * Created by patrick on 2017/3/3.
 */

public class ScreenWeekUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass(context , WeekUpAdActivity.class);
            context.startActivity(intent);
        }
    }
}
