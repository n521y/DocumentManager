package com.example.app.documentmanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.app.documentmanager.custom_view.DrawTextImageView;
import com.example.app.documentmanager.sql.MyDatabaseHelper;
import com.example.app.documentmanager.utils.FileCategoryHelper;
import com.example.app.documentmanager.utils.FileOpen;
import com.example.app.documentmanager.utils.FileRecent;
import com.example.app.documentmanager.utils.RecentInformationCall;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean refreshFlag = true;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private DrawTextImageView imageView4;
    private MyDatabaseHelper myDatabaseHelper;
    private LinearLayout recentLayout;
    private SQLiteDatabase mDb;
    private LinearLayout imageLayout;
    private LinearLayout audioLayout;
    private LinearLayout videoLayout;
    private LinearLayout documentLayout;
    private LinearLayout downloadLayout;
    private LinearLayout apkLayout;
    private LinearLayout phonestorage;
    private Toolbar toolbar;
    private List<String> mRecentImageListPath;
    private RecentInformationCall mRecentInformationCall;
    private FileOpen mFileOpen;
    class MyAsyncTask extends AsyncTask<Void,Void,Boolean>{


        @Override
        protected Boolean doInBackground(Void... voids) {
              initDataBae();
            //queryAPK();
            //queryDocument();
            //queryDownLoad();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Toast.makeText(mContext,"查询数据结束",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private MyAsyncTask myAsyncTask = new MyAsyncTask();


    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabaseHelper = new MyDatabaseHelper(this, "Wenjian.db", null, 2);
        mDb=myDatabaseHelper.getWritableDatabase();
        mContext = getApplicationContext();
        mFileOpen =new FileOpen(mContext);
        init();
        mRecentImageListPath = mRecentInformationCall.getRecentOneMonthImage(mContext);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("文件管理器");
        if(mRecentImageListPath.size() > 0){
            recentLayout.setVisibility(View.VISIBLE);
            List<Bitmap> bitmaplist = new ArrayList<>();
            int size = mRecentImageListPath.size() - 4;
            Log.d("bitmaplist", " "+size);
            for(int i =0;i<4;i++){
                Bitmap bitmap = BitmapFactory.decodeFile(mRecentImageListPath.get(i));
                bitmaplist.add(bitmap);
            }
            imageView1.setImageBitmap(bitmaplist.get(0));
            imageView2.setImageBitmap(bitmaplist.get(1));
            imageView3.setImageBitmap(bitmaplist.get(2));
            imageView4.setImageBitmap(bitmaplist.get(3));
            imageView4.setDrawText("+"+String.valueOf(size));
        }else {
            recentLayout.setVisibility(View.GONE);
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if(menuItemId == R.id.action_serch){
                    Toast.makeText(MainActivity.this , "serch" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                    startActivity(intent);

                }else if (menuItemId == R.id.action_settings){
                    Toast.makeText(MainActivity.this , "setting" , Toast.LENGTH_SHORT).show();
                }else if(menuItemId == R.id.action_refresh){
                    Toast.makeText(MainActivity.this , "action_refresh" , Toast.LENGTH_SHORT).show();

                    delete();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            initDataBae();

                        }
                    }).start();

                }
                return false;
            }
        });
        //如果数据库不存在数据，则加载数据
        if(isCreateDB()){
            Toast.makeText(MainActivity.this , "isCreateDB" , Toast.LENGTH_SHORT).show();
            myAsyncTask.execute();
        }

    }


    private  void initDataBae(){
        FileCategoryHelper helper = new FileCategoryHelper();
        helper.getSystemFileCategory("/storage/emulated/0",mContext);
        Log.d("MainActivity", "Documentvalues "+helper.getSystemDocument().size() );
        Log.d("MainActivity", "getSystemCompression "+helper.getSystemCompression().size() );
        Log.d("MainActivity", "getSystemApk "+helper.getSystemApk().size() );

        ContentValues Documentvalues = new ContentValues();
        for(int i=0;i<helper.getSystemDocument().size();i++){
            Documentvalues.put("Document_Uri",helper.getSystemDocument().get(i));
            Documentvalues.put("Document_name",helper.getmDocumentNameList().get(i));
            Log.d("MainActivity", "Documentvalues "+helper.getSystemDocument().get(i) );
            mDb.insert("Document",null,Documentvalues);
            Documentvalues.clear();
        }

        ContentValues DownLoadvalues = new ContentValues();
        for(int i=0;i<helper.getSystemCompression().size();i++){
            Log.d("MainActivity", "DownLoadvalues "+ helper.getSystemCompression().get(i));
            DownLoadvalues.put("DownLoad_Uri",helper.getSystemCompression().get(i));
            DownLoadvalues.put("DownLoad_name",helper.getmCompressionNameList().get(i));
            mDb.insert("DownLoad",null,DownLoadvalues);
            DownLoadvalues.clear();
        }

        ContentValues Apkdvalues = new ContentValues();
        for(int i=0;i<helper.getSystemApk().size();i++){
            Log.d("MainActivity", "Apkdvalues "+helper.getSystemApk().get(i) );
            Apkdvalues.put("Apk_uri",helper.getSystemApk().get(i));
            Log.d("Apkdvalues", "Apkdvalues "+helper.getmApkNameList().get(i) );
            Apkdvalues.put("Apk_name",helper.getmApkNameList().get(i));
            mDb.insert("Apk",null,Apkdvalues);
            Apkdvalues.clear();
        }



    }

    private void  delete(){
        mDb.delete("Document","id > ?",new String[] { "0" });
        mDb.delete("Apk","id > ?",new String[] { "0" });
        mDb.delete("DownLoad","id > ?",new String[] { "0" });
        Toast.makeText(mContext,"删除数据结束",Toast.LENGTH_SHORT).show();


    }

    private  void  queryAPK(){
        Cursor cursor = mDb.query("Apk",null,null,null,null,null,null,null);
        Log.d("MainActivity", "cursor name is " + cursor.toString());
        Log.d("MainActivity", "cursor name is " + cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            do {
               // 遍历Cursor对象，取出数据并打印
                String uri = cursor.getString(cursor.getColumnIndex
                        ("Apk_uri"));
                String id = cursor.getString(cursor.getColumnIndex
                        ("id"));
                Log.d("MainActivity", "Apkid :" + id);
                Log.d("MainActivity", "Apk_uri " + uri);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }
    //判断当前数据库中是否有值
    private boolean isCreateDB() {
        Cursor cursor = mDb.query("Document", null, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            return false;
        } else {
            return true;
        }
    }

    private  void  queryDocument(){
        Cursor cursor = mDb.query("Document",null,null,null,null,null,null,null);
        Log.d("MainActivity", "Document name is " + cursor.toString());
        Log.d("MainActivity", "Document name is " + cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            do {
// 遍历Cursor对象，取出数据并打印
                String uri = cursor.getString(cursor.getColumnIndex
                        ("Document_Uri"));
                String id = cursor.getString(cursor.getColumnIndex
                        ("id"));
                Log.d("MainActivity", "Documentid :" + id);
                Log.d("MainActivity", "Document_uri " + uri);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    private  void  queryDownLoad(){
        Cursor cursor = mDb.query("DownLoad",null,null,null,null,null,null,null);
        Log.d("MainActivity", "DownLoad name is " + cursor.toString());
        Log.d("MainActivity", "DownLoad name is " + cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            do {
// 遍历Cursor对象，取出数据并打印
                String uri = cursor.getString(cursor.getColumnIndex
                        ("DownLoad_Uri"));
                String name = cursor.getString(cursor.getColumnIndex
                        ("DownLoad_name"));
                Log.d("MainActivityniuniu", "DownLoadname :" + name);
                Log.d("MainActivityniuniu", "DownLoad_uri: " + uri);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }


    private  void  init(){
        mRecentInformationCall = new FileRecent();
        imageView1 =(ImageView)findViewById(R.id.id_recent_image1);
        imageView2 =(ImageView)findViewById(R.id.id_recent_image2);
        imageView3 =(ImageView)findViewById(R.id.id_recent_image3);
        imageView4 = (DrawTextImageView)findViewById(R.id.more_picture);
        mRecentImageListPath = new ArrayList<>();
        imageView4.setDrawLocalXY(80,150);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        recentLayout = (LinearLayout)findViewById(R.id.id_recent_Layout);
        imageLayout = (LinearLayout)findViewById(R.id.image);
        audioLayout = (LinearLayout)findViewById(R.id.audio);
        videoLayout = (LinearLayout)findViewById(R.id.video);
        documentLayout = (LinearLayout)findViewById(R.id.document);
        downloadLayout = (LinearLayout)findViewById(R.id.download);
        apkLayout = (LinearLayout)findViewById(R.id.apk);
        phonestorage = (LinearLayout)findViewById(R.id.phonestorage);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        imageLayout.setOnClickListener(this);
        audioLayout.setOnClickListener(this);
        videoLayout.setOnClickListener(this);
        documentLayout.setOnClickListener(this);
        downloadLayout.setOnClickListener(this);
        apkLayout.setOnClickListener(this);
        phonestorage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent =new Intent(MainActivity.this,CommonActivity.class);
        switch (v.getId()){
            case R.id.id_recent_image1:
                Intent imagei1Intent =mFileOpen.openFile(mRecentImageListPath.get(0));
                startActivity(imagei1Intent);

                break;
            case R.id.id_recent_image2:
                Intent imagei2Intent =mFileOpen.openFile(mRecentImageListPath.get(1));
                startActivity(imagei2Intent);

                break;
            case R.id.id_recent_image3:
                Intent imagei3Intent =mFileOpen.openFile(mRecentImageListPath.get(2));
                startActivity(imagei3Intent);
                break;
            case R.id.more_picture:
                Intent recentIntent = new Intent(MainActivity.this,RecentActivity.class);
                startActivity(recentIntent);
                break;

            case R.id.image:
                intent.setType("image");
                startActivity(intent);
                break;
            case R.id.audio:
                intent.setType("audio");
                startActivity(intent);
                break;
            case R.id.video:
                intent.setType("video");
                startActivity(intent);
                break;
            case R.id.document:
                intent.setType("document");
                startActivity(intent);
                break;
            case R.id.download:
                intent.setType("download");
                startActivity(intent);
                break;
            case R.id.apk:
                intent.setType("apk");
                startActivity(intent);
                break;
            case R.id.phonestorage:
                Intent phonestorageIntent = new Intent(MainActivity.this,FileListActivity.class);
                startActivity(phonestorageIntent);
                Toast.makeText(v.getContext(),"phonestorage",Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(v.getContext(),"default",Toast.LENGTH_LONG).show();
            break;


        }
    }
}
