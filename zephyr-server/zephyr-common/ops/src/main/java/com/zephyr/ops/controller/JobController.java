package com.zephyr.ops.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zephyr.core.tool.api.R;
import com.zephyr.ops.pojo.entity.SysJob;
import com.zephyr.ops.service.ISysJobService;
import com.zephyr.mp.support.PageQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ops/job")
@Tag(name = "任务调度", description = "Quartz 定时任务管理")
public class JobController {

    private final ISysJobService sysJobService;

    @GetMapping("/list")
    @Operation(summary = "分页查询任务列表")
    public R<Page<SysJob>> list(SysJob job, PageQuery<SysJob> query) {
        LambdaQueryWrapper<SysJob> wrapper = new LambdaQueryWrapper<>();
        if (job.getJobName() != null) {
            wrapper.like(SysJob::getJobName, job.getJobName());
        }
        return R.data(sysJobService.page(query.getPage(), wrapper));
    }

    @PostMapping("/add")
    @Operation(summary = "新增定时任务")
    public R<Boolean> add(@RequestBody SysJob job) {
        return R.status(sysJobService.save(job));
    }

    @PutMapping("/edit")
    @Operation(summary = "修改定时任务")
    public R<Boolean> edit(@RequestBody SysJob job) {
        return R.status(sysJobService.updateById(job));
    }

    @PutMapping("/status")
    @Operation(summary = "切换任务状态", description = "0: 正常, 1: 暂停")
    public R<Void> updateStatus(@RequestBody SysJob job) {
        if (job.getStatus().equals(0)) {
            sysJobService.resumeJob(job.getId());
        } else {
            sysJobService.pauseJob(job.getId());
        }
        return R.success("状态更新成功");
    }

    @PostMapping("/run")
    @Operation(summary = "立即执行一次任务")
    public R<Void> run(@RequestBody SysJob job) {
        sysJobService.runOnce(job.getId());
        return R.success("执行指令下发成功");
    }

    @DeleteMapping("/remove/{id}")
    @Operation(summary = "删除任务")
    public R<Boolean> remove(@PathVariable Long id) {
        // ... Logic for deletion
        return R.status(sysJobService.removeById(id));
    }
}
