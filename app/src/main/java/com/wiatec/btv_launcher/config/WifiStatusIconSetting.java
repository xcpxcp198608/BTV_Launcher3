package com.wiatec.btv_launcher.config;

import android.widget.ImageView;

import com.wiatec.btv_launcher.R;

/**
 * Created by patrick on 2017/3/2.
 */

public class WifiStatusIconSetting {

    public static void setIcon (ImageView imageView , int level){
        switch (level) {
            case 4:
                imageView.setImageResource(R.drawable.wifi4);
                break;
            case 3:
                imageView.setImageResource(R.drawable.wifi3);
                break;
            case 2:
                imageView.setImageResource(R.drawable.wifi2);
                break;
            case 1:
                imageView.setImageResource(R.drawable.wifi1);
                break;
            case 0:
                imageView.setImageResource(R.drawable.wifi0);
                break;
        }
    }
}
