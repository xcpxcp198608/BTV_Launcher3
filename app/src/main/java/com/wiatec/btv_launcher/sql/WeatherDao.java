package com.wiatec.btv_launcher.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wiatec.btv_launcher.bean.WeatherInfo;

/**
 * Created by PX on 2016-11-17.
 */

public class WeatherDao {

    private SQLiteDatabase sqLiteDatabase;

    private WeatherDao (Context context) {
        sqLiteDatabase = new SQLiteHelper(context).getWritableDatabase();
    }
    private volatile static WeatherDao instance;
    public static WeatherDao getInstance (Context context) {
        if(instance ==null){
            synchronized (WeatherDao.class){
                if(instance ==null){
                    instance = new WeatherDao(context);
                }
            }
        }
        return instance;
    }

    public boolean deleteAll (){
        boolean flag = true;
        try{
            sqLiteDatabase.delete(SQLiteHelper.WEATHER_TABLE ,"_id>?" ,new String [] {"0"});
        }catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public boolean insertWeather (WeatherInfo weatherInfo){
        boolean flag = true;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("country", weatherInfo.getCountry());
            contentValues.put("city", weatherInfo.getCity());
            contentValues.put("icon", weatherInfo.getIcon());
            contentValues.put("weather", weatherInfo.getWeather());
            contentValues.put("weatherDescription", weatherInfo.getWeatherDescription());
            contentValues.put("temperature", weatherInfo.getTemperature());
            contentValues.put("humidity", weatherInfo.getHumidity());
            contentValues.put("pressure", weatherInfo.getPressure());
            contentValues.put("maxTemperature", weatherInfo.getMaxTemperature());
            contentValues.put("minTemperature", weatherInfo.getMinTemperature());
            contentValues.put("deg", weatherInfo.getDeg());
            contentValues.put("spd", weatherInfo.getSpd());
            contentValues.put("sunrise", weatherInfo.getSunrise());
            contentValues.put("sunset", weatherInfo.getSunset());
            contentValues.put("date", weatherInfo.getDate());
            sqLiteDatabase.insert(SQLiteHelper.WEATHER_TABLE, null, contentValues);
        }catch (Exception e){
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public WeatherInfo query(){
        WeatherInfo weatherInfo =null;
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.WEATHER_TABLE ,null , "_id>?" , new String []{"0"} , null , null ,null);
        while (cursor.moveToNext()){
            weatherInfo = new WeatherInfo();
            weatherInfo.setCountry(cursor.getString(cursor.getColumnIndex("country")));
            weatherInfo.setCity(cursor.getString(cursor.getColumnIndex("city")));
            weatherInfo.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
            weatherInfo.setWeather(cursor.getString(cursor.getColumnIndex("weather")));
            weatherInfo.setWeatherDescription(cursor.getString(cursor.getColumnIndex("weatherDescription")));
            weatherInfo.setTemperature(cursor.getString(cursor.getColumnIndex("temperature")));
            weatherInfo.setHumidity(cursor.getString(cursor.getColumnIndex("humidity")));
            weatherInfo.setPressure(cursor.getString(cursor.getColumnIndex("pressure")));
            weatherInfo.setMaxTemperature(cursor.getString(cursor.getColumnIndex("maxTemperature")));
            weatherInfo.setMinTemperature(cursor.getString(cursor.getColumnIndex("minTemperature")));
            weatherInfo.setDeg(cursor.getString(cursor.getColumnIndex("deg")));
            weatherInfo.setSpd(cursor.getString(cursor.getColumnIndex("spd")));
            weatherInfo.setSunrise(cursor.getString(cursor.getColumnIndex("sunrise")));
            weatherInfo.setSunset(cursor.getString(cursor.getColumnIndex("sunset")));
            weatherInfo.setDate(cursor.getString(cursor.getColumnIndex("date")));
        }
        if(cursor!=null){
            cursor.close();
        }
        return weatherInfo;
    }
}
