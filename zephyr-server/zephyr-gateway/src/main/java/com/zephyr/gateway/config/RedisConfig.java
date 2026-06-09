package com.zephyr.gateway.config;

import com.zephyr.gateway.rbac.RbacCacheMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import static com.zephyr.gateway.constant.GatewayConstant.RBAC_CHANNEL;
import static com.zephyr.gateway.constant.GatewayConstant.USER_ROLES_CHANNEL;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RbacCacheMessageListener rbacCacheMessageListener;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(rbacCacheMessageListener, new PatternTopic(RBAC_CHANNEL));
        container.addMessageListener(rbacCacheMessageListener, new PatternTopic(USER_ROLES_CHANNEL));
        return container;
    }
}
