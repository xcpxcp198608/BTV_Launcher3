package com.wiatec.btv_launcher.SQL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.CloudImageInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PX on 2016-12-02.
 */

public class CloudImageDao {

    private SQLiteDatabase sqLiteDatabase;

    private CloudImageDao() {
        sqLiteDatabase = new SQLiteHelper(Application.getContext()).getWritableDatabase();
    }
    private static volatile CloudImageDao instance;
    public static synchronized CloudImageDao getInstance (){
        if(instance == null){
            synchronized (CloudImageDao.class){
                instance = new CloudImageDao();
            }
        }
        return instance;
    }

    public boolean isExists (CloudImageInfo imageInfo){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.CLOUD_IMAGE_TABLE ,null , "name=?" ,new String [] { imageInfo.getName()} , null, null, null);
        boolean flag = cursor.moveToNext();
        if(cursor != null){
            cursor.close();
        }
        return flag;
    }

    public void insert (CloudImageInfo imageInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name" , imageInfo.getName());
        contentValues.put("url",imageInfo.getUrl());
        contentValues.put("finished" , imageInfo.getFinished());
        contentValues.put("path" , imageInfo.getPath());
        sqLiteDatabase.insert(SQLiteHelper.CLOUD_IMAGE_TABLE ,null , contentValues);
    }

    public void update (CloudImageInfo imageInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("url" ,imageInfo.getUrl());
        contentValues.put("finished" , imageInfo.getFinished());
        contentValues.put("path" , imageInfo.getPath());
        sqLiteDatabase.update(SQLiteHelper.CLOUD_IMAGE_TABLE ,contentValues , "name=?" ,new String []{imageInfo.getName()});
    }

    public void deleteAll () {
        sqLiteDatabase.delete(SQLiteHelper.CLOUD_IMAGE_TABLE ,"_id>?" ,new String[]{"0"});
    }

    public void insertOrUpdate (CloudImageInfo imageInfo){
        if(isExists(imageInfo)){
            update(imageInfo);
            Logger.d("update");
        }else {
            insert(imageInfo);
            Logger.d("insert");
        }
    }

    public List<CloudImageInfo> query (){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.CLOUD_IMAGE_TABLE ,null , "_id>?" ,new String [] { "0"} , null, null, null);
        List<CloudImageInfo> list = new ArrayList<>();
        while (cursor.moveToNext()){
            CloudImageInfo imageInfo = new CloudImageInfo();
            imageInfo.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            imageInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
            imageInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            imageInfo.setPath(cursor.getString(cursor.getColumnIndex("path")));
            imageInfo.setFinished(cursor.getString(cursor.getColumnIndex("finished")));
            list.add(imageInfo);
        }
        if(cursor!=null){
            cursor.close();
        }
        return list;
    }

}
