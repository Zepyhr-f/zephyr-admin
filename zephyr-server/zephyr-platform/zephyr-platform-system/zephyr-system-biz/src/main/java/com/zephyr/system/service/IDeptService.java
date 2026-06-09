package com.zephyr.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyr.system.pojo.entity.Dept;
import com.zephyr.system.pojo.vo.DeptVO;

import java.util.List;

/**
 * 部门 Service 接口
 *
 * @author zephyr
 */
public interface IDeptService extends IService<Dept> {

    /**
     * 查询部门树形结构
     */
    List<DeptVO> listTree();
}
