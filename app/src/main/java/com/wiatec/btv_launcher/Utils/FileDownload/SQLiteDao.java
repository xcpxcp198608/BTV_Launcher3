package com.wiatec.btv_launcher.Utils.FileDownload;

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
        if(isExists(downloadInfo.getFileName())){
            updateData(downloadInfo);
        }else{
            insertData(downloadInfo);
        }
    }

    public void insertData(DownloadInfo downloadInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadId" ,downloadInfo.getDownloadId());
        contentValues.put("fileName" ,downloadInfo.getFileName());
        contentValues.put("downloadUrl" ,downloadInfo.getDownloadUrl());
        contentValues.put("fileLength" ,downloadInfo.getFileLength());
        contentValues.put("startPosition" ,downloadInfo.getStartPosition());
        contentValues.put("stopPosition" ,downloadInfo.getStopPosition());
        contentValues.put("completePosition" ,downloadInfo.getCompletePosition());
        //Log.d("----px----" ,contentValues.toString());
        sqLiteDatabase.insert(SQLiteHelper.TABLE_NAME ,null , contentValues);
        if(contentValues!=null){
            contentValues = null;
        }
    }

    public void updateData(DownloadInfo downloadInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put("downloadId" ,downloadInfo.getDownloadId());
        contentValues.put("fileName" ,downloadInfo.getFileName());
        contentValues.put("downloadUrl" ,downloadInfo.getDownloadUrl());
        contentValues.put("fileLength" ,downloadInfo.getFileLength());
        contentValues.put("startPosition" ,downloadInfo.getStartPosition());
        contentValues.put("stopPosition" ,downloadInfo.getStopPosition());
        contentValues.put("completePosition" ,downloadInfo.getCompletePosition());
        //Log.d("----px----" ,contentValues.toString());
        sqLiteDatabase.update(SQLiteHelper.TABLE_NAME ,contentValues ,"fileName=?" ,new String []{downloadInfo.getFileName()});
        if(contentValues!=null){
            contentValues = null;
        }
    }

    public boolean isExists (String fileName){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME ,null ,"fileName=?" , new String []{fileName} ,null ,null ,null);
        boolean isExists = cursor.moveToNext();
        //Log.d("----px----" ,isExists+"");
        if(cursor!= null){
            cursor.close();
        }
        return isExists;
    }

    public void deleteDataByFileName(String fileName){
        sqLiteDatabase.delete(SQLiteHelper.TABLE_NAME ,"fileName=?" , new String []{fileName});
    }

    public void deleteAllData(){
        sqLiteDatabase.delete(SQLiteHelper.TABLE_NAME ,"_id>?" , new String []{"0"});
    }

    public List<DownloadInfo> queryData(){
        List<DownloadInfo> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME ,null ,"_id>?" , new String []{"0"} ,null ,null ,null);
        while(cursor.moveToNext()){
            DownloadInfo downloadInfo = new DownloadInfo();
            downloadInfo.setDownloadId(cursor.getInt(cursor.getColumnIndex("downloadId")));
            downloadInfo.setFileName(cursor.getString(cursor.getColumnIndex("fileName")));
            downloadInfo.setDownloadUrl(cursor.getString(cursor.getColumnIndex("downloadUrl")));
            downloadInfo.setFileLength(cursor.getLong(cursor.getColumnIndex("fileLength")));
            downloadInfo.setStartPosition(cursor.getLong(cursor.getColumnIndex("startPosition")));
            downloadInfo.setStopPosition(cursor.getLong(cursor.getColumnIndex("stopPosition")));
            downloadInfo.setCompletePosition(cursor.getLong(cursor.getColumnIndex("completePosition")));
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

    public DownloadInfo queryDataByFileName(String fileName){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME ,null ,"fileName=?" , new String []{fileName} ,null ,null ,null);
        DownloadInfo downloadInfo = new DownloadInfo();
        while(cursor.moveToNext()){
            downloadInfo.setDownloadId(cursor.getInt(cursor.getColumnIndex("downloadId")));
            downloadInfo.setFileName(cursor.getString(cursor.getColumnIndex("fileName")));
            downloadInfo.setDownloadUrl(cursor.getString(cursor.getColumnIndex("downloadUrl")));
            downloadInfo.setFileLength(cursor.getLong(cursor.getColumnIndex("fileLength")));
            downloadInfo.setStartPosition(cursor.getLong(cursor.getColumnIndex("startPosition")));
            downloadInfo.setStopPosition(cursor.getLong(cursor.getColumnIndex("stopPosition")));
            downloadInfo.setCompletePosition(cursor.getLong(cursor.getColumnIndex("completePosition")));
        }
        //Log.d("----px----" ,downloadInfo.toString());
        if(cursor!= null){
            cursor.close();
        }
        return downloadInfo;
    }
}
