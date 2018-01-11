package com.px.common.http.configuration;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.px.common.http.pojo.DownloadInfo;
import com.px.common.utils.CommonApplication;

/**
 * Created by patrick on 04/07/2017.
 * create time : 2:20 PM
 */

public class DownloadDao {

    private SQLiteDatabase sqLiteDatabase;

    private static DownloadDao instance;
    private DownloadDao(){
        sqLiteDatabase = new SQLiteHelper(CommonApplication.context).getWritableDatabase();
    }

    public synchronized static DownloadDao getInstance(){
        if(instance == null){
            synchronized (DownloadDao.class){
                if(instance == null){
                    instance = new DownloadDao();
                }
            }
        }
        return instance;
    }

    public void insertOne(DownloadInfo downloadInfo){
        ContentValues values = new ContentValues();
        values.put("name", downloadInfo.getName());
        values.put("url", downloadInfo.getUrl());
        values.put("path", downloadInfo.getPath());
        values.put("message", downloadInfo.getMessage());
        values.put("status", downloadInfo.getStatus());
        values.put("progress", downloadInfo.getProgress());
        values.put("length", downloadInfo.getLength());
        values.put("startPosition", downloadInfo.getStartPosition());
        values.put("finishedPosition", downloadInfo.getFinishedPosition());
        sqLiteDatabase.insert(SQLiteHelper.TABLE_DOWNLOAD, null, values);
    }

    public DownloadInfo queryOneByName(DownloadInfo downloadInfo){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_DOWNLOAD, null ,"name=?",
                new String[]{downloadInfo.getName()}, null, null, null);
        while (cursor.moveToNext()){
            downloadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            downloadInfo.setPath(cursor.getString(cursor.getColumnIndex("path")));
            downloadInfo.setMessage(cursor.getString(cursor.getColumnIndex("message")));
            downloadInfo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            downloadInfo.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
            downloadInfo.setLength(cursor.getLong(cursor.getColumnIndex("length")));
            downloadInfo.setStartPosition(cursor.getLong(cursor.getColumnIndex("startPosition")));
            downloadInfo.setFinishedPosition(cursor.getLong(cursor.getColumnIndex("finishedPosition")));
        }
        cursor.close();
        return downloadInfo;
    }
}
