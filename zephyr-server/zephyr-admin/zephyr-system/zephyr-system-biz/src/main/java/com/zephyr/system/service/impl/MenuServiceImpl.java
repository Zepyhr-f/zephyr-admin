package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.system.convert.MenuConvert;
import com.zephyr.system.mapper.MenuMapper;
import com.zephyr.system.pojo.entity.Menu;
import com.zephyr.system.pojo.vo.MenuVO;
import com.zephyr.system.service.IMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单 Service 实现
 *
 * @author zephyr
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<MenuVO> listTree() {
        List<Menu> allMenus = list(new LambdaQueryWrapper<Menu>()
                .orderByAsc(Menu::getOrderNum));
        List<MenuVO> allVos = allMenus.stream().map(menu -> {
            MenuVO vo = MenuConvert.INSTANCE.toVo(menu);
            return vo;
        }).collect(Collectors.toList());
        return buildTree(allVos, null);
    }

    private List<MenuVO> buildTree(List<MenuVO> all, String parentCode) {
        List<MenuVO> children = new ArrayList<>();
        for (MenuVO vo : all) {
            String pCode = vo.getParentCode();
            if ((pCode == null && parentCode == null) || (pCode != null && pCode.equals(parentCode))) {
                vo.setChildren(buildTree(all, vo.getCode()));
                children.add(vo);
            }
        }
        return children;
    }
}
