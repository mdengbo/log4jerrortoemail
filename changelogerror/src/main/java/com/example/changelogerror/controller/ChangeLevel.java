package com.example.changelogerror.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.example.changelogerror.Logger.logge2.TestName3;
import com.example.changelogerror.Logger.logger.TestName4;
import com.example.changelogerror.Utils.GetPackageUtils;
import com.example.changelogerror.service.LogTest;
import com.example.changelogerror.service.logge1.TestName2;
import com.example.changelogerror.service.logge2.TestName1;
import com.example.changelogerror.service2.Service2Test;
import com.example.changelogerror.service2.logge1.TestName;
import com.example.changelogerror.testlog.Service3Test;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    TestName testName;

    @Autowired
    TestName1 testName1;

    @Autowired
    TestName2 testName2;

    @Autowired
    TestName3 testName3;

    @Autowired
    TestName4 testName4;

    @Autowired
    Service2Test service2Test;

    @Autowired
    Service3Test service3Test;

    @Autowired
    GetPackageUtils getPackageUtils;


    @GetMapping("/changeLog")
    public String changeLog(@NonNull String packageName, @NonNull String logLevel) {
        try {
            LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();

            //自定义包引入 可以 通配符 *
            List<Logger> loggerList = loggerContext.getLoggerList();
            List<String> aPackage = getPackageUtils.getPackage(loggerList, packageName);
            if( aPackage.size() == 0){
                return "please check your packageName, packageName is not exit !";
            }
            for (String name : aPackage) {
                loggerContext.getLogger(name).setLevel(Level.valueOf(logLevel));
            }

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
        testName.testInfo();
        testName1.testInfo();
        testName2.testInfo();
        testName3.testInfo();
        testName4.testInfo();

        return "hello world";
    }
}
