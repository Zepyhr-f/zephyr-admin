package com.zephyr.system.feign;

import com.zephyr.system.pojo.entity.Role;
import com.zephyr.system.pojo.entity.User;
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
    @GetMapping(IUserClient.GET_USER)
    public User getUserByUserName(@RequestParam("username") String username) {
        return userService.getUserByUserName(username);
    }

    @Override
    @GetMapping(IUserClient.GET_ROLES)
    public List<Role> getRolesByUserId(@RequestParam("userId") Long userId) {
        return userService.getRolesByUserId(userId);
    }

    @Override
    @GetMapping(IUserClient.GET_PERMS)
    public List<String> getPermsByUserId(@RequestParam("userId") Long userId) {
        return userService.getPermsByUserId(userId);
    }
}