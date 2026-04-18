package com.zephyr.ops.controller;

import com.zephyr.core.tool.api.R;
import com.zephyr.ops.service.CacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 缓存监控控制器
 *
 * @author zephyr
 * @since 2026-04-16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ops/cache")
@Tag(name = "系统监控", description = "Redis 缓存监控")
public class CacheController {

    private final CacheService cacheService;

    @GetMapping("/get")
    @Operation(summary = "获取 Redis 实时负载与统计信息", description = "包含内存、客户端连接数、Key 命中率等指标")
    public R<Map<String, Object>> get() {
        return R.data(cacheService.getCacheInfo());
    }
}
