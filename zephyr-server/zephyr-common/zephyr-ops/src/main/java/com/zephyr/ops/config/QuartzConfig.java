package com.zephyr.ops.config;

import com.zephyr.ops.mapper.SysJobMapper;
import com.zephyr.ops.pojo.entity.SysJob;
import com.zephyr.ops.util.ScheduleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class QuartzConfig implements CommandLineRunner {

    private final Scheduler scheduler;
    private final SysJobMapper sysJobMapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("▶ 正在初始化定时任务调度器...");
        scheduler.clear();
        List<SysJob> jobList = sysJobMapper.selectList(null);
        for (SysJob job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
        log.info("✅ 定时任务调度器初始化完成！加载任务数：{}", jobList.size());
    }
}
