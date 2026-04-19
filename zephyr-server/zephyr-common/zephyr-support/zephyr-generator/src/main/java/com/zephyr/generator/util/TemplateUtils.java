package com.zephyr.generator.util;


import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.zephyr.generator.pojo.entity.TableFieldModel;
import com.zephyr.generator.pojo.entity.TableModel;
import com.zephyr.generator.pojo.entity.ZTableInfo;
import freemarker.template.Configuration;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模版工具类
 *
 * @author Zephyr
 * @since 2025-09-19
 */
public class TemplateUtils {
    private static final String TEMPLATES_PATH = "templates";
    private static final String JAVA_SUFFIX = ".java";
    private static final String XML_SUFFIX = ".xml";
    private static final String BASE_IMPORTS = "baseImports";
    private static final String JAR_IMPORTS = "jarImports";

    /** 基础实体类公共字段 */
    private static final Set<String> BASE_ENTITY_FIELDS = Set.of(
            "id", "createUser", "createTime", "updateUser", "updateTime", "isDeleted"
    );

    /** 数值类型需要转字符串的集合 */
    private static final Set<String> NUMERIC_TYPES = Set.of(
            "long", "java.lang.long", "biginteger", "java.math.biginteger",
            "bigdecimal", "java.math.bigdecimal"
    );

    /** 日期字段格式 */
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_PATTERN = "yyyy-MM-dd";


    /** 类型到 import 的映射表 */
    private static final Map<String, String> TYPE_IMPORT_MAP = Map.of(
            "Date", "java.util.Date",
            "LocalDate", "java.time.LocalDate",
            "LocalDateTime", "java.time.LocalDateTime",
            "BigDecimal", "java.math.BigDecimal"
    );

    /**
     * 创建Freemarker配置
     * @return Freemarker配置
     */
    public static Configuration createFreemarkerConfig() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassLoaderForTemplateLoading(TemplateUtils.class.getClassLoader(), TEMPLATES_PATH);
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
        cfg.setLogTemplateExceptions(false);
        return cfg;
    }

    /**
     * 构建模版数据
     * @param zConfigContext MyBatis-Plus代码生成器配置构建器
     * @return 模版数据
     */
    public static TableModel buildTemplateContext(ZConfigContext zConfigContext) {
        ZTableInfo tableInfo = zConfigContext.getTableInfos().get(0);
        PackageConfig packageConfig = zConfigContext.getPackageConfig();
        GlobalConfig globalConfig = zConfigContext.getGlobalConfig();

        String comment = tableInfo.getComment();
        comment = (StringUtils.isEmpty(comment) || !comment.endsWith("表")) ?
                comment : comment.substring(0, comment.length() - 1);
        List<TableFieldModel> fieldModels = buildFieldList(tableInfo.getFields());
        Map<String, List<String>> importMap = buildImportList(fieldModels);

        TableModel tableModel = new TableModel();
        tableModel.setPackageName(packageConfig.getParent());
        tableModel.setClassName(tableInfo.getEntityName());
        tableModel.setAuthor(globalConfig.getAuthor());
        tableModel.setDate(LocalDate.now().toString());
        tableModel.setComment(comment);
        tableModel.setTableName(tableInfo.getName());
        tableModel.setFields(fieldModels);
        tableModel.setBaseImports(importMap.get(BASE_IMPORTS));
        tableModel.setJarImports(importMap.get(JAR_IMPORTS));

        return tableModel;
    }

    /**
     * 创建目录
     * @param directory 目录文件对象
     */
    public static void createDirectories(File directory) {
        if (!directory.exists() && !directory.mkdirs()) {
            throw new RuntimeException("创建目录失败: " + directory.getAbsolutePath());
        }
    }

    /**
     * 获取模板文件路径映射
     * @param zConfigContext MyBatis-Plus代码生成器配置构建器
     * @return 模板文件路径映射
     */
    public static Map<String, String> getTemplateOutputPaths(ZConfigContext zConfigContext) {
        Map<String, String> templateOutputPaths = new HashMap<>();

        ZTableInfo tableInfo = zConfigContext.getTableInfos().get(0);
        PackageConfig packageConfig = zConfigContext.getPackageConfig();
        GlobalConfig globalConfig = zConfigContext.getGlobalConfig();

        String basePath = globalConfig.getOutputDir() + "/" + packageConfig.getParent().replace(".", "/");
        basePath = basePath.replace("//","/");

        templateOutputPaths.put("entity.java.ftl",
                basePath + "/pojo/entity/" + tableInfo.getEntityName() + JAVA_SUFFIX);
        templateOutputPaths.put("mapper.java.ftl",
                basePath + "/mapper/" + tableInfo.getMapperName() + JAVA_SUFFIX);
        templateOutputPaths.put("service.java.ftl",
                basePath + "/service/" + tableInfo.getServiceName()+ JAVA_SUFFIX);
        templateOutputPaths.put("serviceImpl.java.ftl",
                basePath + "/service/impl/" + tableInfo.getServiceImplName() + JAVA_SUFFIX);
        templateOutputPaths.put("controller.java.ftl",
                basePath + "/controller/" + tableInfo.getControllerName() + JAVA_SUFFIX);
        templateOutputPaths.put("wrapper.java.ftl",
                basePath + "/wrapper/" + tableInfo.getWrapperName() + JAVA_SUFFIX);
        templateOutputPaths.put("entityVO.java.ftl",
                basePath + "/pojo/vo/" + tableInfo.getEntityVOName() + JAVA_SUFFIX);
        templateOutputPaths.put("mapper.xml.ftl",
                basePath + "/mapper/xml/" + tableInfo.getXmlName() + XML_SUFFIX);

        return templateOutputPaths;
    }

    /**
     * 构建字段列表（过滤基础字段）
     */
    private static List<TableFieldModel> buildFieldList(List<TableField> fields) {
        return fields.stream()
                .filter(field -> !BASE_ENTITY_FIELDS.contains(field.getPropertyName()))
                .map(field -> {
                    TableFieldModel fieldInfo = new TableFieldModel();
                    fieldInfo.setName(field.getPropertyName());
                    fieldInfo.setType(field.getPropertyType());
                    fieldInfo.setComment(field.getComment());

                    String typeLower = field.getPropertyType().toLowerCase();
                    if (NUMERIC_TYPES.contains(typeLower)) {
                        fieldInfo.setSerializeToString(true);
                    }

                    switch (typeLower) {
                        case "date", "localdatetime" -> fieldInfo.setDateFormat(DATE_TIME_PATTERN);
                        case "localdate" -> fieldInfo.setDateFormat(DATE_PATTERN);
                    }

                    return fieldInfo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 构建 import 列表
     */
    private static Map<String,List<String>> buildImportList(List<TableFieldModel> fieldModels) {
        Set<String> baseImports = new HashSet<>();
        Set<String> jarImports = new HashSet<>();

        fieldModels.forEach(field -> {
            String fieldType = field.getType();

            if (field.isSerializeToString()) {
                jarImports.add("com.fasterxml.jackson.databind.annotation.JsonSerialize");
                jarImports.add("com.fasterxml.jackson.databind.ser.std.ToStringSerializer");
            }

            if (TYPE_IMPORT_MAP.containsKey(fieldType)) {
                baseImports.add(TYPE_IMPORT_MAP.get(fieldType));
            }

            if ("Date".equals(fieldType) || "LocalDate".equals(fieldType) || "LocalDateTime".equals(fieldType)) {
                jarImports.add("org.springframework.format.annotation.DateTimeFormat");
                jarImports.add("com.fasterxml.jackson.annotation.JsonFormat");
            }
        });

        Map<String,List<String>> importMap = new HashMap<>();
        importMap.put(BASE_IMPORTS, baseImports.stream().sorted().toList());
        importMap.put(JAR_IMPORTS, jarImports.stream().sorted().toList());
        return importMap;
    }
}