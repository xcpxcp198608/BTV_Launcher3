package com.wiatec.btv_launcher.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.px.common.constant.CommonApplication;
import com.wiatec.btv_launcher.bean.ChannelInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 2016/12/28.
 */

public class ChannelDao {
    private SQLiteDatabase sqLiteDatabase;

    private ChannelDao(){
        sqLiteDatabase = new SQLiteHelper(CommonApplication.context).getWritableDatabase();
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
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.CHANNEL_TABLE ,null , "tag=?" , new String[]{channelInfo.getTag()} ,null,null,null);
        boolean flag = cursor.moveToNext();
        if(cursor != null){
            cursor.close();
        }
        return flag;
    }

    public void insert(ChannelInfo channelInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("sequence",channelInfo.getSequence());
        contentValues.put("tag",channelInfo.getTag());
        contentValues.put("name",channelInfo.getName());
        contentValues.put("url",channelInfo.getUrl());
        contentValues.put("icon",channelInfo.getIcon());
        contentValues.put("type",channelInfo.getType());
        contentValues.put("country",channelInfo.getCountry());
        contentValues.put("style",channelInfo.getStyle());
        contentValues.put("visible",channelInfo.getVisible());
        sqLiteDatabase.insert(SQLiteHelper.CHANNEL_TABLE ,null ,contentValues);
    }

    public void update (ChannelInfo channelInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("sequence",channelInfo.getSequence());
        contentValues.put("tag",channelInfo.getTag());
        contentValues.put("name",channelInfo.getName());
        contentValues.put("url",channelInfo.getUrl());
        contentValues.put("icon",channelInfo.getIcon());
        contentValues.put("type",channelInfo.getType());
        contentValues.put("country",channelInfo.getCountry());
        contentValues.put("style",channelInfo.getStyle());
        contentValues.put("visible",channelInfo.getVisible());
        sqLiteDatabase.update(SQLiteHelper.CHANNEL_TABLE ,contentValues , "tag=?" , new String []{channelInfo.getTag()});
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

    public List<ChannelInfo> query (String selection , String where ,String order){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.CHANNEL_TABLE ,null,selection+"=? and visible=?" , new String []{where , "1"} ,null,null,order);
        List<ChannelInfo> list = new ArrayList<>();
        while(cursor.moveToNext()){
            ChannelInfo channelInfo = new ChannelInfo();
            channelInfo.setSequence(cursor.getInt(cursor.getColumnIndex("sequence")));
            channelInfo.setTag(cursor.getString(cursor.getColumnIndex("tag")));
            channelInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
            channelInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            channelInfo.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
            channelInfo.setType(cursor.getString(cursor.getColumnIndex("type")));
            channelInfo.setCountry(cursor.getString(cursor.getColumnIndex("country")));
            channelInfo.setStyle(cursor.getString(cursor.getColumnIndex("style")));
            channelInfo.setVisible(cursor.getInt(cursor.getColumnIndex("visible")));
            list.add(channelInfo);
        }
        if(cursor!= null){
            cursor.close();
        }
        return list;
    }

}
