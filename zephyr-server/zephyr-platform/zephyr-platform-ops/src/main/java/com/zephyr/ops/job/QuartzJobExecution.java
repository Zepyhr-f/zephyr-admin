package com.zephyr.ops.job;

import com.zephyr.ops.pojo.entity.SysJob;
import com.zephyr.ops.pojo.entity.SysJobLog;
import com.zephyr.ops.mapper.SysJobLogMapper;
import com.zephyr.ops.util.JobInvokeUtil;
import com.zephyr.core.tool.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

@Slf4j
public class QuartzJobExecution extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) {
        SysJob sysJob = (SysJob) context.getMergedJobDataMap().get("TASK_PROPERTIES");
        SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setJobName(sysJob.getJobName());
        sysJobLog.setJobGroup(sysJob.getJobGroup());
        sysJobLog.setInvokeTarget(sysJob.getInvokeTarget());
        sysJobLog.setCreateTime(new Date());

        long startTime = System.currentTimeMillis();
        try {
            JobInvokeUtil.invokeMethod(sysJob.getInvokeTarget());
            sysJobLog.setStatus(0);
            sysJobLog.setJobMessage("执行成功");
        } catch (Exception e) {
            log.error("任务执行异常 - ：", e);
            sysJobLog.setStatus(1);
            sysJobLog.setExceptionInfo(e.getMessage());
            sysJobLog.setJobMessage("执行失败");
        } finally {
            sysJobLog.setCostTime(System.currentTimeMillis() - startTime);
            // 异步或直接通过 SpringUtil 获取 Mapper 记录日志
            SpringUtil.getBean(SysJobLogMapper.class).insert(sysJobLog);
        }
    }
}
