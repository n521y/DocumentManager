package com.example.app.documentmanager.utils;

import android.content.Context;

import java.util.List;

public interface RecentInformationCall {

    //实现类为FileRecent

    //获取最近一个月的图片按时间排序，若为空则无最近文件，
    public List<String> getMainDisplayImage(Context context);


}
