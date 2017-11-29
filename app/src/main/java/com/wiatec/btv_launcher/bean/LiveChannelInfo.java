package com.wiatec.btv_launcher.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * channel info
 */

public class LiveChannelInfo implements Parcelable {

    private int id;
    private String title;
    private String message;
    private String url;
    private String playUrl;
    private String preview;
    private String category;
    private boolean available;
    /**
     * 1:default live
     */
    private int type;
    private float price;
    private String startTime;
    private int userId;

    public LiveChannelInfo() {
    }

    public LiveChannelInfo(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LiveChannelInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", url='" + url + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", preview='" + preview + '\'' +
                ", category='" + category + '\'' +
                ", available=" + available +
                ", type=" + type +
                ", price=" + price +
                ", startTime='" + startTime + '\'' +
                ", userId=" + userId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.message);
        dest.writeString(this.url);
        dest.writeString(this.playUrl);
        dest.writeString(this.preview);
        dest.writeString(this.category);
        dest.writeByte(this.available ? (byte) 1 : (byte) 0);
        dest.writeInt(this.type);
        dest.writeFloat(this.price);
        dest.writeString(this.startTime);
        dest.writeInt(this.userId);
    }

    protected LiveChannelInfo(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.message = in.readString();
        this.url = in.readString();
        this.playUrl = in.readString();
        this.preview = in.readString();
        this.category = in.readString();
        this.available = in.readByte() != 0;
        this.type = in.readInt();
        this.price = in.readFloat();
        this.startTime = in.readString();
        this.userId = in.readInt();
    }

    public static final Creator<LiveChannelInfo> CREATOR = new Creator<LiveChannelInfo>() {
        @Override
        public LiveChannelInfo createFromParcel(Parcel source) {
            return new LiveChannelInfo(source);
        }

        @Override
        public LiveChannelInfo[] newArray(int size) {
            return new LiveChannelInfo[size];
        }
    };
}
