package com.wiatec.btv_launcher.bean;

/**
 * Created by PX on 2016-12-12.
 */

public class CloudImageInfo {
    private int id;
    private String name;
    private String url;
    private String finished;
    private String path;

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

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "CloudImageInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", finished='" + finished + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
