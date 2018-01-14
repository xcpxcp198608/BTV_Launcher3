package com.px.common.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.px.common.constant.Constant;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.utils.CommonApplication;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtil;
import com.px.common.utils.SysUtil;
import com.px.common.utils.TimeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Map<String, String> infoMap = new HashMap<>();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private String mac = "default";

    private CrashHandler() {
    }
    private static CrashHandler INSTANCE;
    public static CrashHandler getInstance() {
        if(INSTANCE == null){
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }

    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        String ethernetMac = SysUtil.getEthernetMac();
        if(TextUtils.isEmpty(ethernetMac)){
            mac = SysUtil.getWifiMac();
        }else{
            mac = ethernetMac;
        }
    }

    /**
     * execute this method when UncaughtException occur
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Logger.d(e.getMessage());
            }
            //exit program
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * exception handle
     * @param exception exception
     * @return boolean
     */
    private boolean handleException(final Throwable exception) {
        if (exception == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                exception.printStackTrace();
                Toast.makeText(CommonApplication.context, "Unknown error, system log...", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        long lastThrowTime = (long) SPUtil.get("lastThrowTime", 0L);
        if(lastThrowTime > 0 && System.currentTimeMillis() - lastThrowTime <= 8000){
            lastThrowTime = System.currentTimeMillis();
            SPUtil.put("lastThrowTime", lastThrowTime);
            return true;
        }
        SPUtil.put("lastThrowTime", System.currentTimeMillis());
        CrashInfo crashInfo = collectDeviceInfo(CommonApplication.context);
        CrashInfo crashInfo1 = saveCrashInfoToFile(exception, crashInfo);
        if(crashInfo1 != null) reportCrash(crashInfo1);
        return true;
    }

    /**
     * collect the device information
     * @param context context
     */
    private CrashInfo collectDeviceInfo(Context context) {
        CrashInfo crashInfo = new CrashInfo();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infoMap.put("versionName", versionName);
                infoMap.put("versionCode", versionCode);
                infoMap.put("packageName", context.getPackageName());
                infoMap.put("mac", mac);
                crashInfo.setVersionName(versionName);
                crashInfo.setVersionCode(versionCode);
                crashInfo.setPackageName(context.getPackageName());
                crashInfo.setMac(mac);
            }
        } catch (Exception e) {
            Logger.e("An error occur when collect package info");
        }
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                infoMap.put(field.getName(), field.get(null).toString());
                Logger.d(field.getName() + " : " + field.get(null));
                if("MODEL".equals(field.getName())){
                    crashInfo.setModel(field.get(null).toString());
                }
                if("DISPLAY".equals(field.getName())){
                    crashInfo.setFwVersion(field.get(null).toString());
                }
            }
        } catch (Exception e) {
            Logger.d("An error occur when collect crash info");
        }
        return crashInfo;
    }

    /**
     * save exception information into log file
     * @param exception exception
     * @return log file full path + name
     */
    private CrashInfo saveCrashInfoToFile(Throwable exception, CrashInfo crashInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : infoMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            stringBuilder.append(key).append("=").append(value).append("\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        FileOutputStream fileOutputStream = null;
        try {
            exception.printStackTrace(printWriter);
            Throwable cause = exception.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            String result = "Exception： \r\n" + writer.toString();
            stringBuilder.append(result);
            stringBuilder.append("\r\n\r\n");

            crashInfo.setCrashTime(TimeUtil.getStringTime());
            crashInfo.setContent(writer.toString());

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String fileName = TimeUtil.getStringDate() + "_" + mac + "_crash.log";
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/logcat/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                fileOutputStream = new FileOutputStream(path + fileName, true);
                fileOutputStream.write((stringBuilder.toString()).getBytes());
            }
                return crashInfo;
        } catch (Exception e) {
            Logger.d("An error occur while writing file...");
        }finally {
            try {
                writer.close();
                printWriter.close();
                if(fileOutputStream != null) fileOutputStream.close();
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
        }
        return crashInfo;
    }

    private void reportCrash(CrashInfo crashInfo){
        HttpMaster.post(Constant.url.log_crash)
                .param("model", crashInfo.getModel())
                .param("fwVersion", crashInfo.getFwVersion())
                .param("mac", crashInfo.getMac())
                .param("packageName", crashInfo.getPackageName())
                .param("versionName", crashInfo.getVersionName())
                .param("versionCode", crashInfo.getVersionCode())
                .param("crashTime", crashInfo.getCrashTime())
                .param("content", crashInfo.getContent())
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws Exception {
                        Logger.d(s);
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }
}