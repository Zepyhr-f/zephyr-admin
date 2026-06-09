package com.zephyr.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zephyr.system.pojo.entity.User;
import com.zephyr.system.pojo.vo.UserVO;

import java.util.List;

/**
 * 用户 Service 接口
 *
 * @author zephyr
 */
public interface IUserService extends IService<User> {

    User getUserByCode(String code, String tenantCode);

    List<String> getRolesByUserCode(String userCode, String tenantCode);

    List<String> getPermsByUserCode(String userCode, String tenantCode);

    /**
     * 分页查询用户，携带部门名称
     *
     * @param username 用户名（模糊）
     * @param phone    手机号（精确）
     * @param status   状态
     * @param deptCode 部门编码
     */
    List<UserVO> listWithDept(String username, String phone, Integer status, String deptCode);

//    /**
//     * 分配角色
//     *
//     * @param userId  用户ID
//     * @param roleIds 角色ID列表
//     */
//    boolean assignRoles(Long userId, List<Long> roleIds);

    /**
     * 重置密码为默认密码（123456 BCrypt加密）
     */
    boolean resetPassword(Long userId);

    /**
     * 更新用户状态
     */
    boolean updateStatus(Long userId, Integer status);
}
