package com.zephyr.gateway.constant;

public interface GatewayConstant {
    String X_USER_CODE = "X-UserCode";
    String X_TENANT_CODE = "X-TenantCode";
    String X_ROLES = "X-Roles";
    String X_REQUEST_ID = "X-Request-Id";
    
    String X_GATEWAY_TIMESTAMP = "X-Gateway-Timestamp";
    String X_GATEWAY_NONCE = "X-Gateway-Nonce";
    String X_GATEWAY_SIGN = "X-Gateway-Sign";
    
    String REDIS_PATH_ROLES_PREFIX = "auth:path:roles:";
    String REDIS_USER_ROLES_PREFIX = "auth:user:roles:";
    String REDIS_NONCE_PREFIX = "gateway:nonce:";
    String RBAC_CHANNEL = "auth:rbac:changed";
    String USER_ROLES_CHANNEL = "auth:user:roles:changed";
}
