package com.zephyr.system.wrapper;

import com.zephyr.mp.base.BaseEntityWrapper;
import com.zephyr.system.convert.UserConvert;
import com.zephyr.system.pojo.entity.User;
import com.zephyr.system.pojo.vo.UserVO;

/**
* User包装类,返回视图层所需的字段
*
* @author zephyr
* @since 2025-09-24
*/
public class UserWrapper extends BaseEntityWrapper<User, UserVO>  {

    private final UserConvert userConvert = UserConvert.INSTANCE;

    public static UserWrapper build() {
        return new UserWrapper();
    }

    @Override
    public UserVO entityVO(User user){
        return userConvert.toVo(user);
    }
}