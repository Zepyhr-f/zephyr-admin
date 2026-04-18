package com.zephyr.generator.config;


import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.zephyr.generator.pojo.dto.GenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 配置构造工厂
 *
 * @author Zephyr
 * @since 2025-09-19
 */
@Component
@RequiredArgsConstructor
public class ConfigBuilderFactory {
    private final GeneratorConfig generatorConfig;

    public ConfigBuilder createConfigBuilder(GenInfo genInfo) {
        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(
                generatorConfig.getDatasource().getUrl(),
                generatorConfig.getDatasource().getUsername(),
                generatorConfig.getDatasource().getPassword()
        ).build();

        // 包配置
        PackageConfig packageConfig = new PackageConfig.Builder()
                .parent(generatorConfig.getOutput().getPackageName())
                .moduleName(genInfo.getModuleName())
                .build();

        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig.Builder()
                .addInclude(genInfo.getTableName())
                .addTablePrefix(generatorConfig.getStrategy().getPrefix())
                .build();

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig.Builder()
                .author(generatorConfig.getStrategy().getAuthor())
                .outputDir(generatorConfig.getOutput().getBaseDir())
                .disableOpenDir()
                .build();

        return new ConfigBuilder(
                packageConfig,
                dataSourceConfig,
                strategyConfig,
                null,
                globalConfig,
                null
        );
    }
}