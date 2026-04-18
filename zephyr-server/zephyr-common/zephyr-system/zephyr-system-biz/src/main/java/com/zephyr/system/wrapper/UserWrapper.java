package com.zephyr.system.wrapper;

import com.zephyr.core.tool.util.ZBeanUtils;
import com.zephyr.mp.base.BaseEntityWrapper;
import com.zephyr.system.pojo.entity.User;
import com.zephyr.system.pojo.vo.UserVO;

/**
* User包装类,返回视图层所需的字段
*
* @author zephyr
* @since 2025-09-24
*/
public class UserWrapper extends BaseEntityWrapper<User, UserVO>  {

    public static UserWrapper build() {
        return new UserWrapper();
    }

    @Override
    public UserVO entityVO(User user){
        UserVO userVO = new UserVO();
        ZBeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}