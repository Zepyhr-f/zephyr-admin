package com.zephyr.generator.util;


import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.zephyr.generator.pojo.entity.ZTableInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author Zephyr
 * @since 2025-09-23
 */
@Getter
public class ZConfigContext {
    private final PackageConfig packageConfig;
    private final GlobalConfig globalConfig;
    private final StrategyConfig strategyConfig;
    private final List<ZTableInfo> tableInfos = new ArrayList<>();

    public ZConfigContext(ConfigBuilder configBuilder){
        this.packageConfig = configBuilder.getPackageConfig();
        this.globalConfig = configBuilder.getGlobalConfig();
        this.strategyConfig = configBuilder.getStrategyConfig();
        initTableInfos(configBuilder);
    }

    private void initTableInfos(ConfigBuilder configBuilder){
        configBuilder.getTableInfoList()
                .forEach(ti -> {
                    ZTableInfo zTi = new ZTableInfo(configBuilder, ti.getName());
                    zTi.processTable();
                    tableInfos.add(zTi);
                });
    }
}