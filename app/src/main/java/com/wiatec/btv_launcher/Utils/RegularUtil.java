package com.wiatec.btv_launcher.Utils;

import java.util.regex.Pattern;

/**
 * regular util
 */
public class RegularUtil {

    /**
     * validate email input format
     */
    public static boolean validateEmail(String email){
        String regular = "(.)+@(\\w)*(.(\\w)+)+$";
        Pattern pattern = Pattern.compile(regular);
        return pattern.matcher(email).matches();
    }
}
