package com.wiatec.btv_launcher.bean;

import java.util.List;

/**
 * the result that return to user after user's request
 */
public class ResultInfo<T> {

    private int code;
    private String message;
    private T data;
    private List<T> dataList;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", dataList=" + dataList +
                '}';
    }
}
