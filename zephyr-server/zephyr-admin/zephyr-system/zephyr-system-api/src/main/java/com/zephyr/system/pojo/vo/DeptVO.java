package com.zephyr.system.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 部门VO
 *
 * @author zephyr
 */
@Data
@Schema(description = "部门")
public class DeptVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "部门编码")
    private String deptCode;

    @Schema(description = "父部门编码")
    private String parentCode;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Schema(description = "部门状态（1=正常，0=停用）")
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private java.util.Date createTime;

    @Schema(description = "子部门列表")
    private List<DeptVO> children;
}
