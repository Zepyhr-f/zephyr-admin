package com.zephyr.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.core.tool.api.R;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.system.pojo.entity.OperLog;
import com.zephyr.system.service.IOperLogService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志控制器
 *
 * @author zephyr
 * @since 2026-04-16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/oper-log")
@Tag(name = "操作日志", description = "操作日志管理")
public class OperLogController {

    private final IOperLogService operLogService;

    @GetMapping("/list")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "查询操作日志列表", description = "支持按模块标题、操作账号搜索")
    public R<IPage<OperLog>> list(OperLog operLog, PageQuery<OperLog> query) {
        LambdaQueryWrapper<OperLog> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(operLog.getTitle())) {
            wrapper.like(OperLog::getTitle, operLog.getTitle());
        }
        if (StringUtils.isNotBlank(operLog.getOperName())) {
            wrapper.like(OperLog::getOperName, operLog.getOperName());
        }
        if (operLog.getStatus() != null) {
            wrapper.eq(OperLog::getStatus, operLog.getStatus());
        }
        // 默认按操作时间倒序
        wrapper.orderByDesc(OperLog::getOperTime);
        return R.data(operLogService.page(query.getPage(), wrapper));
    }

    @PostMapping("/remove")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "删除操作日志", description = "传入ID集合")
    public R<Boolean> remove(@RequestBody List<Long> ids) {
        return R.status(operLogService.removeByIds(ids));
    }

    @PostMapping("/clean")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "清空操作日志", description = "一键清理所有日志")
    public R<Boolean> clean() {
        operLogService.cleanOperLog();
        return R.success("操作日志已清空");
    }
}
