package com.zephyr.auth.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.zephyr.system.pojo.vo.UserVO;
import java.util.List;

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
    @Schema(description = "用户信息")
    private UserVO user;
    @Schema(description = "角色编码列表")
    private List<String> roles;
    @Schema(description = "权限标识列表")
    private List<String> buttons;
    @Schema(description = "返回消息")
    private String message;
    @Builder.Default
    @Schema(description = "token类型")
    private String type = "Bearer";
}
