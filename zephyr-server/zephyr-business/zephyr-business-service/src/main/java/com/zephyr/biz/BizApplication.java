package com.zephyr.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Business Services Startup Class
 *
 * This is the PUBLIC startup class for all business services:
 * - rule engine (Drools) (port 9301)
 * - (future: other business modules)
 *
 * @author Zephyr
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.zephyr")
public class BizApplication {
    public static void main(String[] args) {
        SpringApplication.run(BizApplication.class, args);
    }
}
