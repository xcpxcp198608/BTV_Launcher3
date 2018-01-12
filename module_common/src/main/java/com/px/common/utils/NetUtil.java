package com.px.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * network utils
 */

public class NetUtil {

    /**
     * 判断当前是否有网络连接
     * 需要权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
     * @return 是否连接
     */
    public static boolean isConnected () {
        ConnectivityManager connectivityManager = (ConnectivityManager) CommonApplication.context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isAvailable();
        }else{
            return false;
        }
    }

    /**
     * 判断当前网络连接方式
     * @return 连接类型 ， 0-没有连接 ，1-wifi连接 ，2-移动网络连接 ， 3-有线网络连接
     */
    public static int networkConnectType () {
        ConnectivityManager connectivityManager = (ConnectivityManager) CommonApplication.context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null){
            return 0;
        }
        NetworkInfo.State wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        //NetworkInfo.State mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State ethernet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).getState();
        if(wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING){
            return 1;//wifi网络连接
        }
        //else if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING){
        //    return 2;//移动网络连接
        //}
        else if (ethernet == NetworkInfo.State.CONNECTED || ethernet == NetworkInfo.State.CONNECTING){
            return 3;//有线网络连接
        }else {
            return 0;//没有网络连接
        }
    }

    /**
     * 获取当前系统wifi强度
     * 需要 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
     * @return 信号等级 0-没信号 ，1-信号很差 ，2-信号差 ， 3-信号好 ， 4-信号最好
     */
    public static int getWifiLevel(){
        WifiManager wifiManager = (WifiManager) CommonApplication.context
                .getSystemService(Context.WIFI_SERVICE);
        if(wifiManager == null) return 0;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int level = wifiInfo.getRssi();
        if(level <= 0 && level >= -50){
            return 4;//信号最好
        }else if (level < -50 && level >= -70) {
            return 3;//信号好
        }else if (level < -70 && level >= -80) {
            return 2;//信号差
        }else if (level < -80 && level >= -100) {
            return 1;//信号很差
        }else {
            return 0;//没信号
        }
    }

    /**
     * 获取当前网络数据量
     * @return 总的网络数据量，单位b
     */
    public static int getNetSpeedBytes() {
        String line;
        String[] segs;
        int received = 0;
        int i;
        int tmp = 0;
        boolean isNum;
        try {
            FileReader fr = new FileReader("/proc/net/dev");
            BufferedReader in = new BufferedReader(fr, 500);
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("rmnet") || line.startsWith("eth") || line.startsWith("wlan")) {
                    segs = line.split(":")[1].split(" ");
                    for (i = 0; i < segs.length; i++) {
                        isNum = true;
                        try {
                            tmp = Integer.parseInt(segs[i]);
                        } catch (Exception e) {
                            isNum = false;
                        }
                        if (isNum) {
                            received = received + tmp;
                            break;
                        }
                    }
                }
            }
            in.close();
        } catch (IOException e) {
            Logger.e(e.getMessage());
            return -1;
        }
        return received;
    }

}
