package com.zephyr.core.boot.web;

import com.alibaba.ttl.TransmittableThreadLocal;

public class UserContextHolder {
    private static final TransmittableThreadLocal<UserSession> CONTEXT = new TransmittableThreadLocal<>();

    public static void set(UserSession userSession) {
        CONTEXT.set(userSession);
    }

    public static UserSession get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
