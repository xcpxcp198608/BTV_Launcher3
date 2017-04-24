package com.wiatec.btv_launcher.bean;

/**
 * Created by xuchengpeng on 24/04/2017.
 */

public class MessageListInfo {
    private int id;
    private String name;
    private String img1;
    private String img2;
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageListInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", img1='" + img1 + '\'' +
                ", img2='" + img2 + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
