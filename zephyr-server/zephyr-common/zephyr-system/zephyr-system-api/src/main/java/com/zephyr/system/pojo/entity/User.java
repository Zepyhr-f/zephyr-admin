package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zephyr.mp.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息实体
 *
 * @author zephyr
 * @since 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户")
@TableName("sys_user")
public class User extends BaseEntity {

    /** 部门ID */
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "部门ID")
    private Long deptId;

    /** 用户名 */
    @Schema(description = "用户名")
    private String username;

    /** 真实姓名 */
    @Schema(description = "真实姓名")
    private String realName;

    /** 密码（BCrypt 加密存储） */
    @Schema(description = "密码")
    private String password;

    /** 邮箱 */
    @Schema(description = "邮箱")
    private String email;

    /** 手机号 */
    @Schema(description = "手机号")
    private String phone;

    /** 性别（0=未知，1=男，2=女） */
    @Schema(description = "性别（0=未知，1=男，2=女）")
    private Integer sex;

    /** 帐号状态（1=正常，0=停用） */
    @Schema(description = "帐号状态（1=正常，0=停用）")
    private Integer status;
}
