package com.px.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by patrick on 13/10/2017.
 * create time : 11:32 AM
 */

public class TimeUtil {

    /**
     * convert "yyyy-MM-dd HH:mm:ss" format string time to unix time stamp
     * @param time string time
     * @return unix time stamp, millis seconds
     */
    public static long getUnixFromStr(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                new Locale("en"));
        Date date;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            Logger.e(e.getMessage());
            return 0;
        }
        return date.getTime();
    }

    /**
     * get current time string
     * @return current time string (format: "yyyy-MM-dd HH:mm:ss")
     */
    public static String getStringTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                new Locale("en"));
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }
    /**
     * get current time string
     * @return current time string (format: "yyyy-MM-dd")
     */
    public static String getStringDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",
                new Locale("en"));
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    /**
     * calculate the left time from now to target time, return a format string ('dd-HH-mm')
     * eg: "12days-11hours-22minutes"
     * @param expiresTime expires time, format: yyyy-MM-dd HH:mm:ss
     * @return left time, format: dd-HH-mm
     */
    public static String getLeftTimeToExpires(String expiresTime){
        long exTime = getUnixFromStr(expiresTime);
        long now = System.currentTimeMillis();
        long lTime = exTime - now;
        if(lTime <= 0){
            return "";
        }
        int day = 0, hour = 0, minute = 0;
        long second = lTime / 1000;
        if(second > 60){
            minute = (int) (second/60);
        }
        if(minute > 60){
            hour = minute / 60;
            minute = minute % 60;
        }
        if(hour > 24){
            day = hour / 24;
            hour = hour % 24;
        }
        return day + "D--" + hour + "H--" + minute + "M";
    }

    /**
     * convert the mills second of media to "HH:mm:ss" format string
     * @param millisSecond media total millis second
     * @return media format time string (format: HH:mm:ss)
     */
    public static String getMediaTime(int millisSecond) {
        int hour, minute, second;
        hour = millisSecond / 3600000;
        minute = (millisSecond - hour * 3600000) / 60000;
        second = (millisSecond - hour * 3600000 - minute * 60000) / 1000;
        String sHour, sMinute, sSecond;
        if (hour < 10) {
            sHour = "0" + String.valueOf(hour);
        } else {
            sHour = String.valueOf(hour);
        }
        if (minute < 10) {
            sMinute = "0" + String.valueOf(minute);
        } else {
            sMinute = String.valueOf(minute);
        }
        if (second < 10) {
            sSecond = "0" + String.valueOf(second);
        } else {
            sSecond = String.valueOf(second);
        }
        return sHour + ":" + sMinute + ":" + sSecond;
    }
}