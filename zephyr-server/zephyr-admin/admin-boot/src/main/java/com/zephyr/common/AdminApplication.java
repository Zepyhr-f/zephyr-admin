package com.zephyr.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Common Services Startup Class
 *
 * This is the PUBLIC startup class for all common services:
 * - auth service (port 8072)
 * - system service
 * - ops service
 * - generator service
 *
 * @author Zephyr
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.zephyr.**.feign"})
@ComponentScan(basePackages = "com.zephyr")
@MapperScan({
        "com.zephyr.system.mapper",
        "com.zephyr.ops.mapper",
        "com.zephyr.generator.mapper"
})
public class CommonApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);
    }
}
