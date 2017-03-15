package com.wiatec.btv_launcher.bean;

/**
 * Created by PX on 2016-11-14.
 */

public class ChannelInfo {
    private int id;
    private int sequence;
    private int visible;
    private String tag;
    private String name;
    private String url;
    private String icon;
    private String type;
    private String country;
    private String style;

    @Override
    public String toString() {
        return "ChannelInfo{" +
                "id=" + id +
                ", sequence=" + sequence +
                ", visible=" + visible +
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", type='" + type + '\'' +
                ", country='" + country + '\'' +
                ", style='" + style + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
