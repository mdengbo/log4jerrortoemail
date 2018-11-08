package com.example.changelogerror.Logger.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class testName4 {
    public void testInfo(){
        log.info("service info test");
        log.error("service error test");
        log.debug("service bug test");
    }
}
