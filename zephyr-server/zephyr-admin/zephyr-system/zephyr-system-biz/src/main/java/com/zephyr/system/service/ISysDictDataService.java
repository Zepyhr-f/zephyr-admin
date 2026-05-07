package com.zephyr.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyr.system.pojo.entity.SysDictData;
import java.util.List;

/**
 * 字典数据 服务类
 *
 * @author zephyr
 */
public interface ISysDictDataService extends IService<SysDictData> {
    /**
     * 根据字典类型获取字典数据列表 (优先从缓存读取)
     */
    List<SysDictData> selectDictDataByType(String dictType);
}
