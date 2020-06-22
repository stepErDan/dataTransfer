package com.eaglesoft.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Table(value = "transfer_config_info")
public class TransferConfigInfo implements Serializable {
    //串行版本ID
    private static final long serialVersionUID = 3160201760004526153L;

    public TransferConfigInfo(){

    }

    public TransferConfigInfo(ResultSet resultSet) throws SQLException {
        this.setId(resultSet.getLong("id"));
        this.setSourceUrl(resultSet.getString("source_url"));
        this.setSourceUsername(resultSet.getString("source_username"));
        this.setSourcePassword(resultSet.getString("source_password"));
        this.setTargetUrl(resultSet.getString("targer_url"));
        this.setTargetUsername(resultSet.getString("targer_username"));
        this.setTargetPassword(resultSet.getString("targer_password"));
        this.setDatabaseType(resultSet.getString("database_type"));
        this.setRunTime(resultSet.getLong("run_time"));
        this.setState(resultSet.getLong("state"));
    }

    //主键id
    private Long id;

    //来源数据库地址
    private String sourceUrl;

    //来源数据库帐号
    private String sourceUsername;

    //来源数据库密码
    private String sourcePassword;

    //目标数据库地址
    private String targetUrl;

    //目标数据库帐号
    private String targetUsername;

    //目标数据库密码
    private String targetPassword;

    //数据库类型：mysql、mssql
    private String databaseType;

    //运行间隔时间
    private Long runTime;

    //是否有更新，用于重启线程：0、无；1、有；
    private Long state;

    public enum OperationType{
        A,U,D
    }

    public static Long STATE_NORMAL = new Long(0);
}