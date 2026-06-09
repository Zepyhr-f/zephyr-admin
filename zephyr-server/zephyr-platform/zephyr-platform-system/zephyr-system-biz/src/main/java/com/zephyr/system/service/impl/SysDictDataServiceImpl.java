package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.redis.util.RedisUtil;
import com.zephyr.system.mapper.SysDictDataMapper;
import com.zephyr.system.pojo.entity.SysDictData;
import com.zephyr.system.service.ISysDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 字典数据 服务实现类
 *
 * @author zephyr
 */
@Service
@RequiredArgsConstructor
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataService {

    private final RedisUtil redisUtil;
    private static final String CACHE_KEY = "sys_dict:";

    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        String key = CACHE_KEY + dictType;
        // 尝试从缓存获取
        SysDictData[] cachedArray = redisUtil.getObject(key, SysDictData[].class);
        if (cachedArray != null && cachedArray.length > 0) {
            return Arrays.asList(cachedArray);
        }

        // 缓存未命中，查数据库
        List<SysDictData> list = baseMapper.selectList(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getStatus, 0)
                .orderByAsc(SysDictData::getDictSort));

        // 存入缓存 (24小时)
        if (!list.isEmpty()) {
            redisUtil.setObject(key, list, 24, TimeUnit.HOURS);
        }

        return list;
    }
}
