package com.example.app.documentmanager.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    public static final String CREATE_DOCUMNET = "create table Document ("
            + "id integer primary key autoincrement, "
            + "Document_Uri text)";
    public static final String CREATE_DOWNLOAD = "create table DownLoad ("
            + "id integer primary key autoincrement, "
            + "DownLoad_Uri text)";

    public static final String CREATE_APK= "create table Apk ("
            + "id integer primary key autoincrement, "
            + "Apk_uri text)";



    public MyDatabaseHelper(Context context,  String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DOCUMNET);
        db.execSQL(CREATE_DOWNLOAD);
        db.execSQL(CREATE_APK);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
