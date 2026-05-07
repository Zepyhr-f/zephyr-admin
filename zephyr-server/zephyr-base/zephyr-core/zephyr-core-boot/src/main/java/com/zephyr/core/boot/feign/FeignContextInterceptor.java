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
        if (session == null || session.getUserCode() == null || "-1".equals(session.getUserCode())) {
            return;
        }

        String userCode = session.getUserCode();
        String tenantCode = session.getTenantCode();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String sign = generateSign(userCode, tenantCode, timestamp);

        template.header(USER_CODE_HEADER, userCode);
        if (tenantCode != null) {
            template.header(TENANT_CODE_HEADER, tenantCode);
        }
        template.header(TIMESTAMP_HEADER, timestamp);
        template.header(GATEWAY_SIGN_HEADER, sign);
    }

    private String generateSign(String userCode, String tenantCode, String timestamp) {
        try {
            String data = String.valueOf(userCode) + String.valueOf(tenantCode) + timestamp;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(gatewaySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("Feign 签名生成失败", e);
        }
    }
}
