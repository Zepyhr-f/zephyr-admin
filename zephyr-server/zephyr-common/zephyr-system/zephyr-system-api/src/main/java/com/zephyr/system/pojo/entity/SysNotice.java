package com.zephyr.system.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zephyr.mp.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知公告表
 *
 * @author zephyr
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_notice")
@Schema(description = "通知公告")
public class SysNotice extends BaseEntity {

    @Schema(description = "公告标题")
    private String noticeTitle;

    @Schema(description = "公告类型 (1=系统通知, 2=业务提醒)")
    private Integer noticeType;

    @Schema(description = "公告内容")
    private String noticeContent;

    @Schema(description = "状态 (0=正常, 1=停用)")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
