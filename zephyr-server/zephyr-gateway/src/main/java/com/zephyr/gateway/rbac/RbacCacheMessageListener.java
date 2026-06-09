package com.zephyr.gateway.rbac;

import com.zephyr.gateway.constant.GatewayConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RbacCacheMessageListener implements MessageListener {

    private final RbacService rbacService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());

        log.info("Received redis pub/sub message. Channel: {}, Body: {}", channel, body);

        if (GatewayConstant.USER_ROLES_CHANNEL.equals(channel)) {
            // body format: {tenantCode}:{userCode}
            String[] parts = body.split(":");
            if (parts.length == 2) {
                rbacService.invalidateUserRoleCache(parts[0], parts[1]);
            }
        } else if (GatewayConstant.RBAC_CHANNEL.equals(channel)) {
            // body format: RBAC_RULE_UPDATED or something.
            // For now we just invalidate all or reload.
            // In a real implementation this would trigger a reload or incremental update.
            log.info("RBAC rules updated, should reload cache");
        }
    }
}
