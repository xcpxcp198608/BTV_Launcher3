package com.wiatec.btv_launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.OnLanguageChangeListener;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.SystemConfig;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by PX on 2016-11-28.
 */

public class LanguageChangeReceiver extends BroadcastReceiver {

    private OnLanguageChangeListener onLanguageChangeListener;

    public LanguageChangeReceiver() {
    }

    public void setOnLanguageChangeListener (OnLanguageChangeListener onLanguageChangeListener){
        this.onLanguageChangeListener = onLanguageChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_CONFIGURATION_CHANGED.equals(intent.getAction())){
            String language = SystemConfig.getLanguage(Application.getContext());
            SharedPreferences sharedPreferences = Application.getContext().getSharedPreferences("language" ,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("language" , language);
            editor.commit();
            if(onLanguageChangeListener != null) {
                onLanguageChangeListener.onChange(language);
            }
        }
    }
}
