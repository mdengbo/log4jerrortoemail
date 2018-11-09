package com.example.changelogerror.service.logge1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestName2 {
    public void testInfo(){
        log.info("testName2 info test");
        log.error("testName2 error test");
        log.debug("testName2 bug test");
    }
}
