package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.core.tool.util.ZBeanUtils;
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
            MenuVO vo = new MenuVO();
            ZBeanUtils.copyProperties(menu, vo);
            return vo;
        }).collect(Collectors.toList());
        return buildTree(allVos, 0L);
    }

    private List<MenuVO> buildTree(List<MenuVO> all, Long parentId) {
        List<MenuVO> children = new ArrayList<>();
        for (MenuVO vo : all) {
            Long pid = vo.getParentId() != null ? vo.getParentId() : 0L;
            if (pid.equals(parentId)) {
                vo.setChildren(buildTree(all, vo.getId()));
                children.add(vo);
            }
        }
        return children;
    }
}
