package com.zephyr.ops.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyr.ops.pojo.entity.SysJob;

public interface ISysJobService extends IService<SysJob> {
    void pauseJob(Long jobId);
    void resumeJob(Long jobId);
    void runOnce(Long jobId);
    void deleteJob(Long jobId);
}
