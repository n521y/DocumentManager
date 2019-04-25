package com.example.app.documentmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.documentmanager.adapter.FileListAdapter;
import com.example.app.documentmanager.bean.CommonBean;
import com.example.app.documentmanager.utils.FileOpen;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity  implements View.OnClickListener {
    private RecyclerView mListView;
    private FileListAdapter mAdapter;
    private ArrayList<CommonBean> mDatas;
    private String mSearchType;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mListView =findViewById(R.id.list);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mSearchType = getIntent().getType();
        mDatas = new ArrayList<CommonBean>();
        mAdapter = new FileListAdapter(this,mDatas,false);
        mAdapter.setOnItemClickListener(mItemClickListener);
        Toolbar toolbar = (Toolbar)findViewById(R.id.serch_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final EditText serchText = (EditText)findViewById(R.id.serch_text);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.
                    permission.READ_EXTERNAL_STORAGE }, 1);
        } else {
            read();
        }
        serchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = serchText.getText().toString().trim();
                Toast.makeText(SearchActivity.this, serchText.getText()+"", Toast.LENGTH_SHORT).show();
                //进行搜索功能实现
                mDatas.clear();
                searchMultiMedia(keyword,mSearchType,mDatas);
                mListView.setAdapter(mAdapter);
            }
        });
        serchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(SearchActivity.this, "点击了软键盘中的搜索按钮", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private FileListAdapter.OnRecyclerViewItemClickListener mItemClickListener = new
            FileListAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View view, int data) {
            String path = mDatas.get(data).getContent();
            Intent intent = FileOpen.openFile(path);
            startActivity(intent);
        }
    };

    //根据关键字查询文件
    private void searchMultiMedia(String keyword,String queryType,List<CommonBean> dataList) {
        boolean singleType = true;
        //定义查询信息
        Uri uri = null;
        String pathColumnName = null;
        String dispColumnName = null;
        switch (queryType) {
            case "image":{
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                dispColumnName = MediaStore.Images.Media.DISPLAY_NAME;
                pathColumnName = MediaStore.Images.Media.DATA;
                break;
            }
            case "audio":{
                uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                dispColumnName = MediaStore.Audio.Media.DISPLAY_NAME;
                pathColumnName = MediaStore.Audio.Media.DATA;
                break;
            }
            case "video":{
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                dispColumnName = MediaStore.Video.Media.DISPLAY_NAME;
                pathColumnName = MediaStore.Video.Media.DATA;
                break;
            }
            default:{
                //递归搜索
                searchMultiMedia(keyword,"image",dataList);
                searchMultiMedia(keyword,"audio",dataList);
                searchMultiMedia(keyword,"video",dataList);
                //单类型查询标志位更新
                singleType = false;
                break;
            }
        }
        if (singleType && !"".equals(keyword)) {
            //条件查询文件，获取游标
            ContentResolver resolver = this.getContentResolver();
            Cursor cursor= resolver.query( uri,null, dispColumnName+" like '%"+keyword+"%'", null, null);
            //取出游标中的数据存放到数据列表中
            String fileName = null;
            String filePath = null;
            while (cursor.moveToNext()) {
                fileName = cursor.getString(cursor.getColumnIndex(dispColumnName));
                filePath = cursor.getString(cursor.getColumnIndex(pathColumnName));
                dataList.add(new CommonBean(null,fileName,filePath));
            }
            //关闭游标
            cursor.close();
        }
    }


    private void read(){
        //Uri uri = Uri.parse("content://com.android.providers.downloads.documents/downloads");
        Cursor cursor= null;
        try {
            cursor = getContentResolver().query(MediaStore.Images.Media.INTERNAL_CONTENT_URI,null,null,null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.d("cursor",cursor.toString());
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    read();
                } else {
                    Toast.makeText(SearchActivity.this, "requestCode", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }

    }

    @Override
    public void onClick(View v) {

    }
}
