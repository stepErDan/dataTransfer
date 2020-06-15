package com.eaglesoft.controller;

import com.eaglesoft.entity.TransferConfigInfo;
import com.eaglesoft.entity.TransferLog;
import com.eaglesoft.service.TransferLogService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置线程，循环暂停
 * Created by DingK on 2020/1/20.
 */
public class ConfigThread implements Runnable{
    //监控连接配置
    private TransferConfigInfo config;

    private TransferLogService logService;

    @Override
    public void run() {
        //1、首先查询自身是否有变化    
        TransferThread runnable = new TransferThread();
        runnable.setConfig(config);
        runnable.setLogService(logService);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void setConfig(TransferConfigInfo config) {
        this.config = config;
    }

    public void setLogService(TransferLogService logService) {
        this.logService = logService;
    }
}
