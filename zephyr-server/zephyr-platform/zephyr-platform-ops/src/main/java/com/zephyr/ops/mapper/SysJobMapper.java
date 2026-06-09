package com.zephyr.ops.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.ops.pojo.entity.SysJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务调度 Mapper 接口
 *
 * @author zephyr
 * @since 2026-04-16
 */
@Mapper
public interface SysJobMapper extends BaseMapper<SysJob> {
}
