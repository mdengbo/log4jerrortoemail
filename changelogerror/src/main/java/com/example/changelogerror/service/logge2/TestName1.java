package com.example.changelogerror.service.logge2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestName1 {
    public void testInfo(){
        log.info("testName1 info test");
        log.error("testName1 error test");
        log.debug("testName1 bug test");
    }
}
