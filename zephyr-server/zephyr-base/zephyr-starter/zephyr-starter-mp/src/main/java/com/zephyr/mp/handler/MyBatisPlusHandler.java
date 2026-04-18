package com.zephyr.mp.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
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

    private static final String USER_ID_HEADER = "X-User-userId";
    private static final Long DEFAULT_USER_ID = -1L;

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("start insert fill ....");
        Long userId = getCurrentUserId();
        Date now = new Date();

        this.strictInsertFill(metaObject, "createUser", Long.class, userId);
        this.strictInsertFill(metaObject, "createTime", Date.class, now);
        this.strictInsertFill(metaObject, "updateUser", Long.class, userId);
        this.strictInsertFill(metaObject, "updateTime", Date.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("start update fill ....");
        this.strictUpdateFill(metaObject, "updateUser", Long.class, getCurrentUserId());
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }

    /**
     * 获取当前登录用户 ID
     */
    private Long getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String userIdStr = request.getHeader(USER_ID_HEADER);
            if (userIdStr != null && !userIdStr.isEmpty()) {
                try {
                    return Long.parseLong(userIdStr);
                } catch (NumberFormatException e) {
                    log.warn("Parse user id failed: {}", userIdStr);
                }
            }
        }
        return DEFAULT_USER_ID;
    }
}
