package com.px.common.crash;

/**
 * Created by patrick on 13/01/2018.
 * create time : 11:52 AM
 */

public class CrashInfo {

    private String model;
    private String fwVersion;
    private String mac;
    private String packageName;
    private String versionName;
    private String versionCode;
    private String crashTime;
    private String content;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFwVersion() {
        return fwVersion;
    }

    public void setFwVersion(String fwVersion) {
        this.fwVersion = fwVersion;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getCrashTime() {
        return crashTime;
    }

    public void setCrashTime(String crashTime) {
        this.crashTime = crashTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CrashInfo{" + "\r\n"+
                "model='" + model + '\'' + "\r\n"+
                ", fwVersion='" + fwVersion + '\'' + "\r\n"+
                ", mac='" + mac + '\'' + "\r\n"+
                ", packageName='" + packageName + '\'' + "\r\n"+
                ", versionName='" + versionName + '\'' + "\r\n"+
                ", versionCode='" + versionCode + '\'' + "\r\n"+
                ", crashTime='" + crashTime + '\'' + "\r\n"+
                ", content='" + content + '\'' + "\r\n"+
                '}';
    }
}
