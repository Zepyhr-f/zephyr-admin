package com.zephyr.system.wrapper;

import com.zephyr.core.tool.util.ZBeanUtils;
import com.zephyr.mp.base.BaseEntityWrapper;
import com.zephyr.system.pojo.entity.Menu;
import com.zephyr.system.pojo.vo.MenuVO;

/**
* Menu包装类,返回视图层所需的字段
*
* @author zephyr
* @since 2025-09-24
*/
public class MenuWrapper extends BaseEntityWrapper<Menu, MenuVO>  {

    public static MenuWrapper build() {
        return new MenuWrapper();
    }

    @Override
    public MenuVO entityVO(Menu menu){
        MenuVO menuVO = new MenuVO();
        ZBeanUtils.copyProperties(menu, menuVO);
        return menuVO;
    }
}