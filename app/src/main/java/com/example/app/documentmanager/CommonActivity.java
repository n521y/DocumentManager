package com.example.app.documentmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.documentmanager.adapter.FileListAdapter;
import com.example.app.documentmanager.bean.CommonBean;
import com.example.app.documentmanager.sql.MyDatabaseHelper;
import com.example.app.documentmanager.utils.FileCategoryHelper;
import com.example.app.documentmanager.utils.FileHelper;
import com.example.app.documentmanager.utils.FileOpen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//该Activity是作为所有分类文件的显示
public class CommonActivity extends AppCompatActivity {

    private FileCategoryHelper mFileCategoryHelper = new FileCategoryHelper();
    private Context mContext;
    private String mType;
    private FileListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Bundle mSavedInstanceState;
    private boolean arrangementFlag = false;
    private Toolbar mCommonToolbar;
    private TextView mTextView;
    private RecyclerView mListView;
    public List<CommonBean> data = new ArrayList() ;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase mDb;
    private FileOpen mFileOpen;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(CommonActivity.this, "what0", Toast.LENGTH_LONG).show();
                    onStart();
                    break;
                case 1:
                    Toast.makeText(CommonActivity.this, "what1", Toast.LENGTH_LONG).show();
                    onStart();

                    break;

                default:
                    break;

            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mSavedInstanceState = savedInstanceState;
        setContentView(R.layout.common_layout);
        Intent intent=getIntent();
        mType=intent.getType();
        myDatabaseHelper = new MyDatabaseHelper(this, "Wenjian.db", null, 1);
        mDb=myDatabaseHelper.getWritableDatabase();
        Toast.makeText(CommonActivity.this , "intent"+intent.getType() , Toast.LENGTH_SHORT).show();
        init();
        initData(mType);
        //设置返回图标
        mCommonToolbar.setNavigationIcon(R.drawable.ic_back);
        //初始化菜单
        mCommonToolbar.inflateMenu(R.menu.menu_common_activity);
        //设置菜单点击事件
        mCommonToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_serch) {
                    Toast.makeText(CommonActivity.this, "serch", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CommonActivity.this, SearchActivity.class);
                    startActivity(intent);

                } else if (menuItemId == R.id.action_settings) {
                    Intent intent = new Intent(CommonActivity.this, CommonActivity.class);
                    startActivity(intent);
                    Toast.makeText(CommonActivity.this, "setting", Toast.LENGTH_SHORT).show();
                } else if (menuItemId == R.id.action_grid) {
                    if (arrangementFlag == false) {
                        arrangementFlag = true;
                        item.setIcon(R.drawable.ic_grid);
                        //网格排列文件
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);

                    } else {
                        arrangementFlag = false;
                        item.setIcon(R.drawable.ic_vertical);
                        //水平排列文件
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }
                }

                return false;
            }
        });
        //返回图片的点击事件
        mCommonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        mCommonToolbar = (Toolbar) findViewById(R.id.commontoolbar);
        mTextView = (TextView) findViewById(R.id.common_path);
        mListView = (RecyclerView) findViewById(R.id.datalist);
        mFileOpen = new FileOpen(mContext);
    }


    private List<CommonBean> initData(String type) {
        List<String> dataPathList = new ArrayList<>();
        if (type.equals("image")) {
            mTextView.setText("我的文件 > 图片");
            dataPathList = mFileCategoryHelper.getSystemImage(mContext);
            for (int i = 0; i < dataPathList.size(); i++) {
                Log.d("dataPathList", dataPathList.get(i));
            }
        } else if (type.equals("audio")) {
            mTextView.setText("我的文件 >  音频 ");
            dataPathList = mFileCategoryHelper.getSystemAudio(mContext);
            for (int i = 0; i < dataPathList.size(); i++) {
                Log.d("dataPathList", dataPathList.get(i));
            }

        } else if (type.equals("video")) {
            mTextView.setText("我的文件 >  视频 ");
            dataPathList = mFileCategoryHelper.getSystemVideo(mContext);
            for (int i = 0; i < dataPathList.size(); i++) {
                Log.d("dataPathList", dataPathList.get(i));
            }

        } else if (type.equals("document")) {
            mTextView.setText("我的文件 >  文件 ");
            dataPathList=  queryDocument();
            for(int i=0;i<dataPathList.size();i++){
                Log.d("dataPathList",dataPathList.get(i));
            }


        } else if (type.equals("download")) {
            mTextView.setText("我的文件 >  下载 ");
            dataPathList=  queryDownLoad();
            for(int i=0;i<dataPathList.size();i++){
                Log.d("dataPathList",dataPathList.get(i));
            }

        } else if (type.equals("apk")) {
            mTextView.setText("我的文件 >  apk  ");
            dataPathList=  queryAPK();
            for(int i=0;i<dataPathList.size();i++){
                Log.d("dataPathList",dataPathList.get(i));
            }

        }

        if (dataPathList.size() != 0) {
            String path = dataPathList.get(0);
            Bitmap bitmap = FileCategoryHelper.getFileBitmap(this,
                    path, true);
            for (String filePath : dataPathList) {
                File file = new File(filePath);
                Log.d("file", "file" + file.getName() + file.getAbsolutePath());
                data.add(new CommonBean(bitmap, file.getName(),
                        file.getAbsolutePath(), false));
            }
        }

        return data;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (arrangementFlag == false) {
            mLayoutManager = new LinearLayoutManager(this);
            mListView.setLayoutManager(mLayoutManager);
        } else {
            mLayoutManager = new
                    StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            mListView.setLayoutManager(mLayoutManager);
        }
        adapter = new FileListAdapter(CommonActivity.this, (ArrayList<CommonBean>) data, arrangementFlag);
        adapter.setOnItemClickListener(new FileListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                data.get(position).getContent();
                Toast.makeText(CommonActivity.this,data.get(position).getContent(),Toast.LENGTH_LONG).show();
                Intent intent =mFileOpen.openFile(data.get(position).getContent());
                startActivity(intent);
            }
        });
        adapter.setOnItemLongClickListener(new FileListAdapter.OnRecyclerItemLongListener() {
            String[] fileOpItemStr = {"重命名","删除","移动","查看文件属性"};
            List<String> stringList = new ArrayList<>();
            @Override
            public void onItemLongClick(View view, final int position) {
                final String filePath = data.get(position).getContent();
                stringList.add(data.get(position).getContent());
                DialogInterface.OnClickListener fileOpDialogOnClickListener = new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(CommonActivity.this, fileOpItemStr[which],
                                Toast.LENGTH_LONG).show();
                        Toast.makeText(CommonActivity.this, data.get(position).getContent(),
                                Toast.LENGTH_LONG).show();

                        switch (which){
                            case 0:

                                break;
                            case 1:
                                FileHelper.deleteFile(filePath);
                                Toast.makeText(CommonActivity.this, "删除成功",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case 2:


                                break;
                            case 3:
                                List<String> resultList = new ArrayList<String>();
                                resultList=FileHelper.getFileAttribute(filePath);
                                for(int i = 0;i<resultList.size();i++){
                                    Toast.makeText(CommonActivity.this, resultList.get(i),
                                            Toast.LENGTH_SHORT).show();

                                }

                                break;
                            default:

                                break;
                        }
                    }
                };

                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                };
                new AlertDialog.Builder(CommonActivity.this)
                        .setTitle("文件操作")
                        .setItems(fileOpItemStr, fileOpDialogOnClickListener).show();
            }
        });
        mListView.setAdapter(adapter);

    }

    private  List<String>  queryAPK(){
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

    private  List<String>  queryDocument(){
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

    private  List<String>  queryDownLoad(){
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

}
