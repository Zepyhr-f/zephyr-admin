package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色继承关系实体
 *
 * @author zephyr
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_hierarchy")
@Schema(description = "角色继承关系")
public class RoleHierarchy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "父角色ID")
    private Long parentRoleId;

    @Schema(description = "子角色ID")
    private Long childRoleId;
}
