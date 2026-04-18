package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.redis.util.RedisUtil;
import com.zephyr.system.mapper.SysConfigMapper;
import com.zephyr.system.pojo.entity.SysConfig;
import com.zephyr.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 参数配置 服务实现类
 *
 * @author zephyr
 */
@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    private final RedisUtil redisUtil;
    private static final String CACHE_KEY = "sys_config:";

    @Override
    public String selectConfigByKey(String configKey) {
        String key = CACHE_KEY + configKey;
        // 1. 查缓存
        String value = redisUtil.getString(key);
        if (value != null) {
            return value;
        }

        // 2. 查数据库
        SysConfig config = baseMapper.selectOne(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, configKey));

        if (config != null) {
            value = config.getConfigValue();
            // 3. 回填缓存
            redisUtil.setString(key, value, 24, TimeUnit.HOURS);
            return value;
        }

        return null;
    }

    @Override
    public void clearCache() {
        Set<String> keys = redisUtil.keys(CACHE_KEY + "*");
        if (keys != null && !keys.isEmpty()) {
            keys.forEach(redisUtil::deleteKey);
        }
    }
}
