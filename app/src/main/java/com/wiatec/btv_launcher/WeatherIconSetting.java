package com.wiatec.btv_launcher;

import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by PX on 2016-11-18.
 */

public class WeatherIconSetting {
    public static void setIcon (ImageButton imageButton , String icon){
        if("01d".equals(icon)){
            imageButton.setImageResource(R.drawable.w01d_1);
        }else if ("01n".equals(icon)){
            imageButton.setImageResource(R.drawable.w01n_1);
        }else if ("02d".equals(icon)){
            imageButton.setImageResource(R.drawable.w02d_1);
        }else if ("02n".equals(icon)){
            imageButton.setImageResource(R.drawable.w02n_1);
        }else if ("03d".equals(icon)){
            imageButton.setImageResource(R.drawable.w03d_1);
        }else if ("03n".equals(icon)){
            imageButton.setImageResource(R.drawable.w03n_1);
        }else if ("04d".equals(icon)){
            imageButton.setImageResource(R.drawable.w04d_1);
        }else if ("04n".equals(icon)){
            imageButton.setImageResource(R.drawable.w04n_1);
        }else if ("09d".equals(icon)){
            imageButton.setImageResource(R.drawable.w09d_1);
        }else if ("09n".equals(icon)){
            imageButton.setImageResource(R.drawable.w09n_1);
        }else if ("10d".equals(icon)){
            imageButton.setImageResource(R.drawable.w10d_1);
        }else if ("10n".equals(icon)){
            imageButton.setImageResource(R.drawable.w10n_1);
        }else if ("11d".equals(icon)){
            imageButton.setImageResource(R.drawable.w11d_1);
        }else if ("11n".equals(icon)){
            imageButton.setImageResource(R.drawable.w11n_1);
        }else if ("13d".equals(icon)){
            imageButton.setImageResource(R.drawable.w13d_1);
        }else if ("13n".equals(icon)){
            imageButton.setImageResource(R.drawable.w13n_1);
        }else if ("50d".equals(icon)){
            imageButton.setImageResource(R.drawable.w50d_1);
        }else if ("50n".equals(icon)){
            imageButton.setImageResource(R.drawable.w50n_1);
        }
    }
}
