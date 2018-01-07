package com.wiatec.btv_launcher.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.Application;

/**
 * Created by patrick on 27/05/2017.
 * create time : 9:37 AM
 */

public class UserContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.wiatec.btv_launcher.provide.UserContentProvider";
    public static final String PATH = "user/*";
    public static final int MATCH_CODE = 1;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, PATH, MATCH_CODE);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        if(uriMatcher.match(uri) != MATCH_CODE) return "";
        String[] path = uri.getPath().split("/");
        String key = path[2];
        return (String) SPUtil.get(key, "");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
