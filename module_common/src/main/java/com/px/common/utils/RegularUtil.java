package com.px.common.utils;

import java.util.regex.Pattern;

/**
 * regular util
 */
public class RegularUtil {

    /**
     * validate email input format
     */
    public static boolean validateEmail(String email){
        String regular = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regular);
        return pattern.matcher(email).matches();
    }
}
