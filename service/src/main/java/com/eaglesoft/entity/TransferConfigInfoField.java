package com.eaglesoft.entity;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Table(value = "transfer_config_info_field")
public class TransferConfigInfoField implements Serializable {
    //串行版本ID
    private static final long serialVersionUID = 3160201760004526153L;

    public TransferConfigInfoField(){

    }

    public TransferConfigInfoField(ResultSet resultSet) throws SQLException {
        this.setId(resultSet.getLong("id"));
        this.setSourceFieldName(resultSet.getString("source_field_name"));
        this.setTargetFieldName(resultSet.getString("target_field_name"));
    }

    //主键，等同transfer_config_info.id
    private Long id;

    //源字段名
    private String sourceFieldName;

    //目标字段名
    private String targetFieldName;

}