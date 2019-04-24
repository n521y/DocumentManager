package com.example.app.documentmanager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.app.documentmanager.adapter.FileRecentAdapter;
import com.example.app.documentmanager.bean.CommonBean;
import com.example.app.documentmanager.utils.FileRecent;

import java.util.List;

public class RecentActivity extends AppCompatActivity {

    private Context mContext;
    private Toolbar mToolbar;
    private final static int ONE_DAY = 1;
    private final static int ONE_WEEK = 7;
    private final static int ONE_MONTH = 30;
    private final static int ONE_YEAR = 365;
    private RecyclerView mRecyclerView;
    private List<CommonBean> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        mContext = this;

        //初始化RecyclerView
        mRecyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                RecentActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //初始化当前界面
        //showRecentRecyclerView(ONE_DAY);

        //初始化ToolBar
        initToolBar();

        //设置返回键
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化Menu点击事件
        initMenuEvents();

    }

    private void initToolBar() {
        mToolbar = findViewById(R.id.recent_toolbar);

        //设置ToolBar的导航图标
        mToolbar.setNavigationIcon(R.drawable.ic_back);

        //设置主标题
        //mToolbar.setTitle("");

        //加载menu布局文件
        mToolbar.inflateMenu(R.menu.menu_recent);
    }

    private void initMenuEvents() {
        //设置Menu监听器
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int menuItemId = menuItem.getItemId();
                switch(menuItemId) {
                    case R.id.menu_recent_one_week:
                        Toast.makeText(RecentActivity.this, "最近一周",
                                Toast.LENGTH_SHORT).show();
                        showRecentRecyclerView(ONE_WEEK);
                        break;
                    case R.id.menu_recent_one_month:
                        Toast.makeText(RecentActivity.this, "最近一个月",
                                Toast.LENGTH_SHORT).show();
                        showRecentRecyclerView(ONE_MONTH);
                        break;
                    case R.id.menu_recent_one_year:
                        Toast.makeText(RecentActivity.this, "最近一年",
                                Toast.LENGTH_SHORT).show();
                        showRecentRecyclerView(ONE_YEAR);
                        break;
                }
                return false;
            }
        });
    }

    private void showRecentRecyclerView(int time) {
        FileRecentAdapter fileRecentMonthAdapter = new FileRecentAdapter(
                FileRecent.pathChange(mContext, FileRecent.getRecentSystemImage(mContext, time)));
        mRecyclerView.setAdapter(fileRecentMonthAdapter);
    }


}
