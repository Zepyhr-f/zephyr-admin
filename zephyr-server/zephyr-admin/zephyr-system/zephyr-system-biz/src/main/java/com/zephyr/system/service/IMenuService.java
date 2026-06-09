package com.zephyr.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyr.system.pojo.entity.Menu;
import com.zephyr.system.pojo.vo.MenuVO;

import java.util.List;

/**
 * 菜单 Service 接口
 *
 * @author zephyr
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 查询全部菜单，以树形结构返回
     */
    List<MenuVO> listTree();

    /**
     * 查询当前用户的路由菜单树
     */
    List<MenuVO> listRoutes();
}
