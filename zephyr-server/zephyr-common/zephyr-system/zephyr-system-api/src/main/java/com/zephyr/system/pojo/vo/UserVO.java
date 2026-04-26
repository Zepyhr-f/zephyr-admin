package com.zephyr.system.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户实体VO
 *
 * @author zephyr
 * @since 2025-09-24
 */
@Data
@Schema(description = "用户")
public class UserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "员工编码")
    private String userCode;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别（0=未知 1=男 2=女）")
    private Integer sex;

    @Schema(description = "帐号状态（1=正常，0=停用）")
    private Integer status;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "密码", hidden = true)
    private String password;

    @Schema(description = "已分配角色ID列表")
    private List<String> roleCodes;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;
}
