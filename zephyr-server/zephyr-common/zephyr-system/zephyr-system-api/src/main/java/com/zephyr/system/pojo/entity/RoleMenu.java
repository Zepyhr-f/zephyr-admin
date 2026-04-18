package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色和菜单关联实体
 *
 * @author zephyr
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_menu")
@Schema(description = "角色与菜单关联")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "菜单ID")
    private Long menuId;
}
