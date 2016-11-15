package com.wiatec.btv_launcher.Utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by PX on 2016/9/11.
 */
public class MD5 {

    public static String getFileMD5(String filePath,String fileName) {
        File file = new File(filePath,fileName);
        if (!file.isFile()) {
            return "11";
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
       // Log.d("----px----" ,"---->"+bigInt.toString(16));
        return bigInt.toString(16);
    }
}