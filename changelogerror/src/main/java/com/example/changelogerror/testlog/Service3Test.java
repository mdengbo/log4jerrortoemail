package com.example.changelogerror.testlog;

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
public class Service3Test {

    public void testInfo(){
        log.info("service3test info test");
        log.error("service3test error test");
        log.debug("service3test bug test");
    }
}
