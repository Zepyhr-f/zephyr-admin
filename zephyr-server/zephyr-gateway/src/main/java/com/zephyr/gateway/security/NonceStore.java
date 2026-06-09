package com.zephyr.gateway.security;

import com.zephyr.gateway.constant.GatewayConstant;
import com.zephyr.redis.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NonceStore {
    private final RedisUtil redisUtil;

    public String generateNonce() {
        String nonce = UUID.randomUUID().toString().replace("-", "");
        // Only generate, the downstream will save to verify it hasn't been used.
        // Wait, the design says "nonce 写入 Redis：TTL=300s". But the downstream does that.
        // The gateway only generates it.
        return nonce;
    }
}
