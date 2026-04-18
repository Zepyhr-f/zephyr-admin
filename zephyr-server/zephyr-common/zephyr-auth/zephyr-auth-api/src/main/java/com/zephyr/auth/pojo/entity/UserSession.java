package com.zephyr.auth.pojo.entity;


import lombok.Data;

/**
 * 
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