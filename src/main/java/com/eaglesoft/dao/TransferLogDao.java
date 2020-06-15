package com.eaglesoft.dao;

import com.eaglesoft.entity.TransferLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by DingK on 2020/2/7.
 */
@Repository
public class TransferLogDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * 保存传输日志
     * @param transferLog
     * @return
     */
    public Integer save(TransferLog transferLog){
        String sql = "insert into transfer_log(id,create_time,msg,bbh,data) values(?,?,?,?,?)";
        int i = jdbcTemplate.update(sql,transferLog.getId(),transferLog.getCreateTime(),transferLog.getMsg(),transferLog.getBbh(),transferLog.getData());
        return i;
    }

    /**
     * 保存传输日志
     * @param transferLog
     * @return
     */
    public Integer update(TransferLog transferLog){
        String sql = "update transfer_log set msg = ?,data = ? where id = ?";
        int i = jdbcTemplate.update(sql,transferLog.getMsg(),transferLog.getData(),transferLog.getId());
        return i;
    }

    /**
     * 获取最新一条的传输日志
     * @return
     */
    public TransferLog queryLatest(){
        String sql = "select id,create_time,msg,bbh,data from transfer_log order by create_time desc";
        List<TransferLog> list = jdbcTemplate.query(sql, new RowMapper<TransferLog>() {
            TransferLog log = null;

            @Override
            public TransferLog mapRow(ResultSet resultSet, int i) throws SQLException {
                log = new TransferLog();
                log.setId(resultSet.getLong("id"));
                log.setBbh(resultSet.getInt("bbh"));
                log.setCreateTime(resultSet.getTimestamp("create_time"));
                log.setData(resultSet.getString("data"));
                log.setMsg(resultSet.getString("msg"));
                return log;
            }
        });
        if(list == null || list.size() == 0){
            return null;
        }
        return list.get(0);
    }
}
