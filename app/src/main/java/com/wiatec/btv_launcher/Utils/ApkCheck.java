package com.wiatec.btv_launcher.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * Created by Administrator on 2016/8/19.
 */
public class ApkCheck {


    /**
     * 通过包名判断APK是否已安装
     * @param context
     * @param apkPackageName apk的完整包名
     * @return
     */
    public static boolean isApkInstalled (Context context , String apkPackageName){
        if(TextUtils.isEmpty(apkPackageName)){
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(apkPackageName , PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
           // Log.d("----px----" , e.getMessage());
            return false;
        }
    }

    //通过包名获得已经安装APK的icon
    public static Drawable getInstalledApkIcon (Context context , String packageName){
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName ,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(applicationInfo != null){
            return applicationInfo.loadIcon(packageManager);
        }else{
            return null;
        }
    }

    //通过包名获得已安装APK的Name
    public static String getInstalledApkName (Context context , String apkPackageName) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(apkPackageName ,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(applicationInfo != null){
            return applicationInfo.loadLabel(packageManager).toString();
        }else{
            return null;
        }
    }

    //通过包名获得已安装APK的version name
    public static String getInstalledApkVersionName (Context context , String apkPackageName) {
        if(TextUtils.isEmpty(apkPackageName)){
            return "apkPackageName error";
        }
        String apkVersionName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(apkPackageName , PackageManager.GET_ACTIVITIES);
            if(packageInfo != null) {
                apkVersionName = packageInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.d("----px----" , apkVersionName);
        return apkVersionName;
    }

    //通过包名获得已安装APK的version code
    public static int getInstalledApkVersionCode (Context context , String apkPackageName) {
        if(TextUtils.isEmpty(apkPackageName)){
            return 0;
        }
        int apkVersionCode = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(apkPackageName , PackageManager.GET_ACTIVITIES);
            if(packageInfo != null) {
                apkVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //Log.d("----px----" , apkVersionCode+"");
        return apkVersionCode;
    }

    //通过包名和版本编号判断更新apk是否需要
    public static boolean isApkNeedUpdate (Context context ,String apkPackageName ,int versionCode) {
        if(ApkCheck.isApkInstalled(context , apkPackageName)) {
            int localVersionCode = ApkCheck.getInstalledApkVersionCode(context,apkPackageName);
            if(versionCode > localVersionCode){
                return true;
            }else {
                return false;
            }
        }else {
            return true;
        }
    }

    //通过file path 和 file full name 判断文件是否存在
    public static boolean isFileExists(String filePath ,String fileFullName ){
        try {
            File file = new File(filePath+fileFullName);
            if(file.exists()){
                Logger.d("----file is exists");
                return true;
            }
            else {
                Logger.d("----file is not exists");
                return false;
            }
        }catch (Exception e){
            Logger.d("----file is not exists");
            return false;
        }
    }
    //通过file path 和 file full name 删除apk文件
    public static boolean deleteApkFile (String apkFilePath , String apkFileFullName ){
        File file;
        try {
            file = new File(apkFilePath+apkFileFullName);
            if(! file.exists()){
                return false;
            }
        }catch (Exception e){
            return false;
        }
        return file.delete();
    }

    //获得已存在的apk文件的包名
    public static String getApkFilePackageName (Context context , String apkFilePath ,String apkFileFullName){
        ApplicationInfo applicationInfo = null;
        PackageInfo packageInfo = null;
        PackageManager packageManager = context.getPackageManager();
        String apkPackageName = null;
        packageInfo = packageManager.getPackageArchiveInfo(apkFilePath+apkFileFullName ,PackageManager.GET_ACTIVITIES);
        if(packageInfo != null){
            applicationInfo = packageInfo.applicationInfo;
            apkPackageName = applicationInfo.packageName;
        }else{
            return null;
        }
        Log.d("----px----" ,apkPackageName);
        return apkPackageName;
    }

    //获得已经存在的apk文件的版本号
    public static String getApkFileVersionName (Context context , String apkFilePath ,String apkFileFullName){
        ApplicationInfo applicationInfo = null;
        PackageInfo packageInfo = null;
        PackageManager packageManager = context.getPackageManager();
        String apkVersionName = null;
        packageInfo = packageManager.getPackageArchiveInfo(apkFilePath+apkFileFullName ,PackageManager.GET_ACTIVITIES);
        if(packageInfo != null){
            apkVersionName = packageInfo.versionName;
        }else{
            return null;
        }
        Log.d("----px----" ,apkVersionName);
        return apkVersionName;
    }

    //判断已经存在的apk文件是否可安装
    public static boolean isApkCanInstalled (Context context , String apkFilePath ,String apkFileFullName){
        PackageInfo packageInfo = null;
        PackageManager packageManager = context.getPackageManager();
        packageInfo = packageManager.getPackageArchiveInfo(apkFilePath+apkFileFullName ,PackageManager.GET_ACTIVITIES);
        if(packageInfo != null){
            return true;
        }else{
            return false;
        }
    }

    //获得已经存在的APK文件的版本代号
    public static int getApkFileVersionCode (Context context , String apkFilePath ,String apkFileFullName){
        ApplicationInfo applicationInfo = null;
        PackageInfo packageInfo = null;
        PackageManager packageManager = context.getPackageManager();
        int apkVersionCode = 0;
        packageInfo = packageManager.getPackageArchiveInfo(apkFilePath+apkFileFullName ,PackageManager.GET_ACTIVITIES);
        if(packageInfo != null){
            apkVersionCode = packageInfo.versionCode;
        }else{
            return 0;
        }
        Log.d("----px----" ,apkVersionCode+"");
        return apkVersionCode;
    }

}
