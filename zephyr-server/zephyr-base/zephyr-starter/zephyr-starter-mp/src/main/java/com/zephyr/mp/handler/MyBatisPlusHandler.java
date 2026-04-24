package com.zephyr.mp.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zephyr.core.boot.web.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

/**
 * MyBatis-Plus 自动填充处理器
 *
 * @author zephyr
 */
@Slf4j
@Component
public class MyBatisPlusHandler implements MetaObjectHandler {

    private static final String DEFAULT_USER_ID = "-1";

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("start insert fill ....");
        String userCode = getCurrentUserCode();
        Date now = new Date();

        this.strictInsertFill(metaObject, "createUser", String.class, userCode);
        this.strictInsertFill(metaObject, "createTime", Date.class, now);
        this.strictInsertFill(metaObject, "updateUser", String.class, userCode);
        this.strictInsertFill(metaObject, "updateTime", Date.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("start update fill ....");
        this.strictUpdateFill(metaObject, "updateUser", String.class, getCurrentUserCode());
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }

    private String getCurrentUserCode() {
        return StringUtils.isEmpty(UserContextHolder.get().getUserCode()) ?
                UserContextHolder.get().getUserCode() :
                DEFAULT_USER_ID;
    }

}
