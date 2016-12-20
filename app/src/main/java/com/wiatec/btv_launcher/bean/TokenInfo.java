package com.wiatec.btv_launcher.bean;

/**
 * Created by PX on 2016-12-01.
 */

public class TokenInfo {
    private String result;
    private String error;
    private String token;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenInfo{" +
                "result='" + result + '\'' +
                ", error='" + error + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
