package com.zephyr.auth.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "token返回")
public class LoginResponse {
    @Schema(description = "token值")
    private String token;
    @Schema(description = "刷新token")
    private String refreshToken;
    @Schema(description = "用户Id")
    private String userId;
    @Schema(description = "返回消息")
    private String message;
    @Builder.Default
    @Schema(description = "token类型")
    private String type = "Bearer";
}
