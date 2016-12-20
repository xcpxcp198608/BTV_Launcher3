package com.wiatec.btv_launcher.Utils.ImageDownload;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PX on 2016/9/12.
 */
public class SQLiteDao {
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

    private SQLiteDao(Context context) {
        this.context = context;
        sqLiteDatabase = new SQLiteHelper(context).getWritableDatabase();
    }

    private volatile static SQLiteDao instance;
    public static synchronized SQLiteDao getInstance(Context context){
        if(instance ==null){
            synchronized (SQLiteDao.class){
                if(instance ==null){
                    instance = new SQLiteDao(context);
                }
            }
        }
        return instance;
    }

    public void insertOrUpdateData(DownloadInfo downloadInfo){
        if(isExists(downloadInfo)){
            updateData(downloadInfo);
        }else{
            insertData(downloadInfo);
        }
    }

    public void insertData(DownloadInfo downloadInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("status" ,downloadInfo.getStatus());
        contentValues.put("progress" ,downloadInfo.getProgress());
        contentValues.put("name" ,downloadInfo.getName());
        contentValues.put("url" ,downloadInfo.getUrl());
        contentValues.put("path" ,downloadInfo.getPath());
        contentValues.put("length" ,downloadInfo.getLength());
        contentValues.put("startPosition" ,downloadInfo.getStartPosition());
        contentValues.put("endPosition" ,downloadInfo.getEndPosition());
        contentValues.put("finishedPosition" ,downloadInfo.getFinishedPosition());
        //Log.d("----px----" ,contentValues.toString());
        sqLiteDatabase.insert(SQLiteHelper.TABLE_NAME ,null , contentValues);
        if(contentValues!=null){
            contentValues = null;
        }
    }

    public void updateData(DownloadInfo downloadInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("status" ,downloadInfo.getStatus());
        contentValues.put("progress" ,downloadInfo.getProgress());
        contentValues.put("name" ,downloadInfo.getName());
        contentValues.put("url" ,downloadInfo.getUrl());
        contentValues.put("path" ,downloadInfo.getPath());
        contentValues.put("length" ,downloadInfo.getLength());
        contentValues.put("startPosition" ,downloadInfo.getStartPosition());
        contentValues.put("endPosition" ,downloadInfo.getEndPosition());
        contentValues.put("finishedPosition" ,downloadInfo.getFinishedPosition());
        //Log.d("----px----" ,contentValues.toString());
        sqLiteDatabase.update(SQLiteHelper.TABLE_NAME ,contentValues ,"name=?" ,new String []{downloadInfo.getName()});
        if(contentValues!=null){
            contentValues = null;
        }
    }

    public boolean isExists (DownloadInfo downloadInfo){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME ,null ,"name=?" , new String []{downloadInfo.getName()} ,null ,null ,null);
        boolean isExists = cursor.moveToNext();
        //Log.d("----px----" ,isExists+"");
        if(cursor!= null){
            cursor.close();
        }
        return isExists;
    }

    public void deleteDataByName(String name){
        sqLiteDatabase.delete(SQLiteHelper.TABLE_NAME ,"name=?" , new String []{name});
    }

    public void deleteAllData(){
        sqLiteDatabase.delete(SQLiteHelper.TABLE_NAME ,"_id>?" , new String []{"0"});
    }

    public List<DownloadInfo> queryData(){
        List<DownloadInfo> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME ,null ,"_id>?" , new String []{"0"} ,null ,null ,null);
        while(cursor.moveToNext()){
            DownloadInfo downloadInfo = new DownloadInfo();
            downloadInfo.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            downloadInfo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            downloadInfo.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
            downloadInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
            downloadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            downloadInfo.setPath(cursor.getString(cursor.getColumnIndex("path")));
            downloadInfo.setLength(cursor.getLong(cursor.getColumnIndex("length")));
            downloadInfo.setStartPosition(cursor.getLong(cursor.getColumnIndex("startPosition")));
            downloadInfo.setEndPosition(cursor.getLong(cursor.getColumnIndex("endPosition")));
            downloadInfo.setFinishedPosition(cursor.getLong(cursor.getColumnIndex("finishedPosition")));
            list.add(downloadInfo);
            if(downloadInfo!= null){
                downloadInfo=null;
            }
        }
        //Log.d("----px----" ,list.toString());
        if(cursor!= null){
            cursor.close();
        }
        return list;
    }

    public DownloadInfo queryDataByName(String name){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME ,null ,"name=?" , new String []{name} ,null ,null ,null);
        DownloadInfo downloadInfo = new DownloadInfo();
        while(cursor.moveToNext()){
            downloadInfo.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            downloadInfo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            downloadInfo.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
            downloadInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
            downloadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            downloadInfo.setPath(cursor.getString(cursor.getColumnIndex("path")));
            downloadInfo.setLength(cursor.getLong(cursor.getColumnIndex("length")));
            downloadInfo.setStartPosition(cursor.getLong(cursor.getColumnIndex("startPosition")));
            downloadInfo.setEndPosition(cursor.getLong(cursor.getColumnIndex("endPosition")));
            downloadInfo.setFinishedPosition(cursor.getLong(cursor.getColumnIndex("finishedPosition")));
        }
        //Log.d("----px----" ,downloadInfo.toString());
        if(cursor!= null){
            cursor.close();
        }
        return downloadInfo;
    }
}
