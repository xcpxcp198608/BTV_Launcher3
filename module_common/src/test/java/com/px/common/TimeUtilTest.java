package com.px.common;

import com.px.common.utils.TimeUtil;

import org.junit.Test;

/**
 * Created by patrick on 13/10/2017.
 * create time : 11:38 AM
 */

public class TimeUtilTest {

    @Test
    public void testGetUnixTime(){
        long l = TimeUtil.getUnixFromStr("2017-10-12 11:10:10");
        System.out.println(l);
    }

    @Test
    public void testGetStringTime(){
        String s = TimeUtil.getStringTime();
        System.out.println(s);
    }
}
