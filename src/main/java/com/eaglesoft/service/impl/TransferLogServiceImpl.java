package com.eaglesoft.service.impl;

import com.eaglesoft.dao.TransferLogDao;
import com.eaglesoft.entity.TransferLog;
import com.eaglesoft.service.TransferLogService;
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
public class TransferLogServiceImpl implements TransferLogService{

    @Autowired
    TransferLogDao transferLogDao;

    /**
     * 保存传输日志
     * @param transferLog
     * @return
     */
    @Override
    public int save(TransferLog transferLog){
        return transferLogDao.save(transferLog);
    }

    /**
     * 更新传输日志
     * @param transferLog
     * @return
     */
    @Override
    public int update(TransferLog transferLog) {
        return transferLogDao.update(transferLog);
    }

    /**
     * 获取最新一条的传输日志
     * @return
     */
    @Override
    public TransferLog queryLatest(){
        return transferLogDao.queryLatest();
    }
}
