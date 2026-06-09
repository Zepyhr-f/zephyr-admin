package com.zephyr.security;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserContext {
    private String userCode;
    private String tenantCode;
    private List<String> roles;
    private String requestId;
}
