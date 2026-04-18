package com.zephyr.mp.base;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 *
 * @author Zephyr
 * @since 2025-09-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseEntity implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "创建人", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", hidden = true)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "更新人", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", hidden = true)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    @TableField("del_flag")
    @TableLogic(value = "0", delval = "1")
    @Schema(description = "是否已删除", hidden = true)
    private Integer isDeleted;
}