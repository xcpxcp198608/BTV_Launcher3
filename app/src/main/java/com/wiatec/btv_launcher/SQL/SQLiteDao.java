package com.wiatec.btv_launcher.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wiatec.btv_launcher.bean.InstalledApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PX on 2016-10-14.
 */

public class SQLiteDao {

    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteDao (Context context){
        sqLiteDatabase = new SQLiteHelper(context).getWritableDatabase();
    }
    private static SQLiteDao instance;
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

    public boolean isExists(InstalledApp installedApp){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME , null , "appPackageName=?",
                new String []{installedApp.getAppPackageName()} ,null ,null ,null);
        boolean isExists = cursor.moveToNext();
        if(cursor != null){
            cursor.close();
        }
        return isExists;
    }

    public void insertData(InstalledApp installedApp ,String type){
        ContentValues contentValues = new ContentValues();
        contentValues.put("type",type);
        contentValues.put("appName",installedApp.getAppName());
        contentValues.put("appPackageName" ,installedApp.getAppPackageName());
        contentValues.put("sequence",installedApp.getSequence());
        sqLiteDatabase.insert(SQLiteHelper.TABLE_NAME ,null , contentValues);
    }

    public void updateData(InstalledApp installedApp ,String type){
        ContentValues contentValues = new ContentValues();
        contentValues.put("type",type);
        contentValues.put("sequence",installedApp.getSequence());
        sqLiteDatabase.update(SQLiteHelper.TABLE_NAME , contentValues,"appPackageName=?" ,new String []{installedApp.getAppPackageName()});
    }

    public void insertOrUpdateData(InstalledApp installedApp ,String type){
        if(isExists(installedApp)){
            updateData(installedApp ,type);
        }else{
            insertData(installedApp ,type);
        }
    }

    public List<InstalledApp> queryDataByType(String type){
        List<InstalledApp> list =new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME , null , "type=?",
                new String []{type} ,null ,null ,"sequence");
        while(cursor.moveToNext()){
            InstalledApp installedApp = new InstalledApp();
            installedApp.setType(cursor.getString(cursor.getColumnIndex("type")));
            installedApp.setAppName(cursor.getString(cursor.getColumnIndex("appName")));
            installedApp.setAppPackageName(cursor.getString(cursor.getColumnIndex("appPackageName")));
            installedApp.setSequence(cursor.getInt(cursor.getColumnIndex("sequence")));
            list.add(installedApp);
            if(installedApp !=null){
                installedApp = null;
            }
        }
        if(cursor != null){
            cursor.close();
        }
        return list;
    }

    public List<InstalledApp> queryData(){
        List<InstalledApp> list =new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.TABLE_NAME , null , "_id>?",
                new String []{"0"} ,null ,null ,"sequence");
        while(cursor.moveToNext()){
            InstalledApp installedApp = new InstalledApp();
            installedApp.setType(cursor.getString(cursor.getColumnIndex("type")));
            installedApp.setAppName(cursor.getString(cursor.getColumnIndex("appName")));
            installedApp.setAppPackageName(cursor.getString(cursor.getColumnIndex("appPackageName")));
            installedApp.setSequence(cursor.getInt(cursor.getColumnIndex("sequence")));
            list.add(installedApp);
            if(installedApp !=null){
                installedApp = null;
            }
        }
        if(cursor != null){
            cursor.close();
        }
        return list;
    }

    public void deleteByPackageName(String packageName){
        sqLiteDatabase.delete(SQLiteHelper.TABLE_NAME ,"appPackageName=?" ,new String []{packageName});
    }

    public void deleteAll (){
        sqLiteDatabase.delete(SQLiteHelper.TABLE_NAME ,"_id>?" ,new String []{"0"});
    }
}
