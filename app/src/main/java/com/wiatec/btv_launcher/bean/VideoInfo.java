package com.wiatec.btv_launcher.bean;

/**
 * Created by PX on 2016-11-14.
 */

public class VideoInfo {
    private int id;
    private String name;
    private String url;
    private String md5;
    private int playInterval;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getPlayInterval() {
        return playInterval;
    }

    public void setPlayInterval(int playInterval) {
        this.playInterval = playInterval;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", md5='" + md5 + '\'' +
                ", playInterval=" + playInterval +
                '}';
    }
}
