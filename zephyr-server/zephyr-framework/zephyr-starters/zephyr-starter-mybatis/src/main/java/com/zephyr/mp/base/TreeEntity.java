package com.zephyr.mp.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 树级实体类
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TreeEntity extends CodeEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "父编码", hidden = true)
    private String parentCode;

    @Schema(description = "是否叶子节点 (1=是 0=否)", hidden = true)
    private Integer leaf;

}
