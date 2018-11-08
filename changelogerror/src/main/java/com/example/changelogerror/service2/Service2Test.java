package com.example.changelogerror.service2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author madengbo
 * @create 2018-11-06 14:09
 * @desc
 * @Version 1.0
 **/
@Slf4j
@Component
public class Service2Test {

    public void testInfo(){
        log.info("service2test info test");
        log.error("service2test error test");
        log.debug("service2test bug test");
    }
}
