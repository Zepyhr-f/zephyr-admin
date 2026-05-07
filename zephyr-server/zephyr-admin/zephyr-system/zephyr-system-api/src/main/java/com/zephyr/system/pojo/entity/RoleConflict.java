package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色互斥冲突实体
 *
 * @author zephyr
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_conflict")
@Schema(description = "角色冲突/互斥")
public class RoleConflict implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "冲突角色A ID")
    private Long roleIdA;

    @Schema(description = "冲突角色B ID")
    private Long roleIdB;
}
