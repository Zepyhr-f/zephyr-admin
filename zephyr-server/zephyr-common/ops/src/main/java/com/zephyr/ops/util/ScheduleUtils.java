package com.zephyr.ops.util;

import com.zephyr.ops.pojo.entity.SysJob;
import com.zephyr.ops.job.QuartzJobExecution;
import org.quartz.*;

public class ScheduleUtils {

    public static void createScheduleJob(Scheduler scheduler, SysJob job) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(QuartzJobExecution.class)
                .withIdentity(getJobKey(job))
                .build();
        jobDetail.getJobDataMap().put("TASK_PROPERTIES", job);

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job, cronScheduleBuilder);

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(job))
                .withSchedule(cronScheduleBuilder)
                .build();

        if (scheduler.checkExists(getJobKey(job))) {
            scheduler.deleteJob(getJobKey(job));
        }

        scheduler.scheduleJob(jobDetail, trigger);

        if (job.getStatus().equals(1)) {
            scheduler.pauseJob(getJobKey(job));
        }
    }

    private static JobKey getJobKey(SysJob job) {
        return JobKey.jobKey(job.getId().toString(), job.getJobGroup());
    }

    private static TriggerKey getTriggerKey(SysJob job) {
        return TriggerKey.triggerKey(job.getId().toString(), job.getJobGroup());
    }

    private static CronScheduleBuilder handleCronScheduleMisfirePolicy(SysJob job, CronScheduleBuilder cb) {
        switch (job.getMisfirePolicy()) {
            case 1: return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case 2: return cb.withMisfireHandlingInstructionFireAndProceed();
            case 3: return cb.withMisfireHandlingInstructionDoNothing();
            default: return cb;
        }
    }

    public static void pauseJob(Scheduler scheduler, SysJob job) throws SchedulerException {
        scheduler.pauseJob(getJobKey(job));
    }

    public static void resumeJob(Scheduler scheduler, SysJob job) throws SchedulerException {
        scheduler.resumeJob(getJobKey(job));
    }

    public static void runOnce(Scheduler scheduler, SysJob job) throws SchedulerException {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("TASK_PROPERTIES", job);
        scheduler.triggerJob(getJobKey(job), dataMap);
    }
}
