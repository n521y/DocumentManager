package com.example.app.documentmanager.utils;

import android.content.ContentValues;
import android.content.Context;
import android.provider.MediaStore;

public  class MediaStoreCRUD {

    private static Context mContext;

    public MediaStoreCRUD(Context context) {
        mContext=context;
    }
    public  static void deleteItemFromMediaStoreImages(String itemPath){
        mContext.getContentResolver().delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"_data = ?",new String[] {itemPath});

    }

    public  static void renameItemFromMediaStoreImages(String itemPath,String newName){
        ContentValues values = new ContentValues();
        String newPath = itemPath.substring(0,
                itemPath.lastIndexOf("/") + 1)+newName;
        values.put("_display_name",newName);
        values.put("_data",newPath);
        mContext.getContentResolver().update(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values,"_data = ?",new String[] {itemPath});

    }

    public  static  void deleteItemFromMediaStoreVideos(String itemPath){
        mContext.getContentResolver().delete(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,"_data = ?",new String[] {itemPath});

    }

    public  static void renameItemFromMediaStoreVideos(String itemPath,String newName){
        ContentValues values = new ContentValues();
        String newPath = itemPath.substring(0,
                itemPath.lastIndexOf("/") + 1)+newName;
        values.put("_display_name",newName);
        values.put("_data",newPath);
        mContext.getContentResolver().update(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,values,"_data = ?",new String[] {itemPath});

    }

    public  static  void deleteItemFromMediaStoreAudios(String itemPath){
        mContext.getContentResolver().delete(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,"_data = ?",new String[] {itemPath});

    }

    public  static void renameItemFromMediaStoreAudios(String itemPath,String newName){
        ContentValues values = new ContentValues();
        String newPath = itemPath.substring(0,
                itemPath.lastIndexOf("/") + 1)+newName;
        values.put("_display_name",newName);
        values.put("_data",newPath);
        mContext.getContentResolver().update(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,values,"_data = ?",new String[] {itemPath});

    }



}
