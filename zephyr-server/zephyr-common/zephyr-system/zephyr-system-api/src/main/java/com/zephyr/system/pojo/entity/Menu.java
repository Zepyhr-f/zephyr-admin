package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zephyr.mp.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单与权限规则实体（大一统：目录M、菜单C、按钮/API F）
 *
 * @author zephyr
 * @since 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
@Schema(description = "菜单/权限")
public class Menu extends BaseEntity {

    /** 父菜单ID（0=顶级） */
    @Schema(description = "父菜单ID（0=顶级）")
    private Long parentId;

    /** 菜单名称 */
    @Schema(description = "菜单名称")
    private String menuName;

    /** 菜单类型（M=目录 C=菜单 F=按钮/API） */
    @Schema(description = "菜单类型（M=目录 C=菜单 F=按钮/API）")
    private String menuType;

    /** 路由地址 */
    @Schema(description = "路由地址")
    private String path;

    /** 前端组件路径 */
    @Schema(description = "前端组件路径")
    private String component;

    /** 权限标识（如：sys:user:add） */
    @Schema(description = "权限标识（如：sys:user:add）")
    private String perms;

    /** 菜单图标 */
    @Schema(description = "菜单图标")
    private String icon;

    /** 显示顺序 */
    @Schema(description = "显示顺序")
    private Integer orderNum;

    /** 菜单状态（1=正常，0=停用） */
    @Schema(description = "菜单状态（1=正常，0=停用）")
    private Integer status;
}
