package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.system.mapper.OperLogMapper;
import com.zephyr.system.pojo.entity.OperLog;
import com.zephyr.system.service.IOperLogService;
import org.springframework.stereotype.Service;

/**
 * 操作日志 服务实现类
 *
 * @author zephyr
 * @since 2026-04-16
 */
@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements IOperLogService {

    @Override
    public void cleanOperLog() {
        LambdaUpdateWrapper<OperLog> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(OperLog::getIfDeleted, 0)
               .set(OperLog::getIfDeleted, 1);
        this.update(wrapper);
    }
}
