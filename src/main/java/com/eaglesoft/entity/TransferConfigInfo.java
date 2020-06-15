package com.eaglesoft.entity;

import jdk.nashorn.internal.objects.annotations.Property;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        this.setTableSourceName(resultSet.getString("table_source_name"));
        this.setTableFieldKey(resultSet.getString("table_field_key"));
        this.setTableFieldState(resultSet.getString("table_field_state"));
        this.setTableRequirementBefore(resultSet.getString("table_requirement_before"));
        this.setTableRequirementAfter(resultSet.getString("table_requirement_after"));
        this.setTableTargetName(resultSet.getString("table_target_name"));
        this.setTableRequirementError(resultSet.getString("table_requirement_error"));
        this.setTableFieldOperation(resultSet.getString("table_field_operation"));
        this.setRunTime(resultSet.getLong("run_time"));
        this.setState(resultSet.getLong("state"));
    }

    private Long id;

    private String sourceUrl;

    private String sourceUsername;

    private String sourcePassword;

    private String targetUrl;

    private String targetUsername;

    private String targetPassword;

    private String databaseType;

    private String tableSourceName;

    private String tableFieldKey;

    private String tableFieldState;

    private String tableRequirementBefore;

    private String tableRequirementAfter;

    private String tableRequirementError;

    private String tableTargetName;

    private String tableFieldOperation;

    private Long runTime;

    private Long state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceUsername() {
        return sourceUsername;
    }

    public void setSourceUsername(String sourceUsername) {
        this.sourceUsername = sourceUsername;
    }

    public String getSourcePassword() {
        return sourcePassword;
    }

    public void setSourcePassword(String sourcePassword) {
        this.sourcePassword = sourcePassword;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
    }

    public String getTargetPassword() {
        return targetPassword;
    }

    public void setTargetPassword(String targetPassword) {
        this.targetPassword = targetPassword;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getTableSourceName() {
        return tableSourceName;
    }

    public void setTableSourceName(String tableSourceName) {
        this.tableSourceName = tableSourceName;
    }

    public String getTableFieldKey() {
        return tableFieldKey;
    }

    public void setTableFieldKey(String tableFieldKey) {
        this.tableFieldKey = tableFieldKey;
    }

    public String getTableFieldState() {
        return tableFieldState;
    }

    public void setTableFieldState(String tableFieldState) {
        this.tableFieldState = tableFieldState;
    }

    public String getTableRequirementBefore() {
        return tableRequirementBefore;
    }

    public void setTableRequirementBefore(String tableRequirementBefore) {
        this.tableRequirementBefore = tableRequirementBefore;
    }

    public String getTableRequirementAfter() {
        return tableRequirementAfter;
    }

    public void setTableRequirementAfter(String tableRequirementAfter) {
        this.tableRequirementAfter = tableRequirementAfter;
    }

    public String getTableTargetName() {
        return tableTargetName;
    }

    public void setTableTargetName(String tableTargetName) {
        this.tableTargetName = tableTargetName;
    }

    @Override
    public String toString() {
        return "TransferConfigInfo{" +
                "id=" + id +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", sourceUsername='" + sourceUsername + '\'' +
                ", sourcePassword='" + sourcePassword + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
                ", targetUsername='" + targetUsername + '\'' +
                ", targetPassword='" + targetPassword + '\'' +
                ", databaseType='" + databaseType + '\'' +
                ", tableSourceName='" + tableSourceName + '\'' +
                ", tableFieldKey='" + tableFieldKey + '\'' +
                ", tableFieldState='" + tableFieldState + '\'' +
                ", tableRequirementBefore='" + tableRequirementBefore + '\'' +
                ", tableRequirementAfter='" + tableRequirementAfter + '\'' +
                ", tableRequirementError='" + tableRequirementError + '\'' +
                ", tableTargetName='" + tableTargetName + '\'' +
                ", tableFieldOperation='" + tableFieldOperation + '\'' +
                '}';
    }

    public String getTableRequirementError() {
        return tableRequirementError;
    }

    public void setTableRequirementError(String tableRequirementError) {
        this.tableRequirementError = tableRequirementError;
    }

    public String getTableFieldOperation() {
        return tableFieldOperation;
    }

    public void setTableFieldOperation(String tableFieldOperation) {
        this.tableFieldOperation = tableFieldOperation;
    }

    public Long getRunTime() {
        return runTime;
    }

    public void setRunTime(Long runTime) {
        this.runTime = runTime;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public enum OperationType{
        A,U,D
    }
}