package com.example.changelogerror.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.example.changelogerror.Logger.GetLogger;
import com.example.changelogerror.service.LogTest;
import com.example.changelogerror.service2.Service2Test;
import com.example.changelogerror.testlog.Service3Test;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author madengbo
 * @create 2018-11-06 13:42
 * @desc 通过接口改变日志级别
 * @Version 1.0
 **/
@RestController
@Slf4j
public class ChangeLevel {
    @Autowired
    LogTest logTest;

    @Autowired
    Service2Test service2Test;

    @Autowired
    Service3Test service3Test;


    @GetMapping("/changeLog")
    public String changeLog(@NonNull String packageName, @NonNull String logLevel) {
        try {
            GetLogger loggerContext = (GetLogger) LoggerFactory.getILoggerFactory();
            loggerContext.myLogger(packageName).setLevel(Level.valueOf(logLevel));
        } catch (Exception e) {
            log.error("error动态修改日志级别出错，原因：" + e.getMessage(), e);
            return "change log fail!";
        }
        return "change log success!";
    }

    @RequestMapping("/test")
    public String  hello(){

        logTest.testInfo();
        service2Test.testInfo();
        service3Test.testInfo();

        return "hello world";
    }
}
