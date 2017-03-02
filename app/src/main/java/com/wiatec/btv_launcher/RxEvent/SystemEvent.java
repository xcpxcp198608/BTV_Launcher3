package com.wiatec.btv_launcher.RxEvent;

/**
 * Created by patrick on 2017/3/2.
 */

public class SystemEvent {

    public static final int CODE_NET_STATUS_CHANGE = 1;
    public static final int CODE_LOCATION_CHANGE = 2;
    public static final int CODE_WIFI_STATUS_CHANGE = 3;
    public static final int CODE_WEATHER_STATUS_CHANGE = 4;

    public static final int STATUS_NET_CONNECT = 11;
    public static final int STATUS_NET_DISCONNECT = 12;
    public static final int STATUS_WIFI_LEVEL4 = 14;
    public static final int STATUS_WIFI_LEVEL3 = 15;
    public static final int STATUS_WIFI_LEVEL2 = 16;
    public static final int STATUS_WIFI_LEVEL1 = 17;
    public static final int STATUS_WIFI_LEVEL0 = 18;


    private int code;
    private int statusCode;
    private String statusInfo;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(String statusInfo) {
        this.statusInfo = statusInfo;
    }

    @Override
    public String toString() {
        return "SystemEvent{" +
                "code=" + code +
                ", statusCode=" + statusCode +
                ", statusInfo='" + statusInfo + '\'' +
                '}';
    }
}
