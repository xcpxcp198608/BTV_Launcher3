package com.wiatec.btv_launcher.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by PX on 2016-10-14.
 */

public class InstalledApp {
    private int id;
    private String appName;
    private String appPackageName;
    private String type;
    private String launcherName;
    private int sequence;
    private boolean isSystemApp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLauncherName() {
        return launcherName;
    }

    public void setLauncherName(String launcherName) {
        this.launcherName = launcherName;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    @Override
    public String toString() {
        return "InstalledApp{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", appPackageName='" + appPackageName + '\'' +
                ", type='" + type + '\'' +
                ", launcherName='" + launcherName + '\'' +
                ", sequence=" + sequence +
                ", isSystemApp=" + isSystemApp +
                '}';
    }
}
