package com.zephyr.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyr.system.pojo.entity.SysConfig;

/**
 * 参数配置 服务类
 *
 * @author zephyr
 */
public interface ISysConfigService extends IService<SysConfig> {
    /**
     * 根据键名查询参数配置 (带缓存)
     */
    String selectConfigByKey(String configKey);

    /**
     * 清空配置缓存
     */
    void clearCache();
}
