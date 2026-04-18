package com.zephyr.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyr.core.tool.api.R;
import com.zephyr.mp.support.PageQuery;
import com.zephyr.system.pojo.entity.SysConfig;
import com.zephyr.system.service.ISysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 参数配置控制器
 *
 * @author zephyr
 */
@RestController
@RequestMapping("/config")
@AllArgsConstructor
@Tag(name = "参数配置管理", description = "参数配置管理")
public class SysConfigController {

    private final ISysConfigService configService;

    @GetMapping("/page")
    @Operation(summary = "分页")
    public R<IPage<SysConfig>> page(@ParameterObject PageQuery<SysConfig> query, 
                                    @ParameterObject SysConfig config) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<SysConfig>()
                .like(StringUtils.hasText(config.getConfigName()), SysConfig::getConfigName, config.getConfigName())
                .like(StringUtils.hasText(config.getConfigKey()), SysConfig::getConfigKey, config.getConfigKey())
                .eq(config.getConfigType() != null, SysConfig::getConfigType, config.getConfigType());
        return R.data(configService.page(query.getPage(), wrapper));
    }

    @GetMapping("/key")
    @Operation(summary = "按键名获取配置")
    public R<String> getByKey(@RequestParam("configKey") String configKey) {
        return R.data(configService.selectConfigByKey(configKey));
    }

    @PostMapping("/submit")
    @Operation(summary = "新增或修改")
    public R<Boolean> submit(@Valid @RequestBody SysConfig config) {
        return R.status(configService.saveOrUpdate(config));
    }

    @PostMapping("/remove")
    @Operation(summary = "删除")
    public R<Boolean> remove(@RequestBody List<Long> ids) {
        return R.status(configService.removeByIds(ids));
    }

    @PostMapping("/clear-cache")
    @Operation(summary = "清空缓存")
    public R<Void> clearCache() {
        configService.clearCache();
        return R.success("刷新成功");
    }
}
