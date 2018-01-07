package com.px.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * shared preferences utils
 */

public class SPUtil {

    private static final String NAME = "sp";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    static {
        sharedPreferences = CommonApplication.context.getSharedPreferences(NAME , Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 根据传入的object对象类型存成对应类型的键值对
     * @param key key
     * @param object type: String, int, boolean, float , long
     */
    public static synchronized void put (String key , Object object){
        if(key ==null || object==null){
            return;
        }
        if(object instanceof String){
            editor.putString(key , (String) object);
        }else if(object instanceof Integer){
            editor.putInt(key , (int) object);
        }else if(object instanceof Boolean){
            editor.putBoolean(key , (boolean) object);
        }else if(object instanceof Float){
            editor.putFloat(key , (float) object);
        }else if(object instanceof Long){
            editor.putLong(key , (long) object);
        }else{
            editor.putString(key , object.toString());
        }
        editor.apply();
    }

    /**
     * 根据默认值类型从shared preferences中取出对应类型和Key的值
     * @param key key
     * @param defaultObject type: String, int, boolean, float , long
     * @return key 存在时返回对应的值，不存在时返回默认值,默认值类型错误时返回Null
     */
    public static synchronized Object get(String key , Object defaultObject){
        if(defaultObject instanceof String){
            return sharedPreferences.getString(key , (String) defaultObject);
        }else  if(defaultObject instanceof Integer){
            return sharedPreferences.getInt(key , (int) defaultObject);
        }else  if(defaultObject instanceof Boolean){
            return sharedPreferences.getBoolean(key , (boolean) defaultObject);
        }else  if(defaultObject instanceof Float){
            return sharedPreferences.getFloat(key , (float) defaultObject);
        }else  if(defaultObject instanceof Long) {
            return sharedPreferences.getLong(key, (long) defaultObject);
        }else {
            return null;
        }
    }

    /**
     * 从 shared preferences 删除对应key的键值对
     * @param key key
     */
    public static synchronized boolean remove (String key){
        try{
            editor.remove(key);
            editor.apply();
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * 清空shared preferences 中所有的键值对
     */
    public static synchronized boolean clear (){
        try{
            editor.clear();
            editor.apply();
            return true;
        }catch(Exception e){
            return false;
        }

    }

    /**
     * 判断 shared preferences 中是否包含传入的key
     * @param key key
     * @return is contains
     */
    public static synchronized boolean contains (String key){
        return sharedPreferences.contains(key);
    }

    /**
     * 获取 shared preferences 中所有的键值对并存入一个map中
     * @return map
     */
    public static synchronized Map<String, ?> getAll(){
        return sharedPreferences.getAll();
    }

}