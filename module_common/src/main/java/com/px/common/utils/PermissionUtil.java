package com.px.common.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by patrick on 24/07/2017.
 * create time : 5:59 PM
 */

public class PermissionUtil {

    /**
     * 判断当前拥有需要的权限
     * @param permission 需要的权限
     * @return boolean
     */
    public static boolean hasPermission(String permission){
        return ContextCompat.checkSelfPermission(CommonApplication.context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity activity, String permission, String reson){
        if(!hasPermission(permission)){
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        }
    }
}
