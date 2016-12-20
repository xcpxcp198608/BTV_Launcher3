package com.wiatec.btv_launcher.Utils.ImageDownload;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PX on 2016/9/12.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="ImageDownloadInfo.db";
    public static final String TABLE_NAME ="SingleThreadDownloadInfo";
    private static final String CREATE_TABLE ="create table if not exists "+TABLE_NAME+"(_id integer primary key autoincrement" +
            ",status integer,progress integer,name text,url text,path text,length integer,startPosition integer,endPosition integer" +
            ",finishedPosition integer)";
    private static final String DROP_TABLE ="drop table if exists "+TABLE_NAME;
    private static final int VERSION =1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
