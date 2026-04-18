package com.zephyr.ops.controller;

import com.zephyr.core.tool.api.R;
import com.zephyr.ops.pojo.vo.ServerVO;
import com.zephyr.ops.service.MonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务器监控控制器
 *
 * @author zephyr
 * @since 2026-04-16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ops/monitor")
@Tag(name = "服务监控", description = "实时服务器指标监控")
public class MonitorController {

    private final MonitorService monitorService;

    @GetMapping("/server")
    @Operation(summary = "获取服务器实时指标", description = "包含 CPU、内存、JVM、磁盘、系统信息")
    public R<ServerVO> server() {
        return R.data(monitorService.getServerInfo());
    }
}
