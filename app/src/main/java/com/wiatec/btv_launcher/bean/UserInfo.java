package com.wiatec.btv_launcher.bean;

/**
 * Created by patrick on 2017/3/10.
 */

public class UserInfo {
    private int id;
    private String userName;
    private String password;
    private String email;
    private String token;
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    @Override
    public String toString() {
        return "UserInfo [id=" + id + ", userName=" + userName + ", password="
                + password + ", email=" + email + ", token=" + token + "]";
    }
}
