package com.zephyr.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.core.tool.api.R;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.system.pojo.entity.LoginLog;
import com.zephyr.system.service.ILoginLogService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登录日志控制器
 *
 * @author zephyr
 * @since 2026-04-16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/login-log")
@Tag(name = "登录日志", description = "登录日志管理")
public class LoginLogController {

    private final ILoginLogService loginLogService;

    @GetMapping("/list")
    @ApiOperationSupport(order = 1)
    @Operation(summary = "查询登录日志列表", description = "支持按账号、状态模糊查询")
    public R<IPage<LoginLog>> list(LoginLog loginLog, PageQuery<LoginLog> query) {
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(loginLog.getUsername())) {
            wrapper.like(LoginLog::getUsername, loginLog.getUsername());
        }
        if (loginLog.getStatus() != null) {
            wrapper.eq(LoginLog::getStatus, loginLog.getStatus());
        }
        // 默认按登录时间倒序
        wrapper.orderByDesc(LoginLog::getLoginTime);
        return R.data(loginLogService.page(query.getPage(), wrapper));
    }

    @PostMapping("/remove")
    @ApiOperationSupport(order = 2)
    @Operation(summary = "删除登录日志", description = "传入ID集合")
    public R<Boolean> remove(@RequestBody List<Long> ids) {
        return R.status(loginLogService.removeByIds(ids));
    }

    @PostMapping("/clean")
    @ApiOperationSupport(order = 3)
    @Operation(summary = "清空登录日志", description = "一键清理所有日志")
    public R<Boolean> clean() {
        loginLogService.cleanLoginLog();
        return R.success("日志清空成功");
    }
}
