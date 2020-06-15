package com.eaglesoft.controller;

import com.eaglesoft.entity.TransferConfigInfo;
import com.eaglesoft.entity.TransferLog;
import com.eaglesoft.service.TransferLogService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.sql.*;
import java.util.List;

/**
 * Created by DingK on 2020/1/19.
 */
@Controller
@RequestMapping("/jdbc")
@PropertySource("classpath:jdbc.properties")
public class JdbcController {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private TransferLogService logService;

    @ResponseBody
    @RequestMapping("/startThread")
    private String getData(){
        String sql = "Select * from transfer_config_info";
        List<TransferConfigInfo> list = jdbcTemplate.query(sql, new RowMapper<TransferConfigInfo>() {
            TransferConfigInfo config = null;

            @Override
            public TransferConfigInfo mapRow(ResultSet resultSet, int i) throws SQLException {
                return new TransferConfigInfo(resultSet);
            }
        });

        if(list != null && list.size() > 0){
            for(TransferConfigInfo item : list){
                //新建日志对象
                TransferLog transferLog = new TransferLog();
                //获取最新一条日志
                TransferLog latestTransferLog = logService.queryLatest();
                transferLog.setId(latestTransferLog == null?1:(latestTransferLog.getId() + 1));
                transferLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
                transferLog.setBbh(latestTransferLog == null?1:(latestTransferLog.getBbh() + 1));
                transferLog.setMsg("配置id：" + item.getId() + "数据传输线程启动成功！");
                transferLog.setInfoId(item.getId());
                logService.save(transferLog);

                ConfigThread runnable = new ConfigThread();
                runnable.setConfig(item);
                runnable.setLogService(logService);
                Thread thread = new Thread(runnable);
                thread.start();
            }
        }
        return "";
    }

    @ResponseBody
    @RequestMapping("/startSingleThread")
    private String startSingle(@Param("infoId")Long infoId){
        String sql = "Select * from transfer_config_info where id = " + infoId;
        List<TransferConfigInfo> list = jdbcTemplate.query(sql, new RowMapper<TransferConfigInfo>() {
            TransferConfigInfo config = null;

            @Override
            public TransferConfigInfo mapRow(ResultSet resultSet, int i) throws SQLException {
                return new TransferConfigInfo(resultSet);
            }
        });

        if(list != null && list.size() == 1){
            for(TransferConfigInfo item : list){
                //新建日志对象
                TransferLog transferLog = new TransferLog();
                //获取最新一条日志
                TransferLog latestTransferLog = logService.queryLatest();
                transferLog.setId(latestTransferLog == null?1:(latestTransferLog.getId() + 1));
                transferLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
                transferLog.setBbh(latestTransferLog == null?1:(latestTransferLog.getBbh() + 1));
                logService.save(transferLog);

                TransferThread runnable = new TransferThread();
                runnable.setConfig(item);
                runnable.setLogService(logService);
                runnable.setTransferLog(transferLog);
                Thread thread = new Thread(runnable);
                thread.start();
            }
        }
        return "";
    }

//    /**
//     * 根据字段名从配置文件中获取键对应的值
//     * @param name 键
//     * @return 值
//     */
//    private String getProval(String name){
//        String val = "";
//        // 获取配置文件
//        Properties properties = new Properties();
//        try {
//            InputStream in = new BufferedInputStream(new FileInputStream("classpath:jdbc.properties"));
//            properties.load(in);
//            val = properties.getProperty(name);
//            in.close();
//        } catch (IOException e) {
//            System.out.println(e);
//        }
//        return val;
//    }
}
