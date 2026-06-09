package com.zephyr.redis.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 *
 * @author Zephyr
 * @since 2025-09-07
 */
@Component
public class RedisUtil {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisUtil(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    // ==================== String ====================
    /** 存字符串 */
    public void setString(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /** 存字符串（不过期） */
    public void setString(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /** 存对象（自动序列化为 JSON） */
    public void setObject(String key, Object obj, long timeout, TimeUnit unit) {
        try {
            String json = objectMapper.writeValueAsString(obj);
            redisTemplate.opsForValue().set(key, json, timeout, unit);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化失败", e);
        }
    }

    /** 取字符串 */
    public String getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /** 取对象（自动反序列化 JSON） */
    public <T> T getObject(String key, Class<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        if (json == null)
            return null;
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("反序列化失败", e);
        }
    }

    /** 删除 */
    public Boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }

    // ==================== Hash ====================

    /** 存 Hash */
    public void hSet(String key, String field, Object value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForHash().put(key, field, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化失败", e);
        }
    }

    /** 取 Hash 的单个字段 */
    public <T> T hGet(String key, String field, Class<T> clazz) {
        Object val = redisTemplate.opsForHash().get(key, field);
        if (val == null)
            return null;
        try {
            return objectMapper.readValue(val.toString(), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("反序列化失败", e);
        }
    }

    /** 获取整个 Hash（反序列化为 Map<String, Object>） */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /** 删除 Hash 的字段 */
    public void hDel(String key, Object... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    // ==================== Set ====================

    /** 添加一个或多个权限到用户集合，并设置过期时间 */
    public void addSet(String key, long timeout, TimeUnit unit, String... values) {
        redisTemplate.opsForSet().add(key, values);
        if (timeout > 0) {
            redisTemplate.expire(key, timeout, unit);
        }
    }

    /**
     * 添加整个Set
     */
    public void addSet(String key, Set<String> values) {
        redisTemplate.opsForSet().add(key, values.toArray(new String[0]));
    }

    /** 移除集合中的一个或多个权限 */
    public void removeSet(String key, String... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    /** 判断集合中是否包含某个权限 */
    public boolean isSetMember(String key, String value) {
        Boolean result = redisTemplate.opsForSet().isMember(key, value);
        return result != null && result;
    }

    /** 获取集合中所有权限 */
    public Set<String> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /** 设置集合过期时间 */
    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    /** 获取匹配的 key 集合 */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }
}