package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zephyr.mp.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色信息实体
 *
 * @author zephyr
 * @since 2025-09-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
@Schema(description = "角色")
public class Role extends BaseEntity {

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "角色状态（1=正常 0=停用）")
    private Integer status;
}
