package com.zephyr.system.feign;

import com.zephyr.system.pojo.entity.User;
import com.zephyr.system.pojo.vo.UserVO;
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
    User getUserByUserCode(@RequestParam("userCode") String userCode, @RequestParam("tenantCode")String tenantCode);

    @GetMapping(GET_ROLES_BY_USER_CODE)
    List<String> getRolesByUserCode(@RequestParam("userCode") String userCode, @RequestParam("tenantCode")String tenantCode);

    @GetMapping(GET_PERMS_BY_USER_CODE)
    List<String> getPermsByUserCode(@RequestParam("userCode") String userCode, @RequestParam("tenantCode")String tenantCode);
}
