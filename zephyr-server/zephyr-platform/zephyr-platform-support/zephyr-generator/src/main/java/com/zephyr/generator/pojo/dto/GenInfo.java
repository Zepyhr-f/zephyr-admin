package com.zephyr.generator.pojo.dto;


import lombok.Data;

/**
 * 模版信息入参
 *
 * @author Zephyr
 * @since 2025-09-19
 */
@Data
public class GenInfo {
    String tableName;
    String moduleName;
}