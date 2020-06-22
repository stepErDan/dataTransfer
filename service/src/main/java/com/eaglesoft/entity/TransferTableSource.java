package com.eaglesoft.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Table(value = "transfer_table_source")
public class TransferTableSource implements Serializable {
    //串行版本ID

    public TransferTableSource(){

    }

    public TransferTableSource(ResultSet resultSet) throws SQLException {
        this.setId(resultSet.getLong("id"));
        this.setTableName(resultSet.getString("table_name"));
        this.setTableFieldKey(resultSet.getString("table_field_key"));
        this.setTableFieldState(resultSet.getString("table_field_state"));
        this.setTableRequirementBefore(resultSet.getString("table_requirement_before"));
        this.setTableRequirementAfter(resultSet.getString("table_requirement_after"));
        this.setTableRequirementError(resultSet.getString("table_requirement_error"));
        this.setTableFieldOperation(resultSet.getString("table_field_operation"));
        this.setInfoId(resultSet.getLong("info_Id"));
    }

    //主键
    private Long id;

    //来源表名
    private String tableName;

    //来源表主键字段名
    private String tableFieldKey;

    //标记字段名
    private String tableFieldState;

    //标记字段请求前状态
    private String tableRequirementBefore;

    //标记字段请求后完成状态
    private String tableRequirementAfter;

    //标记字段请求后异常状态
    private String tableRequirementError;

    //操作类型字段名，值默认：A、新增；U、修改；D、删除；注：假删除算修改！
    private String tableFieldOperation;

    //关联transfer_config_info.id
    private Long infoId;
}