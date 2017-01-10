package com.wiatec.btv_launcher.bean;

/**
 * Created by PX on 2016-11-14.
 */

public class ImageInfo {
    private int id;
    private String name;
    private String url;
    private String link;
    private int query_flag;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getQuery_flag() {
        return query_flag;
    }

    public void setQuery_flag(int query_flag) {
        this.query_flag = query_flag;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", link='" + link + '\'' +
                ", query_flag=" + query_flag +
                '}';
    }
}
