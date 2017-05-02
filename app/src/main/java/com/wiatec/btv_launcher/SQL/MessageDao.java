package com.wiatec.btv_launcher.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.MessageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PX on 2016-11-15.
 */

public class MessageDao {

    private SQLiteDatabase sqLiteDatabase;

    private MessageDao (Context context){
        sqLiteDatabase = new SQLiteHelper(context).getWritableDatabase();
    }

    private volatile static MessageDao instance;
    public static MessageDao getInstance(Context context){
        if(instance ==null){
            synchronized (MessageDao.class){
                if(instance ==null){
                    instance = new MessageDao(context);
                }
            }
        }
        return instance;
    }

    public boolean isExists (MessageInfo messageInfo){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.MESSAGE_TABLE ,null , "title=?" , new String []{messageInfo.getTitle()} ,null,null,null);
        boolean isExists = cursor.moveToNext();
        if(cursor!= null){
           cursor.close();
        }
        return isExists;
    }

    public boolean insertMessage (MessageInfo messageInfo){
        boolean flag = true;
        try{
            if(isExists(messageInfo)){
                flag = false;
            }else {
//                Logger.d("insert");
                ContentValues contentValues = new ContentValues();
                contentValues.put("title", messageInfo.getTitle());
                contentValues.put("content", messageInfo.getContent());
                contentValues.put("icon", messageInfo.getIcon());
                contentValues.put("link", messageInfo.getLink());
                contentValues.put("isRead", messageInfo.getIsRead());
                contentValues.put("type", messageInfo.getType());
                sqLiteDatabase.insert(SQLiteHelper.MESSAGE_TABLE, null, contentValues);
            }
        }catch (Exception e){
            e.printStackTrace();
            flag =false;
        }
        return flag;
    }

    public boolean updateMessage(MessageInfo messageInfo){
        boolean flag = true;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("content" ,messageInfo.getContent());
            contentValues.put("icon" ,messageInfo.getIcon());
            contentValues.put("link" ,messageInfo.getLink());
            contentValues.put("isRead" ,messageInfo.getIsRead());
            contentValues.put("type" ,messageInfo.getType());
            sqLiteDatabase.update(SQLiteHelper.MESSAGE_TABLE,contentValues,"title=?" ,new String[]{messageInfo.getTitle()});
        }catch (Exception e){
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public boolean insertOrUpdateMessage (MessageInfo messageInfo){
        if(isExists(messageInfo)){
            return updateMessage(messageInfo);
        }else {
            return insertMessage(messageInfo);
        }
    }

    public List<MessageInfo> queryAllMessage (){
        List<MessageInfo> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.MESSAGE_TABLE , null ,"_id>?" ,new String[]{"0"} , null,null,null);
        while (cursor.moveToNext()){
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            messageInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            messageInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
            messageInfo.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
            messageInfo.setLink(cursor.getString(cursor.getColumnIndex("link")));
            messageInfo.setIsRead(cursor.getString(cursor.getColumnIndex("isRead")));
            messageInfo.setType(cursor.getString(cursor.getColumnIndex("type")));
            list.add(messageInfo);
        }
        if(cursor !=null){
            cursor.close();
        }
        return list;
    }

    public List<MessageInfo> queryUnreadMessage (){
        List<MessageInfo> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.MESSAGE_TABLE , null ,"isRead=?" ,new String[]{"false"} , null,null,null);
        while (cursor.moveToNext()){
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            messageInfo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            messageInfo.setContent(cursor.getString(cursor.getColumnIndex("content")));
            messageInfo.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
            messageInfo.setLink(cursor.getString(cursor.getColumnIndex("link")));
            messageInfo.setIsRead(cursor.getString(cursor.getColumnIndex("isRead")));
            messageInfo.setType(cursor.getString(cursor.getColumnIndex("type")));
            list.add(messageInfo);
        }
        if(cursor !=null){
            cursor.close();
        }
        return list;
    }

    public boolean deleteMessage (MessageInfo messageInfo){
        boolean flag = true;
        try {
            sqLiteDatabase.delete(SQLiteHelper.MESSAGE_TABLE ,"title=?" , new String[]{messageInfo.getTitle()});
        }catch (Exception e){
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public boolean hasUnReadMessage (){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.MESSAGE_TABLE ,null , "isRead=?" , new String []{"false"} ,null,null,null);
        boolean isExists = cursor.moveToNext();
        if(cursor != null){
            cursor.close();
        }
        return isExists;
    }

    public void setAllRead (){
        ContentValues contentValues = new ContentValues();
        contentValues.put("isRead" ,"true");
        sqLiteDatabase.update(SQLiteHelper.MESSAGE_TABLE ,contentValues,"_id>?" ,new String []{"0"});
    }

}
