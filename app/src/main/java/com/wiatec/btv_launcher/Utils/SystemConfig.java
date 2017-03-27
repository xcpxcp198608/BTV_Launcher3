package com.wiatec.btv_launcher.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/8/19.
 */
public class SystemConfig {

    //判断系统是否已Root
    public static boolean isRoot () {
        Process process = null;
        DataOutputStream dataOutputStream = null;
        try {
            process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataOutputStream.writeBytes("check"+"\n");
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("----px----","system no root");
            return false;
        }finally {
            try {
                if(dataOutputStream != null){
                    dataOutputStream.close();
                }
                if(process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("----px----","system have root");
        return true;
    }

    //判断当前是否有网络连接
    public static boolean isNetworkConnected (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo !=null && networkInfo.isAvailable()){
            return true;
        }else {
            return false;
        }
    }

    //判断当前网络连接方式
    public static int networkConnectType (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
//        NetworkInfo.State mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State ethernet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).getState();
        if(wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING){
            return 1;//wifi网络连接
        }
        //else if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING){
        //    return 2;//移动网络连接
        //}
        else if (ethernet == NetworkInfo.State.CONNECTED || ethernet == NetworkInfo.State.CONNECTING){
            return 3;//有线网络连接
        }
        else {
            return 0;//没有网络连接
        }
    }
    //获取当前系统时间
    public static String getTime () {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(date);
    }
    //获取当前系统日期
    public static String getDate () {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
    //获取当前系统wifi强度
    public static int getWifiLevel(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
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

    //toast long
    public static void toastLong (Context context ,String message){
        Toast.makeText(context ,message ,Toast.LENGTH_LONG).show();
    }

    //toast short
    public static void toastShort (Context context ,String message){
        Toast.makeText(context ,message ,Toast.LENGTH_SHORT).show();
    }

    //用系统浏览器打开网址
    public static void openBrowserByUrl (Context context ,String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent (Intent.ACTION_VIEW ,uri);
        context.startActivity(intent);
    }

    public static int getScreenWidth(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        //display.getRealSize(point);
        int width = point.x;
        return width;
    }

    public static int getScreenHeight(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        //display.getRealSize(point);
        int width = point.y;
        return width;
    }

    public static String getWifiMac1(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getMacAddress();
    }

    public static String getWifiMac() {
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            String line;
            while ((line = input.readLine()) != null) {
                macSerial += line.trim();
            }

            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return macSerial;
    }
    //获得当前系统语言
    public static String getLanguage (Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String country = locale.getCountry();
        //Log.d("----px----" ,language+"_"+country);
        return language+"_"+country;
    }
}
