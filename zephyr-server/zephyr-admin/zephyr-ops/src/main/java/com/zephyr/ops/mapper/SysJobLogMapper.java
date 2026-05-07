package com.zephyr.ops.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.ops.pojo.entity.SysJobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务调度日志 Mapper 接口
 *
 * @author zephyr
 * @since 2026-04-16
 */
@Mapper
public interface SysJobLogMapper extends BaseMapper<SysJobLog> {
}
