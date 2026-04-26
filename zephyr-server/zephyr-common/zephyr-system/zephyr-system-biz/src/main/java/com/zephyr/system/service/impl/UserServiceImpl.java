package com.zephyr.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zephyr.system.convert.UserConvert;
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
    public UserVO getUserByUserCode(String userCode, String tenantCode) {
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserCode, userCode)
                .eq(User::getTenantCode, tenantCode));
        return UserConvert.INSTANCE.toVo(user);
    }

    public List<String> getRolesByUserCode(String userCode, String tenantCode){
        List<Role> roles = baseMapper.selectRolesByUserCode(userCode, tenantCode);
        return roles.stream().map(Role::getRoleCode).collect(Collectors.toList());
    }

    @Override
    public List<String> getPermsByUserCode(String userCode, String tenantCode) {
        return baseMapper.selectPermsByUserCode(userCode, tenantCode);
    }

    @Override
    public List<UserVO> listWithDept(String username, String phone, Integer status, String deptCode) {
        // 查询用户
        List<User> users = list(new LambdaQueryWrapper<User>()
                .like(username != null && !username.isEmpty(), User::getUsername, username)
                .eq(phone != null && !phone.isEmpty(), User::getPhone, phone)
                .eq(status != null, User::getStatus, status)
                .eq(deptCode != null, User::getDeptCode, deptCode)
                .orderByDesc(User::getCreateTime));

        if (users.isEmpty()) return List.of();

        // 获取部门ID集合
        List<String> deptCodes = users.stream()
                .filter(u -> u.getDeptCode() != null)
                .map(User::getDeptCode)
                .distinct()
                .collect(Collectors.toList());

        // 批量查询部门名称
        Map<Long, String> deptNameMap = deptCodes.isEmpty() ? Map.of() :
                deptMapper.selectBatchIds(deptCodes).stream()
                        .collect(Collectors.toMap(Dept::getId, Dept::getDeptName));

        // 组装 VO
        return users.stream().map(user -> {
            UserVO vo = UserConvert.INSTANCE.toVo(user);
            if (user.getDeptCode() != null) {
                vo.setDeptName(deptNameMap.getOrDefault(user.getDeptCode(), ""));
            }
            // 查当前用户的角色ID
            List<String> roleCodes = userRoleMapper.selectList(
                    new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserCode, user.getId()))
                    .stream().map(UserRole::getRoleCode).collect(Collectors.toList());
            vo.setRoleCodes(roleCodes);
            return vo;
        }).collect(Collectors.toList());
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public boolean assignRoles(Long userId, List<Long> roleIds) {
//        // 清空旧关系
//        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>()
//                .eq(UserRole::getUserId, userId));
//        // 插入新关系
//        if (roleIds != null && !roleIds.isEmpty()) {
//            roleIds.forEach(roleId -> {
//                UserRole ur = new UserRole();
//                ur.setUserId(userId);
//                ur.setRoleId(roleId);
//                userRoleMapper.insert(ur);
//            });
//        }
//        return true;
//    }

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
