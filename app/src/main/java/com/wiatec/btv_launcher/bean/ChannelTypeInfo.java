package com.wiatec.btv_launcher.bean;

/**
 * Created by patrick on 2017/3/10.
 */

public class ChannelTypeInfo {
    private int id;
    private int flag;
    private String name;
    private String icon;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getFlag() {
        return flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    @Override
    public String toString() {
        return "ChannelTypeInfo [id=" + id + ", flag=" + flag + ", name="
                + name + ", icon=" + icon + "]";
    }
}
