package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.redis.util.RedisUtil;
import com.zephyr.system.mapper.SysDictTypeMapper;
import com.zephyr.system.pojo.entity.SysDictType;
import com.zephyr.system.service.ISysDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Set;

/**
 * 字典类型 服务实现类
 *
 * @author zephyr
 */
@Service
@RequiredArgsConstructor
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    private final RedisUtil redisUtil;
    private static final String CACHE_KEY_PREFIX = "sys_dict:*";

    @Override
    public void clearCache() {
        Set<String> keys = redisUtil.keys(CACHE_KEY_PREFIX);
        if (keys != null && !keys.isEmpty()) {
            keys.forEach(redisUtil::deleteKey);
        }
    }
}
