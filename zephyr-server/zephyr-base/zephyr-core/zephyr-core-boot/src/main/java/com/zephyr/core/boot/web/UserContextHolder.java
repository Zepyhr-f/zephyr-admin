package com.zephyr.core.boot.web;

public class UserContextHolder {
    private static final ThreadLocal<UserSession> CONTEXT = new ThreadLocal<>();

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