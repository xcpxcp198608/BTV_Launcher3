package com.wiatec.btv_launcher.bean;

/**
 * Created by PX on 2016-11-14.
 */

public class ChannelInfo {
    private int id;
    private String name;
    private String url;
    private String icon;
    private String type;
    private String country;
    private int sequence;
    private String style;
    private int sequence1;

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

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getSequence1() {
        return sequence1;
    }

    public void setSequence1(int sequence1) {
        this.sequence1 = sequence1;
    }

    @Override
    public String toString() {
        return "ChannelInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", type='" + type + '\'' +
                ", country='" + country + '\'' +
                ", sequence=" + sequence +
                ", style='" + style + '\'' +
                ", sequence1=" + sequence1 +
                '}';
    }
}
