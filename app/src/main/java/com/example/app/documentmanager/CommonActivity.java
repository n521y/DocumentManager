package com.example.app.documentmanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.documentmanager.adapter.FileListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommonActivity extends AppCompatActivity {

    private FileListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Bundle mSavedInstanceState;
    private boolean arrangementFlag = false;
    private Toolbar mCommonToolbar;
    private TextView mTextView;
    private RecyclerView mListView;
    public List<String> data =new ArrayList() ;


    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(CommonActivity.this , "what0" , Toast.LENGTH_LONG).show();
                    onStart();
                    break;
                case 1:
                    Toast.makeText(CommonActivity.this , "what1" , Toast.LENGTH_LONG).show();
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
        mSavedInstanceState=savedInstanceState;
        setContentView(R.layout.common_layout);
        init();
        //设置返回图标
        mCommonToolbar.setNavigationIcon(R.drawable.ic_back);
        //初始化菜单
        mCommonToolbar.inflateMenu(R.menu.menu_common_activity);
        //设置菜单点击事件
        mCommonToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final int menuItemId = item.getItemId();
                if(menuItemId == R.id.action_serch){
                    Toast.makeText(CommonActivity.this , "serch" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CommonActivity.this,SearchActivity.class);
                    startActivity(intent);

                }else if (menuItemId == R.id.action_settings){
                    Intent intent = new Intent(CommonActivity.this,CommonActivity.class);
                    startActivity(intent);
                    Toast.makeText(CommonActivity.this , "setting" , Toast.LENGTH_SHORT).show();
                }else if(menuItemId == R.id.action_grid){
                    if(arrangementFlag == false){
                        arrangementFlag=true;
                        item.setIcon(R.drawable.ic_grid);
                        //网格排列文件
                        Message msg = new Message();
                        msg.what=1;
                        mHandler.sendMessage(msg);

                    }else {
                        arrangementFlag = false;
                        item.setIcon(R.drawable.ic_vertical);
                        //水平排列文件
                        Message msg = new Message();
                        msg.what=0;
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

    private  void  init(){
        mCommonToolbar = (Toolbar) findViewById(R.id.commontoolbar);
        mTextView = (TextView)findViewById(R.id.common_path);
        mListView = (RecyclerView) findViewById(R.id.datalist);
        for(int i =0 ; i<10;i++){
            data.add("niu"+i);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (arrangementFlag == false){
            mLayoutManager = new LinearLayoutManager(this);
            mListView.setLayoutManager(mLayoutManager);
        }else {
            mLayoutManager = new
                    StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            mListView.setLayoutManager(mLayoutManager);
        }
        adapter = new FileListAdapter(CommonActivity.this, (ArrayList<String>) data,arrangementFlag);
        adapter.setOnItemClickListener(new FileListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int data) {
                Toast.makeText(CommonActivity.this,"item",Toast.LENGTH_LONG).show();
            }
        });
        adapter.setOnItemLongClickListener(new FileListAdapter.OnRecyclerItemLongListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(CommonActivity.this,"itemlong",Toast.LENGTH_LONG).show();
            }
        });
        mListView.setAdapter(adapter);



    }
}
