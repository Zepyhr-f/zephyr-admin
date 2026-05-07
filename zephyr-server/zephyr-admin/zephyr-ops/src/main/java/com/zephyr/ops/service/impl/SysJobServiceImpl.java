package com.zephyr.ops.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.ops.mapper.SysJobMapper;
import com.zephyr.ops.pojo.entity.SysJob;
import com.zephyr.ops.service.ISysJobService;
import com.zephyr.ops.util.ScheduleUtils;
import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements ISysJobService {

    private final Scheduler scheduler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(SysJob entity) {
        boolean result = super.save(entity);
        if (result) {
            try {
                ScheduleUtils.createScheduleJob(scheduler, entity);
            } catch (SchedulerException e) {
                throw new RuntimeException("调度任务失败", e);
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(SysJob entity) {
        SysJob oldJob = getById(entity.getId());
        boolean result = super.updateById(entity);
        if (result) {
            try {
                ScheduleUtils.createScheduleJob(scheduler, getById(entity.getId()));
            } catch (SchedulerException e) {
                throw new RuntimeException("修改调度任务失败", e);
            }
        }
        return result;
    }

    @Override
    public void pauseJob(Long jobId) {
        SysJob job = getById(jobId);
        job.setStatus(1);
        updateById(job);
        try {
            ScheduleUtils.pauseJob(scheduler, job);
        } catch (SchedulerException e) {
            throw new RuntimeException("暂停任务失败", e);
        }
    }

    @Override
    public void resumeJob(Long jobId) {
        SysJob job = getById(jobId);
        job.setStatus(0);
        updateById(job);
        try {
            ScheduleUtils.resumeJob(scheduler, job);
        } catch (SchedulerException e) {
            throw new RuntimeException("恢复任务失败", e);
        }
    }

    @Override
    public void runOnce(Long jobId) {
        try {
            ScheduleUtils.runOnce(scheduler, getById(jobId));
        } catch (SchedulerException e) {
            throw new RuntimeException("执行任务失败", e);
        }
    }

    @Override
    public void deleteJob(Long jobId) {
        // Implement removal from scheduler and DB
    }
}
