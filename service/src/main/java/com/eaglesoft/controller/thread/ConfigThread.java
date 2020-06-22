package com.eaglesoft.controller.thread;

import com.eaglesoft.entity.TransferConfigInfo;
import com.eaglesoft.service.TransferLogService;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * 配置线程，循环暂停
 * Created by DingK on 2020/1/20.
 */
public class ConfigThread implements Runnable{
    //监控连接配置
    private TransferConfigInfo config;

    //
    private JdbcTemplate jdbcTemplate;

    private TransferLogService logService;

    private int bbh;

    public ConfigThread(TransferConfigInfo config,TransferLogService logService,int bbh,JdbcTemplate jdbcTemplate){
        this.config = config;
        this.bbh = bbh;
        this.logService = logService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run() {
        TransferThread runnable = new TransferThread();
        runnable.setConfig(config);
        runnable.setLogService(logService);
        while(true){
            //1、首先查询自身是否有变化
            String sql = "Select * from transfer_config_info where id = " + config.getId();
            List<TransferConfigInfo> list = jdbcTemplate.query(sql, (resultSet, i) -> new TransferConfigInfo(resultSet));
            if(list.size() > 0){
                TransferConfigInfo newConfig = list.get(0);
                if(!TransferConfigInfo.STATE_NORMAL.equals(newConfig.getState())){
                    config = newConfig;
                    runnable = new TransferThread();
                    runnable.setConfig(config);
                    runnable.setLogService(logService);
                }
                runnable.setBbh(++bbh);
                //2、无变化，根据配置继续执行线程
                Thread thread = new Thread(runnable);
                thread.start();
                try {
                    //线程休眠
                    //Thread.currentThread().sleep(config.getRunTime());
                    //todo 是否会释放线程？
                    Thread.sleep(config.getRunTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                break;
            }
        }
    }

    public void setConfig(TransferConfigInfo config) {
        this.config = config;
    }

    public void setBbh(int bbh) {
        this.bbh = bbh;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setLogService(TransferLogService logService) {
        this.logService = logService;
    }
}
