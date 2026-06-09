package com.zephyr.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.system.pojo.entity.SysConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 参数配置 Mapper
 *
 * @author zephyr
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {
}
