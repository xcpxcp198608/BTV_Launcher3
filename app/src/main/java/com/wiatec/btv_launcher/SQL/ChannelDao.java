package com.wiatec.btv_launcher.SQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.bean.ChannelInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 2016/12/28.
 */

public class ChannelDao {
    private SQLiteDatabase sqLiteDatabase;

    private ChannelDao(){
        sqLiteDatabase = new SQLiteHelper(Application.getContext()).getWritableDatabase();
    }

    private volatile static ChannelDao instance;
    public static ChannelDao getInstance(){
        if(instance == null){
            synchronized (ChannelDao.class){
                if(instance == null){
                    instance = new ChannelDao();
                }
            }
        }
        return instance;
    }

    public boolean isExists (ChannelInfo channelInfo){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.CHANNEL_TABLE ,null , "name=?" , new String[]{channelInfo.getName()} ,null,null,null);
        boolean flag = cursor.moveToNext();
        if(cursor != null){
            cursor.close();
        }
        return flag;
    }

    public void insert(ChannelInfo channelInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",channelInfo.getName());
        contentValues.put("url",channelInfo.getUrl());
        contentValues.put("icon",channelInfo.getIcon());
        contentValues.put("type",channelInfo.getType());
        contentValues.put("country",channelInfo.getCountry());
        contentValues.put("sequence",channelInfo.getSequence());
        contentValues.put("style",channelInfo.getStyle());
        contentValues.put("sequence1",channelInfo.getSequence1());
        sqLiteDatabase.insert(SQLiteHelper.CHANNEL_TABLE ,null ,contentValues);
    }

    public void update (ChannelInfo channelInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",channelInfo.getName());
        contentValues.put("url",channelInfo.getUrl());
        contentValues.put("icon",channelInfo.getIcon());
        contentValues.put("type",channelInfo.getType());
        contentValues.put("country",channelInfo.getCountry());
        contentValues.put("sequence",channelInfo.getSequence());
        contentValues.put("style",channelInfo.getStyle());
        contentValues.put("sequence1",channelInfo.getSequence1());
        sqLiteDatabase.update(SQLiteHelper.CHANNEL_TABLE ,contentValues , "name=?" , new String []{channelInfo.getName()});
    }


    public void insertOrUpdate(ChannelInfo channelInfo){
        if(!isExists(channelInfo)){
            insert(channelInfo);
        }else{
            update(channelInfo);
        }
    }

    public void delete(){
        sqLiteDatabase.delete(SQLiteHelper.CHANNEL_TABLE ,"_id>?" ,new String []{"0"});
    }

    public List<ChannelInfo> queryByCountry(String country){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.CHANNEL_TABLE ,null,"country=?" , new String []{country} ,null,null,"sequence");
        List<ChannelInfo> list = new ArrayList<>();
        while(cursor.moveToNext()){
            ChannelInfo channelInfo = new ChannelInfo();
            channelInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
            channelInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            channelInfo.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
            channelInfo.setType(cursor.getString(cursor.getColumnIndex("type")));
            channelInfo.setCountry(cursor.getString(cursor.getColumnIndex("country")));
            channelInfo.setSequence(cursor.getInt(cursor.getColumnIndex("sequence")));
            channelInfo.setStyle(cursor.getString(cursor.getColumnIndex("style")));
            channelInfo.setSequence1(cursor.getInt(cursor.getColumnIndex("sequence1")));
            list.add(channelInfo);
        }
        if(cursor!= null){
            cursor.close();
        }
        return list;
    }

    public List<ChannelInfo> queryByStyle(String style){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.CHANNEL_TABLE ,null,"style=?" , new String []{style} ,null,null,"name");
        List<ChannelInfo> list = new ArrayList<>();
        while(cursor.moveToNext()){
            ChannelInfo channelInfo = new ChannelInfo();
            channelInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
            channelInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            channelInfo.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
            channelInfo.setType(cursor.getString(cursor.getColumnIndex("type")));
            channelInfo.setCountry(cursor.getString(cursor.getColumnIndex("country")));
            channelInfo.setSequence(cursor.getInt(cursor.getColumnIndex("sequence")));
            channelInfo.setStyle(cursor.getString(cursor.getColumnIndex("style")));
            channelInfo.setSequence1(cursor.getInt(cursor.getColumnIndex("sequence1")));
            list.add(channelInfo);
        }
        if(cursor!= null){
            cursor.close();
        }
        return list;
    }

}
