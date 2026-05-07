package com.zephyr.core.tool.constant;

/**
 * web 常量
 *
 * @author Zephyr
 * @since 2025-09-19
 */
public interface WebConstants {
    String USER_CODE_HEADER = "X-User-Code";
    String TENANT_CODE_HEADER = "X-Tenant-Code";
    String TIMESTAMP_HEADER = "X-Timestamp";
    String GATEWAY_SIGN_HEADER = "X-Gateway-Sign";
    String REQUEST_ID_HEADER = "X-Request-Id";
    String CLIENT_TYPE_HEADER = "X-Client-Type";

    // 旧常量已废弃，保留向下兼容（后续可删除）
    @Deprecated
    String ROLE_CODE_HEADER = "X-User-roleCode";
    @Deprecated
    String USER_INFO_HEADER = "X-User-Info";
}