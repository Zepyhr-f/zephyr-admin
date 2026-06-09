package com.zephyr.system.feign;

import com.zephyr.system.pojo.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "zephyr-common")
public interface IUserClient {
    String API_PREFIX = "/feign/client/system";
    String GET_USER_BY_CODE = API_PREFIX + "/role/getUserByCode";
    String GET_ROLES_BY_USER_CODE = API_PREFIX + "/role/getRoles";
    String GET_PERMS_BY_USER_CODE = API_PREFIX + "/role/getPerms";

    @GetMapping(GET_USER_BY_CODE)
    User getUserByCode(@RequestParam("code") String code, @RequestParam(value = "tenantCode", required = false) String tenantCode);

    @GetMapping(GET_ROLES_BY_USER_CODE)
    List<String> getRolesByUserCode(@RequestParam("userCode") String userCode, @RequestParam(value = "tenantCode", required = false) String tenantCode);

    @GetMapping(GET_PERMS_BY_USER_CODE)
    List<String> getPermsByUserCode(@RequestParam("userCode") String userCode, @RequestParam(value = "tenantCode", required = false) String tenantCode);
}
