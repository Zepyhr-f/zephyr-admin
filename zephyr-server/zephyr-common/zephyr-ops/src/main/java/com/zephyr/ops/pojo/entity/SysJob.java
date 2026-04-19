package com.zephyr.ops.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zephyr.mp.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定时任务调度对象 sys_job
 *
 * @author zephyr
 * @since 2026-04-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_job")
@Schema(description = "定时任务调度对象")
public class SysJob extends BaseEntity {

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "任务组名")
    private String jobGroup;

    @Schema(description = "调用目标字符串")
    private String invokeTarget;

    @Schema(description = "cron执行表达式")
    private String cronExpression;

    @Schema(description = "计划执行策略（1立即执行 2执行一次 3放弃执行）")
    private Integer misfirePolicy;

    @Schema(description = "是否并发执行（0禁止 1允许）")
    private Integer concurrent;

    @Schema(description = "状态（0正常 1暂停）")
    private Integer status;

    @Schema(description = "备注信息")
    private String remark;
}
