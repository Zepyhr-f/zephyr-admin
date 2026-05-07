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
* 菜单权限实体VO
*
* @author zephyr
* @since 2025-09-24
*/
@Data
@Schema(description = "菜单/权限")
public class MenuVO implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "菜单编码")
    private String code;

    @Schema(description = "父菜单编码")
    private String parentCode;

    @Schema(description = "是否叶子节点 (1=是 0=否)")
    private Integer leaf;

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "菜单类型（M目录 C菜单 F按钮/API）")
    private String menuType;

    @Schema(description = "")
    private String path;

    @Schema(description = "")
    private String component;

    @Schema(description = "权限标识")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "菜单状态（1=正常，0=停用）")
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "子菜单列表")
    private List<MenuVO> children;
}
