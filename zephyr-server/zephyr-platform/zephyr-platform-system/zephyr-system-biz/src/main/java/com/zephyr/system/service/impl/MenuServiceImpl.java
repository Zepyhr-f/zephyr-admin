package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.system.convert.MenuConvert;
import com.zephyr.system.mapper.MenuMapper;
import com.zephyr.system.pojo.entity.Menu;
import com.zephyr.system.pojo.vo.MenuVO;
import com.zephyr.system.service.IMenuService;
import org.springframework.stereotype.Service;

import com.zephyr.core.boot.web.UserContextHolder;
import com.zephyr.core.boot.web.UserSession;
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

    @org.springframework.beans.factory.annotation.Autowired
    private com.zephyr.redis.util.RedisUtil redisUtil;

    @Override
    public List<MenuVO> listRoutes() {
        UserSession session = UserContextHolder.get();
        if (session == null || session.getUserCode() == null) {
            return new ArrayList<>();
        }
        
        List<Menu> allMenus;
        List<String> roles = session.getRoles();
        if (roles != null && roles.contains("admin")) {
            allMenus = list(new LambdaQueryWrapper<Menu>().orderByAsc(Menu::getOrderNum));
        } else {
            allMenus = baseMapper.selectMenusByUserCode(session.getUserCode(), session.getTenantCode());
        }

        List<MenuVO> allVos = allMenus.stream().map(menu -> {
            MenuVO vo = MenuConvert.INSTANCE.toVo(menu);
            return vo;
        }).collect(Collectors.toList());
        return buildTree(allVos, null);
    }

    private List<MenuVO> buildTree(List<MenuVO> all, String parentCode) {
        List<MenuVO> children = new ArrayList<>();
        boolean targetIsRoot = parentCode == null || parentCode.trim().isEmpty() || "-1".equals(parentCode) || "0".equals(parentCode);
        
        for (MenuVO vo : all) {
            String pCode = vo.getParentCode();
            boolean isRoot = pCode == null || pCode.trim().isEmpty() || "-1".equals(pCode) || "0".equals(pCode);
            
            if ((targetIsRoot && isRoot) || (!isRoot && pCode.equals(parentCode))) {
                vo.setChildren(buildTree(all, vo.getCode()));
                children.add(vo);
            }
        }
        return children;
    }

    @Override
    public boolean removeByIds(java.util.Collection<?> idList) {
        if (idList != null && !idList.isEmpty()) {
            for (Object id : idList) {
                Menu menu = getById((java.io.Serializable) id);
                if (menu != null) {
                    long count = count(new LambdaQueryWrapper<Menu>().eq(Menu::getParentCode, menu.getCode()));
                    if (count > 0) {
                        throw new RuntimeException("包含下级菜单，不允许删除");
                    }
                }
            }
        }
        return super.removeByIds(idList);
    }
}
