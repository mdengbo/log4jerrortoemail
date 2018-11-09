package com.example.changelogerror.service2.logge1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestName {
    public void testInfo(){
        log.info("testName info test");
        log.error("testName error test");
        log.debug("testName bug test");
    }
}
