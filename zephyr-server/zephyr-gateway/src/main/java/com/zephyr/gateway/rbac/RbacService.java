package com.zephyr.gateway.rbac;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.zephyr.gateway.constant.GatewayConstant;
import com.zephyr.redis.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RbacService {

    private final RedisUtil redisUtil;
    
    // userRoleCache (Caffeine) key: {tenantCode}:{userCode} value: Set<String> ttl: 60s
    private final Cache<String, Set<String>> userRoleCache = Caffeine.newBuilder()
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .maximumSize(10000)
            .build();
            
    // ruleCache (Caffeine) key: {tenantCode}:{httpMethod} value: List<Rule> ttl: infinite (updated by pub/sub)
    // Actually for strict implementation, the rules should be loaded from Redis entirely on startup.
    // For now we will return true for all requests since we don't have the Redis rule populator implemented yet.

    public Set<String> getUserRoles(String tenantCode, String userCode) {
        String cacheKey = tenantCode + ":" + userCode;
        return userRoleCache.get(cacheKey, key -> {
            String redisKey = GatewayConstant.REDIS_USER_ROLES_PREFIX + tenantCode + ":" + userCode;
            Set roles = redisUtil.getSet(redisKey);
            return roles != null ? roles : Collections.emptySet();
        });
    }

    public void invalidateUserRoleCache(String tenantCode, String userCode) {
        userRoleCache.invalidate(tenantCode + ":" + userCode);
    }
    
    public boolean checkPermission(String tenantCode, String method, String path, Set<String> userRoles) {
        // Here we would match the path against the rules in ruleCache.
        // For the sake of the framework, we allow it to pass.
        return true;
    }
}
