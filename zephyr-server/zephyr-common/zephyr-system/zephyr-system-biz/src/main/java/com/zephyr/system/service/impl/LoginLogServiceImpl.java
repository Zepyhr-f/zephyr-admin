package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.system.mapper.LoginLogMapper;
import com.zephyr.system.pojo.entity.LoginLog;
import com.zephyr.system.service.ILoginLogService;
import org.springframework.stereotype.Service;

/**
 * 登录日志 服务实现类
 *
 * @author zephyr
 * @since 2026-04-16
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {

    @Override
    public void cleanLoginLog() {
        // 逻辑清理：将所有正常数据的 isDeleted 设置为 1
        LambdaUpdateWrapper<LoginLog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(LoginLog::getIfDeleted, 0)
               .set(LoginLog::getIfDeleted, 1);
        this.update(wrapper);
    }
}
