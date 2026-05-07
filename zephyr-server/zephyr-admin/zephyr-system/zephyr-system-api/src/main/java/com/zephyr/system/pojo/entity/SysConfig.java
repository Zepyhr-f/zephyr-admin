package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zephyr.mp.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 参数配置表
 *
 * @author zephyr
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("zephyr_sys_config")
@Schema(description = "参数配置")
public class SysConfig extends BaseEntity {

    @Schema(description = "参数名称")
    private String configName;

    @Schema(description = "参数键名")
    private String configKey;

    @Schema(description = "参数键值")
    private String configValue;

    @Schema(description = "系统内置 (1=是, 0=否)")
    private Integer configType;

    @Schema(description = "备注")
    private String remark;
}
