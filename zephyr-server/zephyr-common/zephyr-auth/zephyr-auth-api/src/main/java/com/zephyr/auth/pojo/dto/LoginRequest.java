package com.zephyr.auth.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录参数")
public class LoginRequest {
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "用户密码")
    private String password;
}
