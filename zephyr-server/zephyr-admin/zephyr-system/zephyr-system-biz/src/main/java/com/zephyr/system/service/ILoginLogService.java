package com.zephyr.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyr.system.pojo.entity.LoginLog;

/**
 * 登录日志 服务类
 *
 * @author zephyr
 * @since 2026-04-16
 */
public interface ILoginLogService extends IService<LoginLog> {
    /**
     * 清空登录日志
     */
    void cleanLoginLog();
}
