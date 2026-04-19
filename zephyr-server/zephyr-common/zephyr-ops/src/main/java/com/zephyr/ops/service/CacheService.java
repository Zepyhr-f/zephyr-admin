package com.zephyr.ops.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class CacheService {

    private final StringRedisTemplate redisTemplate;

    public Map<String, Object> getCacheInfo() {
        Properties info = redisTemplate.execute((RedisCallback<Properties>) RedisConnection::info);
        Long dbSize = redisTemplate.execute((RedisCallback<Long>) RedisConnection::dbSize);
        Properties commandStats = redisTemplate.execute((RedisCallback<Properties>) connection -> connection.info("commandstats"));

        Map<String, Object> result = new HashMap<>();
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        if (commandStats != null) {
            commandStats.stringPropertyNames().forEach(key -> {
                Map<String, String> data = new HashMap<>();
                String property = commandStats.getProperty(key);
                data.put("name", key.replace("cmdstat_", ""));
                data.put("value", property.split(",")[0].split("=")[1]);
                pieList.add(data);
            });
        }
        result.put("commandStats", pieList);
        return result;
    }
}
