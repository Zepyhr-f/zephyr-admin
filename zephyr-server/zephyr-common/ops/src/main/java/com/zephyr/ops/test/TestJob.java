package com.zephyr.ops.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("testJob")
public class TestJob {

    public void execute() {
        log.info(">>>>>> [Zephyr-Ops] 定时任务测试：执行无参方法成功！");
    }

    public void executeWithParam(String param) {
        log.info(">>>>>> [Zephyr-Ops] 定时任务测试：接收到参数 {}，执行成功！", param);
    }
}
