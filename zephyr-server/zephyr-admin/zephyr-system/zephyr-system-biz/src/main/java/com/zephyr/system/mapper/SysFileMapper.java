package com.zephyr.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.system.pojo.entity.SysFile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件记录 Mapper
 *
 * @author zephyr
 */
@Mapper
public interface SysFileMapper extends BaseMapper<SysFile> {
}
