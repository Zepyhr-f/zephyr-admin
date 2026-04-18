package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.core.tool.util.ZBeanUtils;
import com.zephyr.system.mapper.DeptMapper;
import com.zephyr.system.mapper.UserMapper;
import com.zephyr.system.mapper.UserRoleMapper;
import com.zephyr.system.pojo.entity.Dept;
import com.zephyr.system.pojo.entity.Role;
import com.zephyr.system.pojo.entity.User;
import com.zephyr.system.pojo.entity.UserRole;
import com.zephyr.system.pojo.vo.UserVO;
import com.zephyr.system.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户 Service 实现
 *
 * @author zephyr
 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final UserRoleMapper userRoleMapper;
    private final DeptMapper deptMapper;

    @Override
    public User getUserByUserName(String username) {
        return baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        return baseMapper.selectRolesByUserId(userId);
    }

    @Override
    public List<String> getPermsByUserId(Long userId) {
        return baseMapper.selectPermsByUserId(userId);
    }

    @Override
    public List<UserVO> listWithDept(String username, String phone, Integer status, Long deptId) {
        // 查询用户
        List<User> users = list(new LambdaQueryWrapper<User>()
                .like(username != null && !username.isEmpty(), User::getUsername, username)
                .eq(phone != null && !phone.isEmpty(), User::getPhone, phone)
                .eq(status != null, User::getStatus, status)
                .eq(deptId != null, User::getDeptId, deptId)
                .orderByDesc(User::getCreateTime));

        if (users.isEmpty()) return List.of();

        // 获取部门ID集合
        List<Long> deptIds = users.stream()
                .filter(u -> u.getDeptId() != null)
                .map(User::getDeptId)
                .distinct()
                .collect(Collectors.toList());

        // 批量查询部门名称
        Map<Long, String> deptNameMap = deptIds.isEmpty() ? Map.of() :
                deptMapper.selectBatchIds(deptIds).stream()
                        .collect(Collectors.toMap(Dept::getId, Dept::getDeptName));

        // 组装 VO
        return users.stream().map(user -> {
            UserVO vo = new UserVO();
            ZBeanUtils.copyProperties(user, vo);
            if (user.getDeptId() != null) {
                vo.setDeptName(deptNameMap.getOrDefault(user.getDeptId(), ""));
            }
            // 查当前用户的角色ID
            List<Long> roleIds = userRoleMapper.selectList(
                    new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getId()))
                    .stream().map(UserRole::getRoleId).collect(Collectors.toList());
            vo.setRoleIds(roleIds);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        // 清空旧关系
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId));
        // 插入新关系
        if (roleIds != null && !roleIds.isEmpty()) {
            roleIds.forEach(roleId -> {
                UserRole ur = new UserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                userRoleMapper.insert(ur);
            });
        }
        return true;
    }

    @Override
    public boolean resetPassword(Long userId) {
        String encoded = new BCryptPasswordEncoder().encode("123456");
        User user = new User();
        user.setId(userId);
        user.setPassword(encoded);
        return updateById(user);
    }

    @Override
    public boolean updateStatus(Long userId, Integer status) {
        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        return updateById(user);
    }
}
