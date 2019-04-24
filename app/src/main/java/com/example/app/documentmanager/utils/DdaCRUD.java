package com.example.app.documentmanager.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DdaCRUD {
    private static Context mContext;
    private static SQLiteDatabase mDb;

    public DdaCRUD(Context context, SQLiteDatabase db) {
        mContext=context;
        mDb=db;

    }


    public static List<String> queryAPK(){
        List<String> dataPathList = new ArrayList<>();
        Cursor cursor = mDb.query("Apk",null,null,null,null,null,null,null);
        Log.d("MainActivity", "cursor name is " + cursor.toString());
        Log.d("MainActivity", "cursor name is " + cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            do {
// 遍历Cursor对象，取出数据并打印
                dataPathList.add(cursor.getString(cursor.getColumnIndex
                        ("Apk_uri")));
                String id = cursor.getString(cursor.getColumnIndex
                        ("id"));
                Log.d("MainActivity", "Apkid :" + id);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dataPathList;

    }

    public static void deleteApk(String itemPath){
        mDb.delete("Apk","Apk_uri = ?",new String[]{itemPath});

    }

    public static void renameApk(String itemPath,String newName){
        ContentValues values = new ContentValues();
        String newPath = itemPath.substring(0,
                itemPath.lastIndexOf("/") + 1)+newName;
        values.put("Apk_uri",newPath);
        mDb.update("Apk",values,"Apk_uri = ?",new String[]{itemPath});

    }



    public static  List<String>  queryDocument(){
        List<String> dataPathList = new ArrayList<>();
        Cursor cursor = mDb.query("Document",null,null,null,null,null,null,null);
        Log.d("MainActivity", "Document name is " + cursor.toString());
        Log.d("MainActivity", "Document name is " + cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            do {
// 遍历Cursor对象，取出数据并打印
                dataPathList.add(cursor.getString(cursor.getColumnIndex
                        ("Document_Uri")));
                String id = cursor.getString(cursor.getColumnIndex
                        ("id"));
                Log.d("MainActivity", "Documentid :" + id);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dataPathList;
    }

    public static void deleteDocument(String itemPath){
        mDb.delete("Document","Document_Uri = ?",new String[]{itemPath});

    }

    public static void renameDocument(String itemPath,String newName){
        ContentValues values = new ContentValues();
        String newPath = itemPath.substring(0,
                itemPath.lastIndexOf("/") + 1)+newName;
        values.put("Document_Uri",newPath);
        mDb.update("Document",values,"Document_Uri = ?",new String[]{itemPath});

    }


    public static  List<String>  queryDownLoad(){
        List<String> dataPathList = new ArrayList<>();
        Cursor cursor = mDb.query("DownLoad",null,null,null,null,null,null,null);
        Log.d("MainActivity", "DownLoad name is " + cursor.toString());
        Log.d("MainActivity", "DownLoad name is " + cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            do {
// 遍历Cursor对象，取出数据并打印
                dataPathList.add(cursor.getString(cursor.getColumnIndex
                        ("DownLoad_Uri")));
                String id = cursor.getString(cursor.getColumnIndex
                        ("id"));
                Log.d("MainActivity", "DownLoadid :" + id);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dataPathList;
    }

    public static void deleteDownLoad(String itemPath){
        mDb.delete("DownLoad","DownLoad_Uri = ?",new String[]{itemPath});

    }

    public static void renameDownLoad(String itemPath,String newName){
        ContentValues values = new ContentValues();
        String newPath = itemPath.substring(0,
                itemPath.lastIndexOf("/") + 1)+newName;
        values.put("DownLoad_Uri",newPath);
        mDb.update("DownLoad",values,"DownLoad_Uri = ?",new String[]{itemPath});

    }





}
