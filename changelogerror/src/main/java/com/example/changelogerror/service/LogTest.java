package com.example.changelogerror.service;

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
public class LogTest {

    public void testInfo(){
        log.info("info test");
        log.error("error test");
        log.debug("bug test");
    }
}
