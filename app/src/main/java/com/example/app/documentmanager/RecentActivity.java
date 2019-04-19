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
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.documentmanager.adapter.FileListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecentActivity extends AppCompatActivity {

    private boolean arrangementFlag = false;
    private Toolbar mRecenttoolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);
        init();
        //设置返回图标
        mRecenttoolbar.setNavigationIcon(R.drawable.ic_back);
        //初始化菜单
        mRecenttoolbar.inflateMenu(R.menu.menu_common_activity);
        //设置菜单点击事件
        mRecenttoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final int menuItemId = item.getItemId();
                if(menuItemId == R.id.action_serch){
                    Toast.makeText(RecentActivity.this , "serch" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecentActivity.this,SearchActivity.class);
                    startActivity(intent);

                }else if (menuItemId == R.id.action_settings){
                    Intent intent = new Intent(RecentActivity.this,RecentActivity.class);
                    startActivity(intent);
                    Toast.makeText(RecentActivity.this , "setting" , Toast.LENGTH_SHORT).show();
                }else if(menuItemId == R.id.action_grid){
                    if(arrangementFlag == false){
                        arrangementFlag=true;
                        item.setIcon(R.drawable.ic_grid);
                        //网格排列文件


                    }else {
                        arrangementFlag = false;
                        item.setIcon(R.drawable.ic_vertical);
                        //水平排列文件

                    }
                }

                return false;
            }
        });

        //返回图片的点击事件
        mRecenttoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    private  void  init(){
        mRecenttoolbar = (Toolbar) findViewById(R.id.recenttoolbar);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}
