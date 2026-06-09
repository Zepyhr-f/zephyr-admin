package com.zephyr.core.boot.feign;

import com.zephyr.core.boot.web.UserContextHolder;
import com.zephyr.core.boot.web.UserSession;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.zephyr.core.tool.constant.WebConstants.*;

/**
 * Feign 调用时自动透传用户上下文 Header
 */
@Component
public class FeignContextInterceptor implements RequestInterceptor {

    @Value("${zephyr.gateway.secret:zephyr-default-secret}")
    private String gatewaySecret;

    @Override
    public void apply(RequestTemplate template) {
        UserSession session = UserContextHolder.get();
        String userCode = "";
        String tenantCode = "";
        
        if (session != null) {
            userCode = session.getUserCode() == null ? "" : session.getUserCode();
            tenantCode = session.getTenantCode() == null ? "" : session.getTenantCode();
        }
        
        // Always generate signature for Feign calls so the target service accepts it
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = java.util.UUID.randomUUID().toString().replace("-", "");
        String requestId = java.util.UUID.randomUUID().toString().replace("-", "");
        String sign = generateSign(timestamp, nonce, tenantCode, userCode, requestId);

        template.header(USER_CODE_HEADER, userCode);
        template.header(TENANT_CODE_HEADER, tenantCode);
        template.header(TIMESTAMP_HEADER, timestamp);
        template.header("X-Gateway-Nonce", nonce);
        template.header("X-Request-Id", requestId);
        template.header(GATEWAY_SIGN_HEADER, sign);
    }

    private String generateSign(String timestamp, String nonce, String tenantCode, String userCode, String requestId) {
        try {
            String canonicalString = timestamp + "\n" + nonce + "\n" + tenantCode + "\n" + userCode + "\n" + requestId;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(gatewaySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getEncoder().encodeToString(mac.doFinal(canonicalString.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("Feign 签名生成失败", e);
        }
    }
}
