package com.zephyr.core.tool.annotation;


import java.lang.annotation.*;

/**
 * 不拷贝注解
 *
 * @author Zephyr
 * @since 2025-09-23
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CopyIgnore {
}