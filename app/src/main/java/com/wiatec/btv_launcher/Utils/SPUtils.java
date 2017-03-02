package com.wiatec.btv_launcher.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by patrick on 2017/3/2.
 */

public class SPUtils {

    private static final String NAME = "sp";

    public static void put (Context context , String key , Object object){
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
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

    public static Object get(Context context , String key , Object defaultObject){
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME  ,Context.MODE_PRIVATE);
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
        }
        return null;
    }

    public static void remove (Context context , String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME  ,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void clear (Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME  ,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static boolean contains (Context context , String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME  ,Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }

    public static Map<String, ?> getAll(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME  ,Context.MODE_PRIVATE);
        return sharedPreferences.getAll();
    }

}
