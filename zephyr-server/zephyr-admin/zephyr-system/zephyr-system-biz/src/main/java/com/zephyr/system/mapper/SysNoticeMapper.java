package com.zephyr.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyr.system.pojo.entity.SysNotice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知公告 Mapper
 *
 * @author zephyr
 */
@Mapper
public interface SysNoticeMapper extends BaseMapper<SysNotice> {
}
