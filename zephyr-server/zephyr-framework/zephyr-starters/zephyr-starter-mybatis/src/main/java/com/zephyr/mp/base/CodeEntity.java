package com.zephyr.mp.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务实体类（存在唯一编码）
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CodeEntity extends BaseEntity{

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "唯一编码", hidden = true)
    private String code;

}
