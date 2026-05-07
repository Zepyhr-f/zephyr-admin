package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zephyr.mp.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户信息实体
 *
 * @author zephyr
 * @since 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户")
@TableName("zephyr_sys_user")
public class User extends BaseEntity {

    @Schema(description = "员工编码")
    private String userCode;

    @Schema(description = "昵称")
    private String userName;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别（0=男 1=女）")
    private Integer sex;

    @Schema(description = "出生日期")
    private Date birthday;

    @Schema(description = "用户类型（0=员工 1=管理员 2=系统）")
    private Integer userType;

    @Schema(description = "帐号状态（1=正常 0=停用）")
    private Integer status;

    @Schema(description = "所属部门编码")
    private String deptCode;

    @Schema(description = "所属岗位编码")
    private String postCode;
}
