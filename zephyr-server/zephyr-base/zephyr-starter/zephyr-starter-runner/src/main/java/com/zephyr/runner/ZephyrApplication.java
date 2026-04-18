package com.zephyr.runner;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

/**
 * 启动注解
 *
 * @author Zephyr
 * @since 2025-09-13
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringBootApplication
@EnableFeignClients
public @interface ZephyrApplication {
}