package com.px.common.http.configuration;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HttpMaster";
    public static final String TABLE_DOWNLOAD = "Download";
    private static final String CREATE_DOWNLOAD = "create table if not exists "+TABLE_DOWNLOAD+
            "(_id integer primary key autoincrement, name text, url text, path text, " +
            "message text, status integer, progress integer, length integer, " +
            "startedPosition integer, finishedPosition integer)";
    private static final String DROP_DOWNLOAD = "drop table is exists "+TABLE_DOWNLOAD;
    private static final int VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DOWNLOAD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_DOWNLOAD);
        onCreate(db);
    }
}
