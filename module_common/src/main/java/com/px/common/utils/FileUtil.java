package com.px.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * file utils
 */

public class FileUtil {

    /**
     * 获取当前应用内部存储目录下的download目录路径， 没有则创建改目录
     * @return download path
     */
    public static String getDownloadPath(){
        File externalFilesDir = CommonApplication.context.getExternalFilesDir("download");
        if(externalFilesDir != null) {
            return externalFilesDir.getAbsolutePath();
        }else{
            return "";
        }
    }

    /**
     * 获取当前应用内部存储目录下指定名称的目录路径， 没有则创建改目录
     * @return download path
     */
    public static String getPathWith(String dir){
        File externalFilesDir = CommonApplication.context.getExternalFilesDir(dir);
        if(externalFilesDir != null) {
            return externalFilesDir.getAbsolutePath();
        }else{
            return "";
        }
    }

    /**
     * 通过file path 和 file name 判断文件是否存在
     * @param filePath 文件绝对路径
     * @param fileName 文件名称
     * @return 是否存在
     */
    public static boolean isExists(String filePath ,String fileName ) {
        try {
            return new File(filePath + fileName).exists();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 通过file path 和 file name ，md5判断文件是否完整
     * @param filePath 文件绝对路径
     * @param fileName 文件名称
     * @param md5 参照的正确md5值
     * @return 文件是否完整
     */
    public static boolean isIntact (String filePath,String fileName,String md5){
        try {
            String localMD5 = getMD5(filePath ,fileName);
            //Logger.d(localMD5);
            return localMD5.equalsIgnoreCase(md5);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获得文件的MD5
     * @param filePath 文件绝对路径
     * @param fileName 文件名称
     * @return 文件的md5值
     */
    public static String getMD5(String filePath,String fileName) {
        try {
            File file = new File(filePath,fileName);
            if (!file.isFile()) {
                return "1";
            }
            MessageDigest digest = null;
            FileInputStream in = null;
            byte buffer[] = new byte[1024];
            int len;
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            Logger.e(e.getMessage());
            return "1";
        }
    }

    /**
     * 删除文件
     * @param filePath 文件绝对路径
     * @param fileName 文件名称
     * @return 是否删除成功
     */
    public static boolean delete(String filePath ,String fileName ) {
        try {
            return new File(filePath + "/" + fileName).delete();
        } catch (Exception e) {
            Logger.e(e.getMessage());
            return false;
        }
    }
}
