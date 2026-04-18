package com.zephyr.system.feign;

import com.zephyr.system.pojo.entity.Role;
import com.zephyr.system.pojo.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "zephyr-system")
public interface IUserClient {
    String API_PREFIX = "/feign/client/system";
    String GET_USER = API_PREFIX + "/role/getUser";
    String GET_ROLES = API_PREFIX + "/role/getRoles";
    String GET_PERMS = API_PREFIX + "/role/getPerms";

    @GetMapping(GET_USER)
    User getUserByUserName(@RequestParam("username") String username);

    @GetMapping(GET_ROLES)
    List<Role> getRolesByUserId(@RequestParam("userId") Long userId);

    @GetMapping(GET_PERMS)
    List<String> getPermsByUserId(@RequestParam("userId") Long userId);
}
