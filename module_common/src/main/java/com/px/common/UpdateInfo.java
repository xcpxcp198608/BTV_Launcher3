package com.px.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by PX on 2016-11-14.
 */

public class UpdateInfo implements Parcelable {
    private int id;
    private String name;
    private String fileName;
    private String url;
    private String packageName;
    private String version;
    private String info;
    private int code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        info = info;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", url='" + url + '\'' +
                ", packageName='" + packageName + '\'' +
                ", version='" + version + '\'' +
                ", info='" + info + '\'' +
                ", code=" + code +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.fileName);
        dest.writeString(this.url);
        dest.writeString(this.packageName);
        dest.writeString(this.version);
        dest.writeString(this.info);
        dest.writeInt(this.code);
    }

    public UpdateInfo() {
    }

    protected UpdateInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.fileName = in.readString();
        this.url = in.readString();
        this.packageName = in.readString();
        this.version = in.readString();
        this.info = in.readString();
        this.code = in.readInt();
    }

    public static final Creator<UpdateInfo> CREATOR = new Creator<UpdateInfo>() {
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
