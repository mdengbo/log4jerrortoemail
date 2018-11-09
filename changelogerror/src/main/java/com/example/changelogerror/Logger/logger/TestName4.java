package com.example.changelogerror.Logger.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestName4 {
    public void testInfo(){
        log.info("testName4 info test");
        log.error("testName4 error test");
        log.debug("testName4 bug test");
    }
}
