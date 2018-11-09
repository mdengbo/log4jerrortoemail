package com.example.changelogerror.Logger.logge2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestName3 {
    public void testInfo(){
        log.info("testName3 info test");
        log.error("testName3 error test");
        log.debug("testName3 bug test");
    }
}

