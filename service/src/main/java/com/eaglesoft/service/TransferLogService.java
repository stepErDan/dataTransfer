package com.eaglesoft.service;

import com.eaglesoft.entity.TransferLog;

/**
 * Created by DingK on 2020/2/7.
 */
public interface TransferLogService {
    int save(TransferLog transferLog);

    int update(TransferLog transferLog);

    TransferLog queryLatest(Long infoId);
}
