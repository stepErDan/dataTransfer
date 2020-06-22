package com.eaglesoft.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Table(value = "transfer_table_target")
public class TransferTableTarget implements Serializable {
    //串行版本ID

    public TransferTableTarget(){

    }

    public TransferTableTarget(ResultSet resultSet) throws SQLException {
        this.setId(resultSet.getLong("id"));
        this.setTableName(resultSet.getString("table_name"));
        this.setTableFieldKey(resultSet.getString("table_field_key"));
        this.setInfoId(resultSet.getLong("info_Id"));
    }

    //主键
    private Long id;

    //来源表名
    private String tableName;

    //来源表主键字段名
    private String tableFieldKey;

    //关联transfer_config_info.id
    private Long infoId;
}