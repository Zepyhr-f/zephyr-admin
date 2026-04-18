package com.zephyr.system.pojo.dto;

import com.zephyr.system.pojo.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色 DTO
 *
 * @author zephyr
 * @since 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色传输对象")
public class RoleDTO extends Role {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID集合
     */
    @Schema(description = "菜单ID集合")
    private List<Long> menuIds;

}
