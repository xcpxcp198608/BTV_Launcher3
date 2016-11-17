package com.wiatec.btv_launcher.bean;

/**
 * Created by PX on 2016-11-16.
 */

public class LocationInfo {
    private String country;
    private String city;
    private String countryCode;
    private String ip;
    private String isp;
    private String regionName;

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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", ip='" + ip + '\'' +
                ", isp='" + isp + '\'' +
                ", regionName='" + regionName + '\'' +
                '}';
    }
}
