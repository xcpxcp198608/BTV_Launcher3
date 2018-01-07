package com.px.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Locale;

/**
 * system utils
 */

public class SysUtil {

    /**
     * 判断系统是否已Root
     * @return 是否已root
     */
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
        //Log.d("----px----","system have root");
        return true;
    }

    /**
     * 获得当前屏幕宽度 api >=17
     * @param activity 当前activity
     * @return 当前屏幕宽度
     */
    public static int getScreenWidth(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
//        display.getRealSize(point);
        return point.x;
    }

    /**
     * 获得当前屏幕高度 api >=17
     * @param activity 当前activity
     * @return 当前屏幕高度
     */
    public static int getScreenHeight(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        //display.getRealSize(point);
        return point.y;
    }

    /**
     * 获得当前设备的wifi mac地址
     * @return 当前设备wifi的mac地址
     */
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

    /**
     * 获取系统ethernet的mac地址
     * @return ethernet mac
     */
    public static String getEthernetMac(){
        try {
            return loadFileAsString("/sys/class/net/eth0/address")
                    .toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 读取文件内容
     * @param filePath 文件路径
     * @return 文件内容
     * @throws IOException 调用者处理
     */
    private  static String loadFileAsString(String filePath) throws IOException{
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    /**
     * 获得当前系统语言
     * @return 当前系统设置的语言+国家地区类型
     */
    public static String getLanguage () {
        Locale locale = CommonApplication.context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String country = locale.getCountry();
        //Log.d("----px----" ,language+"_"+country);
        return language+"_"+country;
    }

    /**
     * 获取设备android_id
     * @return device idRegularUtil.java
     */
    public static String getAndroidId() {
        return Settings.Secure.getString(CommonApplication.context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * 用系统浏览器打开网址
     */
    public static void openUrl (Context context, String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent (Intent.ACTION_VIEW ,uri);
        context.startActivity(intent);
    }

    /**
     * 判断当前软键盘是否打开
     * @param activity 上下文
     * @return result
     */
    public static boolean isSoftInputShow(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            return inputManger.isActive() && activity.getWindow().getCurrentFocus() != null;
        }
        return false;
    }

    /**
     * 打开软键盘
     * @param editText  输入框
     * @param context 上下文
     */
    public static void openKeybord(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     * @param editText 输入框
     * @param context 上下文
     */
    public static void closeKeybord(EditText editText, Context context) {
        if(isSoftInputShow((Activity) context)) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
}
