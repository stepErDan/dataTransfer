package com.eaglesoft.entity;

import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.sql.Timestamp;

@Table(value = "transfer_log")
public class TransferLog implements Serializable {
    //串行版本ID
    private static final long serialVersionUID = 3160201760004526154L;

    private Long id;

    private Timestamp createTime;

    private String msg;

    private String data;

    private Integer bbh;

    private Long infoId;

    @Override
    public String toString() {
        return "TransferLog{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                ", infoId='" + infoId + '\'' +
                ", bbh=" + bbh +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getBbh() {
        return bbh;
    }

    public void setBbh(Integer bbh) {
        this.bbh = bbh;
    }

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }
}