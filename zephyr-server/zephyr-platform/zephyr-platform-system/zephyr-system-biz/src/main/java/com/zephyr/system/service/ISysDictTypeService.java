package com.zephyr.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyr.system.pojo.entity.SysDictType;

/**
 * 字典类型 服务类
 *
 * @author zephyr
 */
public interface ISysDictTypeService extends IService<SysDictType> {
    /**
     * 清空字典缓存
     */
    void clearCache();
}
