package com.zephyr.core.boot.web;

import lombok.Data;

import java.util.List;

/**
 * 用户会话信息
 *
 * @author Zephyr
 * @since 2025-09-07
 */
@Data
public class UserSession {
    private String userCode;
    private String username;
    private List<String> roleCodes;
    private String tenantCode;
}