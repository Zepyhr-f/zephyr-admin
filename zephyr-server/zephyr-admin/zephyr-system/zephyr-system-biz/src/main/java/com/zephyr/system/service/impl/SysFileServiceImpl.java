package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.system.mapper.SysFileMapper;
import com.zephyr.system.pojo.entity.SysFile;
import com.zephyr.system.service.ISysFileService;
import org.springframework.stereotype.Service;

/**
 * 文件记录 服务实现类
 *
 * @author zephyr
 */
@Service
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements ISysFileService {
}
