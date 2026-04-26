package com.zephyr.core.boot.web;

import lombok.Data;

import java.util.Set;

/**
 * 用户会话信息（网关/后端解析header后存储）
 *
 * @author Zephyr
 * @since 2025-09-07
 */
@Data
public class UserSession {
    private String userCode;
    private Set<String> roleCodes;
    private String tenantCode;
}