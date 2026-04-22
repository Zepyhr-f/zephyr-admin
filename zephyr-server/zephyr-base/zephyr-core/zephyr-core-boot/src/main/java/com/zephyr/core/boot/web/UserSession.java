package com.zephyr.core.boot.web;

import lombok.Data;

/**
 * 用户会话信息
 *
 * @author Zephyr
 * @since 2025-09-07
 */
@Data
public class UserSession {
    private Long userId;
    private String username;
    private Long roleId;
}