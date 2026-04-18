package com.zephyr.generator.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("gen_table_column")
public class GenTableColumn implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long columnId;

    private Long tableId;
    private String columnName;
    private String columnComment;
    private String columnType;
    private String javaType;
    private String javaField;
    private String isPk;
    private String isIncrement;
    private String isRequired;
    private String isInsert;
    private String isEdit;
    private String isList;
    private String isQuery;
    private String queryType;
    private String htmlType;
    private String dictType;
    private Integer sort;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
}
