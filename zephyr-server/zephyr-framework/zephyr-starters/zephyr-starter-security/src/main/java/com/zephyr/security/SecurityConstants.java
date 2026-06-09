package com.zephyr.security;

public interface SecurityConstants {
    String USER_CODE_HEADER = "X-User-Code";
    String TENANT_CODE_HEADER = "X-Tenant-Code";
    String TIMESTAMP_HEADER = "X-Timestamp";
    String GATEWAY_SIGN_HEADER = "X-Gateway-Sign";
    String REQUEST_ID_HEADER = "X-Request-Id";
    String NONCE_HEADER = "X-Gateway-Nonce";
    String ROLES_HEADER = "X-User-Roles";
}
