package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色和部门关联实体（用于自定义数据权限）
 *
 * @author zephyr
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_dept")
@Schema(description = "角色与部门关联")
public class RoleDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "部门ID")
    private Long deptId;
}
