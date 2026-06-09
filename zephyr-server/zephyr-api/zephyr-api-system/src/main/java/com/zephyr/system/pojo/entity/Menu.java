package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zephyr.mp.base.TreeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单与权限规则实体
 *
 * @author zephyr
 * @since 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("zephyr_sys_menu")
@Schema(description = "菜单/权限")
public class Menu extends TreeEntity {

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "菜单类型（M=目录 C=菜单 F=按钮/API）")
    private String menuType;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "前端组件路径")
    private String component;

    @Schema(description = "权限标识")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "菜单状态（1=正常 0=停用）")
    private Integer status;
}
