package com.wiatec.btv_launcher.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xuchengpeng on 10/05/2017.
 */

public class RegExpUtils {

    public static boolean validateEmail(String email){
        String reg = "/(\\w)+@(\\w)*(.(\\w)+)+$/";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validatePassword(String password){
        String reg = "/(\\w){6}/";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
