package com.zephyr.generator.pojo.entity;


import lombok.Data;

import java.util.List;

/**
 * 表信息
 *
 * @author Zephyr
 * @since 2025-09-19
 */
@Data
public class TableModel {
    String packageName;
    String className;
    String author;
    String date;
    String comment;
    String tableName;
    List<String> baseImports;
    List<String> jarImports;
    List<TableFieldModel>  fields;
}