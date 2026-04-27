package com.zephyr.system.feign;

import com.zephyr.system.pojo.entity.User;
import com.zephyr.system.pojo.vo.UserVO;
import com.zephyr.system.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 内部 Feign 接口服务端实现
 *
 * @author Zephyr
 * @since 2025-09-07
 */
@RestController
@AllArgsConstructor
public class UserClient implements IUserClient {

    private final IUserService userService;

    @Override
    @GetMapping(IUserClient.GET_USER_BY_CODE)
    public User getUserByUserCode(@RequestParam("userCode") String userCode, @RequestParam("tenantCode")String tenantCode) {
        return userService.getUserByUserCode(userCode, tenantCode);
    }

    @Override
    @GetMapping(IUserClient.GET_ROLES_BY_USER_CODE)
    public List<String> getRolesByUserCode(@RequestParam("userCode") String userCode, @RequestParam("tenantCode")String tenantCode) {
        return userService.getRolesByUserCode(userCode, tenantCode);
    }

    @Override
    @GetMapping(IUserClient.GET_PERMS_BY_USER_CODE)
    public List<String> getPermsByUserCode(@RequestParam("userCode") String userCode, @RequestParam("tenantCode")String tenantCode) {
        return userService.getPermsByUserCode(userCode, tenantCode);
    }
}