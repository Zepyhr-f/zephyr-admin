package com.zephyr.generator.service.impl;


import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.zephyr.generator.config.ConfigBuilderFactory;
import com.zephyr.generator.exception.TemplateGenerationException;
import com.zephyr.generator.pojo.dto.GenInfo;
import com.zephyr.generator.pojo.entity.TableModel;
import com.zephyr.generator.service.IGeneratorService;
import com.zephyr.generator.util.TemplateUtils;
import com.zephyr.generator.util.ZConfigContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;


/**
 * 代码生成服务实现
 *
 * @author Zephyr
 * @since 2025-09-19
 */
@Slf4j
@Service
@AllArgsConstructor
public class GeneratorServiceImpl implements IGeneratorService {

    private final ConfigBuilderFactory configBuilderFactory;

    public String generateCode(GenInfo genInfo) {
        try {
            log.info("开始生成代码，表名: {}", genInfo.getTableName());

            ConfigBuilder configBuilder = configBuilderFactory.createConfigBuilder(genInfo);
            ZConfigContext zConfigContext = new ZConfigContext(configBuilder);

            // 构建模板上下文
            TableModel tableModel = TemplateUtils.buildTemplateContext(zConfigContext);

            // 获取所有需要生成的模板文件路径
            Map<String, String> templateOutputPaths = TemplateUtils.getTemplateOutputPaths(zConfigContext);

            // 生成所有模板文件
            generateAllTemplates(tableModel, templateOutputPaths);

            log.info("代码生成完成，表名: {}", genInfo.getTableName());

            return configBuilder.getGlobalConfig().getOutputDir();
        } catch (Exception e) {
            log.error("代码生成失败，表名: {}", genInfo.getTableName(), e);
            throw new RuntimeException("代码生成失败: " + e.getMessage(), e);
        }
    }

    /**
     * 生成所有模板文件
     * @param tableModel 模版数据
     * @param templateOutputPaths 模版文件路径映射
     */
    private void generateAllTemplates(TableModel tableModel, Map<String, String> templateOutputPaths) {
        templateOutputPaths.forEach((templateName, outputFilePath) -> {
            try{
                generateTemplate(templateName,tableModel,outputFilePath);
            } catch (TemplateGenerationException e) {
                // 记录失败但继续执行其他模板
                log.error("模板生成失败: {}", templateName, e);
            } catch (Exception e) {
                throw new RuntimeException("代码生成过程中发生严重错误", e);
            }

        });
    }

    /**
     * 生成单个模板文件
     * @param templateName 模版名称
     * @param tableModel 模版数据
     * @param outputFilePath 输出文件路径
     */
    private void generateTemplate(String templateName, TableModel tableModel, String outputFilePath) {
        File outputFile = new File(outputFilePath);
        TemplateUtils.createDirectories(outputFile.getParentFile());
        try (FileWriter writer = new FileWriter(outputFile)) {
            Configuration config = TemplateUtils.createFreemarkerConfig();
            Template template = config.getTemplate(templateName);
            template.process(tableModel, writer);
            log.debug("成功生成文件: {}", outputFilePath);
        } catch (IOException e) {
            throw new TemplateGenerationException("文件IO错误: " + templateName, e);
        } catch (TemplateException e) {
            throw new TemplateGenerationException("模板处理错误: " + templateName, e);
        } catch (Exception e) {
            throw new TemplateGenerationException("生成模板时发生未知错误: " + templateName, e);
        }
    }
}