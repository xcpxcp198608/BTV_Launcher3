package com.wiatec.btv_launcher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PX on 2016-11-14.
 */

public class UpdateInfo implements Parcelable {
    private int id;
    private String apkName;
    private String apkFileName;
    private String apkFileDownloadUrl;
    private String apkPackageName;
    private String apkVersionName;
    private String apkUpdateInfo;
    private int apkVersionCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getApkFileName() {
        return apkFileName;
    }

    public void setApkFileName(String apkFileName) {
        this.apkFileName = apkFileName;
    }

    public String getApkFileDownloadUrl() {
        return apkFileDownloadUrl;
    }

    public void setApkFileDownloadUrl(String apkFileDownloadUrl) {
        this.apkFileDownloadUrl = apkFileDownloadUrl;
    }

    public String getApkPackageName() {
        return apkPackageName;
    }

    public void setApkPackageName(String apkPackageName) {
        this.apkPackageName = apkPackageName;
    }

    public String getApkVersionName() {
        return apkVersionName;
    }

    public void setApkVersionName(String apkVersionName) {
        this.apkVersionName = apkVersionName;
    }

    public String getApkUpdateInfo() {
        return apkUpdateInfo;
    }

    public void setApkUpdateInfo(String apkUpdateInfo) {
        this.apkUpdateInfo = apkUpdateInfo;
    }

    public int getApkVersionCode() {
        return apkVersionCode;
    }

    public void setApkVersionCode(int apkVersionCode) {
        this.apkVersionCode = apkVersionCode;
    }

    @Override
    public String toString() {
        return "Updater{" +
                "id=" + id +
                ", apkName='" + apkName + '\'' +
                ", apkFileName='" + apkFileName + '\'' +
                ", apkFileDownloadUrl='" + apkFileDownloadUrl + '\'' +
                ", apkPackageName='" + apkPackageName + '\'' +
                ", apkVersionName='" + apkVersionName + '\'' +
                ", apkUpdateInfo='" + apkUpdateInfo + '\'' +
                ", apkVersionCode=" + apkVersionCode +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.apkName);
        dest.writeString(this.apkFileName);
        dest.writeString(this.apkFileDownloadUrl);
        dest.writeString(this.apkPackageName);
        dest.writeString(this.apkVersionName);
        dest.writeString(this.apkUpdateInfo);
        dest.writeInt(this.apkVersionCode);
    }

    public UpdateInfo() {
    }

    protected UpdateInfo(Parcel in) {
        this.id = in.readInt();
        this.apkName = in.readString();
        this.apkFileName = in.readString();
        this.apkFileDownloadUrl = in.readString();
        this.apkPackageName = in.readString();
        this.apkVersionName = in.readString();
        this.apkUpdateInfo = in.readString();
        this.apkVersionCode = in.readInt();
    }

    public static final Parcelable.Creator<UpdateInfo> CREATOR = new Parcelable.Creator<UpdateInfo>() {
        @Override
        public UpdateInfo createFromParcel(Parcel source) {
            return new UpdateInfo(source);
        }

        @Override
        public UpdateInfo[] newArray(int size) {
            return new UpdateInfo[size];
        }
    };
}
