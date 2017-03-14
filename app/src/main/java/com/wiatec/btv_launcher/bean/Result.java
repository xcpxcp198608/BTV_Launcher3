package com.wiatec.btv_launcher.bean;

/**
 * Created by patrick on 2017/3/4.
 */

public class Result {

    public static final int CODE_OK = 100;
    public static final String STATUS_OK = "request success";

    public static final int CODE_USERNAME_ERROR = 101;
    public static final String STATUS_USERNAME_ERROR = "User Name Error";

    public static final int CODE_PASSWORD_ERROR = 102;
    public static final String STATUS_PASSWORD_ERROR = "Password Error";

    public static final int CODE_TOKEN_ERROR = 103;
    public static final String STATUS_TOKEN_ERROR = "Token Error";

    public static final int CODE_EMAIL_EXISTS = 104;
    public static final String STATUS_EMAIL_EXISTS = "Email exists";

    public static final int CODE_USERNAME_EXISTS = 105;
    public static final String STATUS_USERNAME_EXISTS = "UserName exists";

    public static final int CODE_REGISTER_OK = 106;
    public static final String STATUS_REGISTER_OK = "Register success";

    public static final int CODE_REGISTER_ERROR = 107;
    public static final String STATUS_REGISTER_ERROR = "Register failure";

    public static final int CODE_EMAIL_CHECK_ERROR = 108;
    public static final String STATUS_EMAIL_CHECK_ERROR = "Email have no confirm";

    public static final int CODE_INPUT_ERROR = 109;
    public static final String STATUS_INPUT_ERROR = "input error";

    public static final int CODE_LOGIN_ERROR = 110;
    public static final String STATUS_LOGIN_ERROR = "Login Error";

    public static final int CODE_LOGIN_REPEAT = 111;
    public static final String STATUS_LOGIN_REPEAT = "Login Repeat";

    public static final int CODE_EMAIL_CONFIRM_ERROR = 112;
    public static final String STATUS_EMAIL_CONFIRM_ERROR = "Email confirm failure";

    private int code;
    private String status;
    private int count;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", count=" + count +
                '}';
    }
}
