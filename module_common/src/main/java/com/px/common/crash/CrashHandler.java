package com.px.common.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.px.common.utils.CommonApplication;
import com.px.common.utils.Logger;
import com.px.common.utils.TimeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Map<String, String> infoMap = new HashMap<>();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ", Locale.CHINA);

    private CrashHandler() {
    }
    private static CrashHandler INSTANCE = new CrashHandler();
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Logger.d(e.getMessage());
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     * @param exception exception
     * @return true:如果处理了该异常信息;否则返回false.
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
                Toast.makeText(CommonApplication.context, "Unexpected error, system log", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        // 收集设备参数信息
        collectDeviceInfo(CommonApplication.context);
        // 保存日志文件
        saveCrashInfoToFile(exception);
        return true;
    }

    /**
     * 收集设备参数信息
     * @param context context
     */
    private void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infoMap.put("versionName", versionName);
                infoMap.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.d("an error occured when collect package info");
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infoMap.put(field.getName(), field.get(null).toString());
                Logger.d(field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Logger.d("an error occured when collect crash info");
            }
        }
    }

    /**
     * 保存错误信息到文件中
     * @param exception exception
     * @return 返回文件名称,便于将文件传送到服务器
     */
    private String saveCrashInfoToFile(Throwable exception) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : infoMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            stringBuilder.append(key).append("=").append(value).append("\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        exception.printStackTrace(printWriter);
        Throwable cause = exception.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        String time = formatter.format(new Date());
        stringBuilder.append(time).append(result);
        try {
            String fileName = TimeUtil.getStringTime() + "_crash.log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName, true);
                fos.write((stringBuilder.toString()).getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Logger.d("an error occured while writing file...");
        }
        return null;
    }
}