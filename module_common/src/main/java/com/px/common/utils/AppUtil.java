package com.px.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * app utils
 */

public class AppUtil {

    /**
     * 通过包名判断APK是否已安装
     * @param packageName apk的完整包名
     * @return 是否已安装
     */
    public static boolean isInstalled (String packageName){
        if(TextUtils.isEmpty(packageName)){
            return false;
        }
        try {
            CommonApplication.context.getPackageManager().getPackageInfo(packageName , PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("----px----" , e.getMessage());
            return false;
        }
    }

    /**
     * 通过包名获得已经安装APK的icon
     * @param packageName apk的完整包名
     * @return icon的drawable
     */
    public static Drawable getIcon (String packageName){
        PackageManager packageManager = CommonApplication.context.getPackageManager();
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

    /**
     * 通过包名获得已安装APK的label name
     * @param packageName apk的完整包名
     * @return apk的LABEL (显示的名称)
     */
    public static String getLabelName (String packageName) {
        PackageManager packageManager = CommonApplication.context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName ,PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(applicationInfo != null){
            return applicationInfo.loadLabel(packageManager).toString();
        }else{
            return null;
        }
    }

    /**
     * 通过包名获得已安装APK的version name
     * @param packageName apk的完整包名
     * @return 版本号
     */
    public static String getVersionName (String packageName) {
        if(TextUtils.isEmpty(packageName)){
            return "apkPackageName error";
        }
        String apkVersionName = null;
        try {
            PackageManager packageManager = CommonApplication.context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName , PackageManager.GET_ACTIVITIES);
            if(packageInfo != null) {
                apkVersionName = packageInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
//            Log.d("----px----" , e.getMessage());
        }
//        Log.d("----px----" , apkVersionName);
        return apkVersionName;
    }

    /**
     * 通过包名获得已安装APK的version code
     * @param packageName apk的完整包名
     * @return 版本代号
     */
    public static int getVersionCode (String packageName) {
        if(TextUtils.isEmpty(packageName)){
            return 0;
        }
        int apkVersionCode = 0;
        try {
            PackageManager packageManager = CommonApplication.context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName , PackageManager.GET_ACTIVITIES);
            if(packageInfo != null) {
                apkVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            //Log.d("----px----" , e.getMessage());
        }
        //Log.d("----px----" , apkVersionCode+"");
        return apkVersionCode;
    }

    /**
     * 通过包名和版本编号判断apk是否是最新版本
     * @param packageName apk的完整包名
     * @param versionCode 参照的正确版本代号
     * @return 是否是最新版本
     */
    public static boolean isLastVersion (String packageName ,int versionCode) {
        if(isInstalled(packageName)) {
            int localVersionCode = getVersionCode(packageName);
            if(versionCode > localVersionCode){
                return true;
            }else {
                return false;
            }
        }else {
            return true;
        }
    }

    /**
     * 获得已存在的apk文件的包名
     * @param filePath 文件绝对路径
     * @param fileName 文件名称
     * @return apk文件的包名
     */
    public static String getApkPackageName (String filePath ,String fileName){
        ApplicationInfo applicationInfo = null;
        PackageInfo packageInfo = null;
        PackageManager packageManager = CommonApplication.context.getPackageManager();
        String apkPackageName = null;
        packageInfo = packageManager.getPackageArchiveInfo(filePath+fileName ,PackageManager.GET_ACTIVITIES);
        if(packageInfo != null){
            applicationInfo = packageInfo.applicationInfo;
            apkPackageName = applicationInfo.packageName;
        }else{
            return null;
        }
        //Log.d("----px----" ,apkPackageName);
        return apkPackageName;
    }

    /**
     * 获得已经存在的apk文件的版本号
     * @param filePath 文件绝对路径
     * @param fileName 文件名称
     * @return 版本号
     */
    public static String getApkVersionName (String filePath ,String fileName){
        ApplicationInfo applicationInfo = null;
        PackageInfo packageInfo = null;
        PackageManager packageManager = CommonApplication.context.getPackageManager();
        String apkVersionName = null;
        packageInfo = packageManager.getPackageArchiveInfo(filePath+fileName ,PackageManager.GET_ACTIVITIES);
        if(packageInfo != null){
            apkVersionName = packageInfo.versionName;
        }else{
            return null;
        }
        Log.d("----px----" ,apkVersionName);
        return apkVersionName;
    }

    /**
     * 判断apk文件是否可安装
     * @param filePath 文件绝对路径
     * @param fileName 文件名称
     * @return 是否可以安装
     */
    public static boolean isApkCanInstall (String filePath ,String fileName){
        PackageInfo packageInfo = null;
        PackageManager packageManager = CommonApplication.context.getPackageManager();
        packageInfo = packageManager.getPackageArchiveInfo(filePath+"/"+fileName ,PackageManager.GET_ACTIVITIES);
        if(packageInfo != null){
            return true;
        }else{
            return false;
        }
    }

       /**
     * 判断当前app是否需要更新
     * @param code 服务器最新版本code
     * @return 是否需要更新
     */
    public static boolean isNeedUpgrade(int code){
        if(isInstalled(CommonApplication.context.getPackageName())){
            int localCode = getVersionCode(CommonApplication.context.getPackageName());
            return localCode < code;
        }else{
            return false;
        }
    }
    
    /**
     * 获得APK文件的versionCode
     * @param filePath 文件绝对路径
     * @param fileName 文件名称
     * @return apk文件的版本代号
     */
    public static int getApkVersionCode (String filePath ,String fileName){
        ApplicationInfo applicationInfo = null;
        PackageInfo packageInfo = null;
        PackageManager packageManager = CommonApplication.context.getPackageManager();
        int apkVersionCode = 0;
        packageInfo = packageManager.getPackageArchiveInfo(filePath+fileName ,PackageManager.GET_ACTIVITIES);
        if(packageInfo != null){
            apkVersionCode = packageInfo.versionCode;
        }else{
            return 0;
        }
        Log.d("----px----" ,apkVersionCode+"");
        return apkVersionCode;
    }

    public static void installApk(String filePath , String fileName, String permission){
        if(Build.VERSION.SDK_INT > 23){
            installApkAfter7(filePath, fileName, permission);
        }else{
            installApkBefore7(filePath, fileName);
        }
    }

    /**
     * 安装apk文件
     * @param filePath 文件绝对路径
     * @param fileName 文件名称
     */
    private static void installApkBefore7 (String filePath , String fileName){
        File file = new File(filePath, fileName);
        if(!file.exists()) {
            Toast.makeText(CommonApplication.context , "Apk file is not exists" ,Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isApkCanInstall(filePath ,fileName)){
            Toast.makeText(CommonApplication.context , "Apk file can not install" ,Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        CommonApplication.context.startActivity(intent);
    }

    /**
     * 7.0 后安装apk文件
     * @param filePath 文件绝对路径
     * @param fileName 文件名称
     * @param permission manifests 中配置的file provider的permission
     */
    private static void installApkAfter7(String filePath , String fileName, String permission){
        File file = new File(filePath, fileName);
        if(!file.exists()) {
            Toast.makeText(CommonApplication.context , "Apk file is not exists" ,Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isApkCanInstall(filePath ,fileName)){
            Toast.makeText(CommonApplication.context , "Apk file can not install" ,Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri ;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            uri = FileProvider.getUriForFile(CommonApplication.context, permission, file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else{
            uri = Uri.fromFile(file);
        }
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        CommonApplication.context.startActivity(intent);
    }

    /**
     * 通过包名启动已经安装的app
     * @param context 上下文
     * @param packageName apk的包名
     */
    public static void launchApp (Context context ,String packageName){
        if(!isInstalled(packageName)){
            return;
        }
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if(intent!= null){
            context.startActivity(intent);
        }
    }
}
