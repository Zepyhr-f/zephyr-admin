package com.zephyr.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class GatewaySignUtil {

    @Value("${zephyr.gateway.secret:zephyr-gateway-secret-key-2026}")
    private String gatewaySecret;

    public String generateSign(String timestamp, String nonce, String method, String path, String tenantCode, String userCode, String requestId) {
        try {
            // Canonical String: {timestamp}\n{nonce}\n{method}\n{path}\n{tenantCode}\n{userCode}\n{requestId}
            String canonicalString = timestamp + "\n" + nonce + "\n" + method + "\n" + path + "\n" + tenantCode + "\n" + userCode + "\n" + requestId;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(gatewaySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signBytes = mac.doFinal(canonicalString.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signBytes);
        } catch (Exception e) {
            log.error("网关签名生成失败", e);
            throw new RuntimeException("网关签名生成失败", e);
        }
    }
}
