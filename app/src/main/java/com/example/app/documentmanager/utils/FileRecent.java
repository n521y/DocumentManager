package com.example.app.documentmanager.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import java.util.ArrayList;
import java.util.List;

public class FileRecent {

    private final static int ONE_DAY = 1;
    private final static int ONE_WEEK = 7;
    private final static int ONE_MONTH = 30;
    private final static int ONE_YEAR = 365;

    public static boolean recentOneDay(long fileTime, long currentTime) {
        if (((currentTime / 1000) - fileTime) / 86400 <= ONE_DAY) {
            return true;
        }
        return false;
    }

    public static boolean recentOneWeek(long fileTime, long currentTime) {
        if (((currentTime / 1000) - fileTime) / 86400 <= ONE_WEEK) {
            return true;
        }
        return false;
    }

    public static boolean recentOneMonth(long fileTime, long currentTime) {
        if (((currentTime / 1000) - fileTime) / 86400 <= ONE_MONTH) {
            return true;
        }
        return false;
    }

    public static boolean recentOneYear(long fileTime, long currentTime) {
        if (((currentTime / 1000) - fileTime) / 86400 <= ONE_YEAR) {
            return true;
        }
        return false;
    }

    public static List<String> getMainDisplayImage(Context context) {
        List<String> fileList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Images.Media.DATE_MODIFIED +"DESC");
        while (cursor.moveToNext() && fileList.size() < 5) {
            String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            fileList.add(filePath);
        }
        if(fileList.size() != 4) {
            fileList = null;
        }
        cursor.close();
        return fileList;
    }

    public static List<String> getRecentSystemImage(Context context, int recentTime) {
        List<String> fileList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                null, null);

        while (cursor.moveToNext()) {
            long fileTime = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
            long currentTime = System.currentTimeMillis();
            switch (recentTime) {
                case ONE_DAY:
                    if (recentOneDay(fileTime, currentTime)) {
                        String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        fileList.add(filePath);
                    }
                    break;
                case ONE_WEEK:
                    if (recentOneWeek(fileTime, currentTime)) {
                        String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        fileList.add(filePath);
                    }
                    break;
                case ONE_MONTH:
                    if (recentOneMonth(fileTime, currentTime)) {
                        String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        fileList.add(filePath);
                    }
                    break;
                case ONE_YEAR:
                    if (recentOneYear(fileTime, currentTime)) {
                        String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        fileList.add(filePath);
                    }
                    break;
            }
        }
        cursor.close();
        return fileList;
    }

    public static List<Bitmap> pathChange(Context context, List<String> pathList) {
        List<Bitmap> dataList = new ArrayList();
        if (pathList.size() != 0) {
            for (String filePath : pathList) {
                //File file = new File(filePath);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                dataList.add(bitmap);
            }
        }
        return dataList;
    }
}

