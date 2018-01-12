package com.px.common.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * AES encrypt and decrypt
 */
public class AESUtil {

    public static final String VIPARA = "1269571569321021";
    public static final String BM = "utf-8";

    public static final String KEY = "www.wiatec.com";

    public static String MD5 (String s){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(s.getBytes());
            return new BigInteger(1 ,messageDigest.digest()).toString(16);
        } catch (Exception e) {
            Logger.e(e.getMessage());
            return "md5_error";
        }
    }

    private static String byte2HexStr(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String s = Integer.toHexString(b[i] & 0xFF);
            if (s.length() == 1) {
                sb.append("0");
            }
            sb.append(s.toUpperCase());
        }
        return sb.toString();
    }

    private static byte[] str2ByteArray(String s) {
        int byteArrayLength = s.length() / 2;
        byte[] b = new byte[byteArrayLength];
        for (int i = 0; i < byteArrayLength; i++) {
            byte b0 = (byte) Integer.valueOf(s.substring(i * 2, i * 2 + 2), 16)
                    .intValue();
            b[i] = b0;
        }
        return b;
    }

    public static String encrypt(String content, String key) {
        try {
            key = MD5(key);
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, zeroIv);
            byte[] encryptedData = cipher.doFinal(content.getBytes(BM));
//            return Base64.encode(encryptedData);
          return byte2HexStr(encryptedData);
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return "encrypt_error";
    }

    public static String decrypt(String content, String key) {
        try {
            key = MD5(key);
//            byte[] byteMi = Base64.decode(content);
          byte[] byteMi=  str2ByteArray(content);
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, zeroIv);
            byte[] decryptedData = cipher.doFinal(byteMi);
            return new String(decryptedData, "utf-8");
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return "decrypt_error";
    }

}
