package com.wiatec.btv_launcher.bean;

/**
 * Created by patrick on 2017/3/10.
 */

public class DeviceInfo {
    private int id;
    private String mac;
    private String ethernetMac;
    private String userName;
    private String country;
    private String countryCode;
    private String city;
    private String currentLoginTime;
    private String regionName ;
    private String timeZone ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getEthernetMac() {
        return ethernetMac;
    }

    public void setEthernetMac(String ethernetMac) {
        this.ethernetMac = ethernetMac;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCurrentLoginTime() {
        return currentLoginTime;
    }

    public void setCurrentLoginTime(String currentLoginTime) {
        this.currentLoginTime = currentLoginTime;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "id=" + id +
                ", mac='" + mac + '\'' +
                ", ethernetMac='" + ethernetMac + '\'' +
                ", userName='" + userName + '\'' +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", city='" + city + '\'' +
                ", currentLoginTime='" + currentLoginTime + '\'' +
                ", regionName='" + regionName + '\'' +
                ", timeZone='" + timeZone + '\'' +
                '}';
    }
}
