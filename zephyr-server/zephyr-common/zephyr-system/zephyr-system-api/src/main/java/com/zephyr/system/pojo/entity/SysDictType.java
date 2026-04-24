package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zephyr.mp.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典类型表
 *
 * @author zephyr
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("zephyr_sys_dict_type")
@Schema(description = "字典类型")
public class SysDictType extends BaseEntity {

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "状态 (0=正常, 1=停用)")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
