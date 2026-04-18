package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.system.mapper.SysNoticeMapper;
import com.zephyr.system.pojo.entity.SysNotice;
import com.zephyr.system.service.ISysNoticeService;
import org.springframework.stereotype.Service;

/**
 * 通知公告 服务实现类
 *
 * @author zephyr
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {
}
