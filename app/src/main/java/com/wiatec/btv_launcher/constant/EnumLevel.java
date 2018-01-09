package com.wiatec.btv_launcher.constant;

/**
 * Created by patrick on 09/01/2018.
 * create time : 9:46 AM
 */

public enum EnumLevel {

    L0("0"), L1("1"), L2("2"), L3("3"), L4("4"), L5("5");

    private String l;

    EnumLevel(String l) {
        this.l = l;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    @Override
    public String toString() {
        return "Level{" +
                "l='" + l + '\'' +
                '}';
    }
}
