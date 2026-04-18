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

    /** 角色名称 */
    @Schema(description = "角色名称")
    private String roleName;

    /** 角色权限标识（如：ROLE_ADMIN） */
    @Schema(description = "角色权限标识（如：ROLE_ADMIN）")
    private String roleCode;

    /** 显示顺序 */
    @Schema(description = "显示顺序")
    private Integer roleSort;

    /**
     * 数据范围
     * 1=全部数据权限  2=自定义数据权限
     * 3=本部门数据权限  4=本部门及以下  5=仅本人
     */
    @Schema(description = "数据范围（1~5）")
    private Integer dataScope;

    /** 角色状态（1=正常，0=停用） */
    @Schema(description = "角色状态（1=正常，0=停用）")
    private Integer status;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
