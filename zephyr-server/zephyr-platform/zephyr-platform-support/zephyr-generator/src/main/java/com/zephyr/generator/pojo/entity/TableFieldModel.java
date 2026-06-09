package com.zephyr.generator.pojo.entity;

import lombok.Data;

/**
 * 表字段信息
 *
 * @author Zephyr
 * @since 2025-09-19
 */
@Data
public class TableFieldModel {
    private String name;
    private String type;
    private String comment;

    /** 是否需要序列化为字符串（Long/BigInteger/BigDecimal） */
    private boolean serializeToString;
    private String dateFormat;
}