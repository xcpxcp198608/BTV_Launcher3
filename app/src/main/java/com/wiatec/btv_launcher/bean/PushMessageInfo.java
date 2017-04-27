package com.wiatec.btv_launcher.bean;

/**
 * Created by xuchengpeng on 27/04/2017.
 */

public class PushMessageInfo {
    private int id;
    private String userName;
    private String time;
    private String message;
    private String img1;
    private String img2;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    @Override
    public String toString() {
        return "PushMessageInfo{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", time='" + time + '\'' +
                ", message='" + message + '\'' +
                ", img1='" + img1 + '\'' +
                ", img2='" + img2 + '\'' +
                '}';
    }
}
