package com.eaglesoft.controller.thread;

import com.eaglesoft.entity.TransferConfigInfo;
import com.eaglesoft.entity.TransferLog;
import com.eaglesoft.service.TransferLogService;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 监控线程
 * Created by DingK on 2020/1/20.
 */
public class TransferThread implements Runnable{
    //监控连接配置
    private TransferConfigInfo config;

    private TransferLogService logService;

    //mysql数据库驱动配置
    @Value("thransfer.jdbc.mysql.driver")
    private String mysqlDrive = "";

    //sql server数据库驱动配置
    @Value("thransfer.jdbc.mssql.driver")
    private String mssqlDrive = "";

    private int bbh;

    @Override
    public void run() {
        //创建连接
        Connection sourceCon;
        Connection targetCon;
        //sql查询语句
        String sql = "select * from " + config.getTableSourceName();
        //添加筛选条件
        if(config.getTableFieldState() != null && config.getTableRequirementBefore() != null){
            sql += " where " + config.getTableFieldState() + " = " + config.getTableRequirementBefore();
        }
        TransferLog transferLog = new TransferLog();
        transferLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
        transferLog.setBbh(bbh);
        PreparedStatement pStemt = null;
        try{
            //加载驱动
            if("mysql".equals(config.getDatabaseType())){
                Class.forName(mysqlDrive);
                //创建源表连接jdbc:mysql://localhost:3306/datatable?serverTimezone=UTC&characterEncoding=utf8
                sourceCon = DriverManager.getConnection(config.getSourceUrl(),config.getSourceUsername(),config.getSourcePassword());
                targetCon = DriverManager.getConnection(config.getTargetUrl(),config.getTargetUsername(),config.getTargetPassword());
            }else if("mssql".equals(config.getDatabaseType())){
                Class.forName(mssqlDrive);
                //创建源表连接jdbc:mircosoft:sqlserver:localhost:1433;databasename=
                sourceCon = DriverManager.getConnection(config.getSourceUrl(),config.getSourceUsername(),config.getSourcePassword());
                targetCon = DriverManager.getConnection(config.getTargetUrl(),config.getTargetUsername(),config.getTargetPassword());
            }else {
                throw new Exception();
            }
            pStemt = sourceCon.prepareStatement(sql);
            //返回结果表结构
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表字段数
            int size = rsmd.getColumnCount();
            String[] colnames = new String[size];
            String[] colTypes = new String[size];
            int[] colSizes = new int[size];
            for (int i = 0; i < size; i++) {
                colnames[i] = rsmd.getColumnName(i + 1);
                colTypes[i] = rsmd.getColumnTypeName(i + 1);
                colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
            }
            //返回结果集
            ResultSet rs2 = pStemt.executeQuery();
            List<String> sourceIds = new ArrayList();
            List<String> errIds = new ArrayList();
            //数据请求参数
            StringBuffer info = new StringBuffer();
            info.append(" { ");
            //拼接推送sql
            while (rs2.next()){
                info.append(" [ ");
                String key = rs2.getString(config.getTableFieldKey());
                int r = 0;
                switch (TransferConfigInfo.OperationType.valueOf(rs2.getString(config.getTableFieldOperation()))){
                    case A:
                        //拼接插入语句
                        StringBuffer insertStr = new StringBuffer("insert into ").append(config.getTableTargetName()).append("(");
                        StringBuffer value = new StringBuffer("");
                        for(int j = 0;j < size;j++){
                            info.append(j == 0?"'":", '").append(colnames[j]).append("' : '").append(rs2.getString(colnames[j])).append("' ");
                            if(config.getTableFieldState().equals(colnames[j]) || config.getTableFieldOperation().equals(colnames[j])){
                                continue;
                            }
                            if( rs2.getString(colnames[j]) != null) {
                                //判断是否是状态字段
                                String colvalue = rs2.getString(colnames[j]);
                                colvalue = toSqlString(colTypes[j],colvalue);
                                if (j == 0){
                                    insertStr.append(colnames[j]);
                                    value.append(colvalue);
                                }else {
                                    insertStr.append(",").append(colnames[j]);
                                    value.append(",").append(colvalue);
                                }
                            }
                        }
                        insertStr.append(") values(").append(value.toString()).append(")");
                        //执行插入语句
                        pStemt = targetCon.prepareStatement(insertStr.toString());
                        r = pStemt.executeUpdate();
                        break;
                    case U:
                        //拼接更新语句
                        StringBuffer updateStr = new StringBuffer("update ")
                                .append(config.getTableTargetName())
                                .append(" set ");
                        for(int i = 0;i < colnames.length;i++){
                            info.append(i == 0?"'":", '").append(colnames[i]).append("' : '").append(rs2.getString(colnames[i])).append("' ");
                            if(config.getTableFieldState().equals(colnames[i]) || config.getTableFieldOperation().equals(colnames[i])){
                                continue;
                            }
                            if( rs2.getString(colnames[i]) != null) {
                                //判断是否是状态字段
                                String colvalue = rs2.getString(colnames[i]);
                                colvalue = toSqlString(colTypes[i],colvalue);
                                if(i != 0){
                                    updateStr.append(",");
                                }
                                updateStr.append(colnames[i]).append(" = ").append(colvalue);
                            }
                            if(config.getTableFieldKey().equals(colnames[i])) {
                                key = toSqlString(colTypes[i],key);
                            }
                        }
                        updateStr.append(" where ").append(config.getTableFieldKey()).append(" = ").append(key);
                        pStemt = targetCon.prepareStatement(updateStr.toString());
                        r = pStemt.executeUpdate();
                        break;
                    case D:
                        //拼接删除语句
                        StringBuffer deleteStr = new StringBuffer("delete from ")
                                .append(config.getTableTargetName())
                                .append(" where ").append(config.getTableFieldKey()).append(" = ");
                        for(int i = 0;i < colnames.length;i++){
                            info.append(i == 0?"'":", '").append(colnames[i]).append("' : '").append(rs2.getString(colnames[i])).append("' ");
                            if(config.getTableFieldKey().equals(colnames[i])) {
                                deleteStr.append(toSqlString(colTypes[i],key));
                            }
                        }
                        pStemt = targetCon.prepareStatement(deleteStr.toString());
                        r = pStemt.executeUpdate();
                        break;
                    default:
                }
                info.append(" ], ");
                //判断r > 0，表示插入成功，状态改成1，并且把id加入来源id中
                if(r > 0){
                    sourceIds.add(rs2.getString(config.getTableFieldKey()));
                }else if(r == 0){
                    errIds.add(rs2.getString(config.getTableFieldKey()));
                }
            }
            info.append(" }");

            //保存请求参数
            if(info.length() < 4 * 1024 * 1024){
                transferLog.setData(info.toString());
            }else{
                transferLog.setData("数据过长，无法保存");
            }

            //修改来源表字段的状态：成功
            if(sourceIds.size() > 0){
                StringBuffer updateSourceStateSql = new StringBuffer("update ")
                                                    .append(config.getTableSourceName())
                                                    .append(" set ")
                                                    .append(config.getTableFieldState())
                                                    .append(" = ")
                                                    .append(config.getTableRequirementAfter())
                                                    .append(" where ")
                                                    .append(config.getTableFieldKey())
                                                    .append(" in (");
                for (int i = 0;i < sourceIds.size();i++){
                    if(i != 0){
                        updateSourceStateSql.append(",");
                    }
                    updateSourceStateSql.append(sourceIds.get(i));
                }
                updateSourceStateSql.append(")");
                pStemt = sourceCon.prepareStatement(updateSourceStateSql.toString());
                //执行更新状态语句
                int i = pStemt.executeUpdate();
            }

            //修改来源表字段的状态：失败
            if(errIds.size() > 0){
                StringBuffer updateSourceStateSql = new StringBuffer("update ")
                        .append(config.getTableSourceName())
                        .append(" set ")
                        .append(config.getTableFieldState())
                        .append(" = ")
                        .append(config.getTableRequirementError())
                        .append(" where ")
                        .append(config.getTableFieldKey())
                        .append(" in (");
                for (int i = 0;i < errIds.size();i++){
                    if(i != 0){
                        updateSourceStateSql.append(",");
                    }
                    updateSourceStateSql.append(errIds.get(i));
                }
                updateSourceStateSql.append(")");
                pStemt = sourceCon.prepareStatement(updateSourceStateSql.toString());
                //执行更新状态语句
                int i = pStemt.executeUpdate();
            }

            StringBuffer data = new StringBuffer("操作成功的数据id：");
            sourceIds.forEach(item ->{
                data.append(item).append(",");
            });
            data.append("；操作失败的数据id：");
            errIds.forEach(item -> {
                data.append(item).append(",");
            });

            transferLog.setMsg(data.toString());
            logService.save(transferLog);

            //关闭连接
            sourceCon.close();
            targetCon.close();
        }catch (SQLException sqlE){
            transferLog.setMsg(sqlE.toString());
            logService.save(transferLog);
        }catch (Exception e){
            transferLog.setMsg(e.toString());
            logService.save(transferLog);
        }
        try {
            Thread.sleep(config.getRunTime());
//            Thread.currentThread().sleep(config.getRunTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String toSqlString(String type,String colvalue){
        switch (type) {
            case "INT":
                break;
            case "BIGINT":
                break;
            case "VARCHAR":
                colvalue = new StringBuffer("'").append(colvalue).append("'").toString();
                break;
            case "TIMESTAMP":
                colvalue = new StringBuffer("'").append(colvalue).append("'").toString();
                break;
            case "DATETIME":
                colvalue = new StringBuffer("'").append(colvalue).append("'").toString();
                break;
            default:
                colvalue = new StringBuffer("'").append(colvalue).append("'").toString();
        }
        return colvalue;
    }

    public void setConfig(TransferConfigInfo config) {
        this.config = config;
    }

    public void setLogService(TransferLogService logService) {
        this.logService = logService;
    }

    public void setBbh(int bbh) {
        this.bbh = bbh;
    }
}
