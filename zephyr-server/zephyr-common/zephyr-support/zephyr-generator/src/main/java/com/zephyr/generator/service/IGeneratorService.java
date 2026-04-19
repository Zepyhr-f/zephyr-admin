package com.zephyr.generator.service;


import com.zephyr.generator.pojo.dto.GenInfo;


/**
 * 代码生成接口
 *
 * @author Zephyr
 * @since 2025-09-19
 */
public interface IGeneratorService {
    String generateCode(GenInfo genInfo);
}