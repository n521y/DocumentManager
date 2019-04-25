package com.example.app.documentmanager.utils;

import android.content.Context;

import java.util.List;

public interface RecentInformationCall {

    //实现类为FileRecent

    //获取最新的四张图片
    public List<String> getMainDisplayImage(Context context);

    //获取最近一个月的图片
    public List<String> getRecentOneMonthImage(Context context);
}
