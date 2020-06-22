package com.eaglesoft.controller;

import com.eaglesoft.controller.thread.ConfigThread;
import com.eaglesoft.controller.thread.ThreadPoolInit;
import com.eaglesoft.entity.TransferConfigInfo;
import com.eaglesoft.entity.TransferLog;
import com.eaglesoft.service.TransferLogService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
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

    /**
     * 批量启动线程
     * @return
     */
    @ResponseBody
    @RequestMapping("/startThread")
    private String getData(){
        //查询配置信息表
        //todo 少用*,改为字段
        String sql = "Select * from transfer_config_info";
        List<TransferConfigInfo> list = jdbcTemplate.query(sql, (resultSet, i) -> new TransferConfigInfo(resultSet));

        //判断查询结果是否为空
        if(list != null && list.size() > 0){
            //todo 使用线程池管理
            for(TransferConfigInfo item : list){
                //新建日志对象
                TransferLog transferLog = new TransferLog();
                //获取最新一条日志
                TransferLog latestTransferLog = logService.queryLatest(item.getId());
                transferLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
                transferLog.setBbh(latestTransferLog == null?1:(latestTransferLog.getBbh() + 1));
                transferLog.setMsg("配置id：" + item.getId() + "数据传输线程启动成功！");
                transferLog.setInfoId(item.getId());
                logService.save(transferLog);

                //初始化线程池
                ThreadPoolInit threadPoolInit = ThreadPoolInit.instance;
                //初始化线程
                ConfigThread runnable = new ConfigThread(item,logService,transferLog.getBbh(),jdbcTemplate);
                //加入线程池
                threadPoolInit.getExecutor().execute(runnable);
            }
        }
        return "";
    }

    /**
     * 启用单线程
     * 注：仅用于新增配置时
     * @param infoId
     * @return
     */
    @ResponseBody
    @RequestMapping("/startSingleThread")
    private String startSingle(@Param("infoId")Long infoId){
        //查询单条配置从配置信息表
        String sql = "Select * from transfer_config_info where id = " + infoId;
        List<TransferConfigInfo> list = jdbcTemplate.query(sql, (resultSet, i) -> new TransferConfigInfo(resultSet));

        if(list != null && list.size() == 1){
            //新建日志对象
            TransferLog transferLog = new TransferLog();
            //获取最新一条日志
            TransferLog latestTransferLog = logService.queryLatest(list.get(0).getId());
            transferLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
            transferLog.setBbh(latestTransferLog == null?1:(latestTransferLog.getBbh() + 1));
            transferLog.setMsg("配置id：" + list.get(0).getId() + "数据传输线程启动成功！");
            transferLog.setInfoId(list.get(0).getId());
            logService.save(transferLog);

            //初始化线程池
            ThreadPoolInit threadPoolInit = ThreadPoolInit.instance;
            //初始化线程
            ConfigThread runnable = new ConfigThread(list.get(0),logService,transferLog.getBbh(),jdbcTemplate);
            //加入线程池
            threadPoolInit.getExecutor().execute(runnable);
        }
        return "";
    }


    @ResponseBody
    @RequestMapping("/testMssql")
    private String testMssql() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String sql = "Select * from source";
        //创建连接
        Connection sourceCon = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1;DatabaseName=datatable","sa","123456");
        PreparedStatement pStemt = sourceCon.prepareStatement(sql);
        //返回结果表结构
        ResultSet rs = pStemt.executeQuery();
        while (rs.next()) {
            String id = rs.getString("id");
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
