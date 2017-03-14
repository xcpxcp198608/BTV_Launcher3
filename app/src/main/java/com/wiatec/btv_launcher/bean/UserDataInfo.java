package com.wiatec.btv_launcher.bean;

/**
 * Created by patrick on 2017/3/14.
 */

public class UserDataInfo {
    private int id;
    private String userName;
    private String ip;
    private String mac;
    private String country;
    private String city;
    private String exitTime;
    private String stayTime;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getExitTime() {
        return exitTime;
    }
    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }
    public String getStayTime() {
        return stayTime;
    }
    public void setStayTime(String stayTime) {
        this.stayTime = stayTime;
    }
    @Override
    public String toString() {
        return "UserData [id=" + id + ", userName=" + userName + ", ip=" + ip
                + ", mac=" + mac + ", country=" + country + ", city=" + city
                + ", exitTime=" + exitTime + ", stayTime=" + stayTime + "]";
    }
}
