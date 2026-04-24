package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zephyr.mp.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 登录日志实体
 *
 * @author zephyr
 * @since 2026-04-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "登录日志")
@TableName("zephyr_sys_login_log")
public class LoginLog extends BaseEntity {

    @Schema(description = "用户账号")
    private String username;

    @Schema(description = "登录IP地址")
    private String ipaddr;

    @Schema(description = "登录地点")
    private String loginLocation;

    @Schema(description = "浏览器类型")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "登录状态（0=成功 1=失败）")
    private Integer status;

    @Schema(description = "提示消息")
    private String msg;

    @Schema(description = "访问时间")
    private LocalDateTime loginTime;
}
