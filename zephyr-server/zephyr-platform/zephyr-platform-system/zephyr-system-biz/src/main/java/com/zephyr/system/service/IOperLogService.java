package com.zephyr.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyr.system.pojo.entity.OperLog;

/**
 * 操作日志 服务类
 *
 * @author zephyr
 * @since 2026-04-16
 */
public interface IOperLogService extends IService<OperLog> {
    /**
     * 清空操作日志
     */
    void cleanOperLog();
}
