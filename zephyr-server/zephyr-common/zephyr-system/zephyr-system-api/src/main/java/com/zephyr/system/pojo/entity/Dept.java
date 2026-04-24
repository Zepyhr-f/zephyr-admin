package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zephyr.mp.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门信息实体
 *
 * @author zephyr
 * @since 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("zephyr_sys_dept")
@Schema(description = "部门")
public class Dept extends BaseEntity {

    @Schema(description = "部门编码")
    private String deptCode;

    @Schema(description = "父部门编码")
    private String parentCode;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "部门全称")
    private String fullName;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "部门状态（1=正常 0=停用）")
    private Integer status;
}
