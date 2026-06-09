package com.zephyr.ops.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务调度日志对象
 *
 * @author zephyr
 * @since 2026-04-16
 */
@Data
@TableName("zephyr_sys_job_log")
@Schema(description = "定时任务调度日志对象")
public class SysJobLog implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "日志ID")
    private Long id;

    @Schema(description = "任务名称")
    private String jobName;

    @Schema(description = "任务组名")
    private String jobGroup;

    @Schema(description = "调用目标字符串")
    private String invokeTarget;

    @Schema(description = "日志信息")
    private String jobMessage;

    @Schema(description = "执行状态（0正常 1失败）")
    private Integer status;

    @Schema(description = "异常信息")
    private String exceptionInfo;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "消耗时间(ms)")
    private Long costTime;
}
