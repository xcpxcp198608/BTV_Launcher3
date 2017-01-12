package com.wiatec.btv_launcher.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PX on 2016-10-14.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "LAUNCHER";

    public static final String TABLE_NAME = "INSTALLED_APP";
    private static final String CREATE_TABLE = "create table if not exists "+TABLE_NAME+"(_id integer primary key autoincrement," +
            "appName text ,appPackageName text,type text,launcherName text,sequence integer)";
    private static final String DROP_TABLE= "drop table if exists "+TABLE_NAME;

    public static final String MESSAGE_TABLE = "MESSAGE";
    private static final String CREATE_TABLE_MESSAGE = "create table if not exists "+MESSAGE_TABLE+"(_id integer primary key autoincrement," +
            "title text ,content text,icon text,link text,isRead text,type text)";
    private static final String DROP_TABLE_MESSAGE= "drop table if exists "+MESSAGE_TABLE;

    public static final String WEATHER_TABLE = "WEATHER";
    private static final String CREATE_TABLE_WEATHER = "create table if not exists "+WEATHER_TABLE+"(_id integer primary key autoincrement," +
            "country text ,city text,icon text,weather text,weatherDescription text,temperature text,humidity text," +
            "pressure text,maxTemperature text,minTemperature text,deg text,spd text,sunrise text,sunset text,date text)";
    private static final String DROP_TABLE_WEATHER= "drop table if exists "+WEATHER_TABLE;

    public static final String CLOUD_IMAGE_TABLE = "CLOUD_IMAGE";
    private static final String CREATE_TABLE_CLOUD_IMAGE = "create table if not exists "+CLOUD_IMAGE_TABLE+"(_id integer primary key autoincrement," +
            "name text ,url text,path text,finished text)";
    private static final String DROP_TABLE_CLOUD_IMAGE= "drop table if exists "+CLOUD_IMAGE_TABLE;

    public static final String CHANNEL_TABLE = "CHANNEL";
    private static final String CREATE_TABLE_CHANNEL = "create table if not exists "+CHANNEL_TABLE+"(_id integer primary key autoincrement," +
            "name text ,url text,icon text,type text ,country text,sequence integer,style text,sequence1 integer)";
    private static final String DROP_TABLE_CHANNEL= "drop table if exists "+CHANNEL_TABLE;

    private static final int VERSION =11;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_MESSAGE);
        db.execSQL(CREATE_TABLE_WEATHER);
        db.execSQL(CREATE_TABLE_CLOUD_IMAGE);
        db.execSQL(CREATE_TABLE_CHANNEL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        db.execSQL(DROP_TABLE_MESSAGE);
        db.execSQL(DROP_TABLE_WEATHER);
        db.execSQL(DROP_TABLE_CLOUD_IMAGE);
        db.execSQL(DROP_TABLE_CHANNEL);
        this.onCreate(db);
    }
}
