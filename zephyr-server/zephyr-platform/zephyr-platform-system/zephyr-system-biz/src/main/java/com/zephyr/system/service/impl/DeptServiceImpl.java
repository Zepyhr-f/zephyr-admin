package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.system.convert.DeptConvert;
import com.zephyr.system.mapper.DeptMapper;
import com.zephyr.system.pojo.entity.Dept;
import com.zephyr.system.pojo.vo.DeptVO;
import com.zephyr.system.service.IDeptService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门 Service 实现
 *
 * @author zephyr
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    @Override
    public List<DeptVO> listTree() {
        // 查询全部未删除部门，按排序升序
        List<Dept> allDepts = list(new LambdaQueryWrapper<Dept>()
                .orderByAsc(Dept::getOrderNum));
        // 转为 VO
        List<DeptVO> allVos = allDepts.stream().map(dept -> {
            DeptVO vo = DeptConvert.INSTANCE.toVo(dept);
            return vo;
        }).collect(Collectors.toList());
        // 构建树形
        return buildTree(allVos, null);
    }

    private List<DeptVO> buildTree(List<DeptVO> all, String parentCode) {
        List<DeptVO> children = new ArrayList<>();
        boolean targetIsRoot = parentCode == null || parentCode.trim().isEmpty() || "0".equals(parentCode);
        
        for (DeptVO vo : all) {
            String pCode = vo.getParentCode();
            boolean isRoot = pCode == null || pCode.trim().isEmpty() || "0".equals(pCode);
            
            if ((targetIsRoot && isRoot) || (!isRoot && pCode.equals(parentCode))) {
                vo.setChildren(buildTree(all, vo.getCode()));
                children.add(vo);
            }
        }
        return children;
    }
}
