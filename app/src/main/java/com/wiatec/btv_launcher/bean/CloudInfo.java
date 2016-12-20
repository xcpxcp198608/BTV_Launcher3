package com.wiatec.btv_launcher.bean;

/**
 * Created by patrick on 2016/12/20.
 */

public class CloudInfo {
    private String token;
    private String url;

    @Override
    public String toString() {
        return "CloudInfo{" +
                "token='" + token + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
