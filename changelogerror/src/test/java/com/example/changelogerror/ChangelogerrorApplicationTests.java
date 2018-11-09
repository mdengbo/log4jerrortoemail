package com.example.changelogerror;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.example.changelogerror.Utils.GetPackageUtils;
import com.example.changelogerror.service.LogTest;
import com.example.changelogerror.service2.Service2Test;
import com.example.changelogerror.testlog.Service3Test;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ChangelogerrorApplicationTests {

    @Autowired
    LogTest logTest;

    @Autowired
    Service2Test service2Test;

    @Autowired
    Service3Test service3Test;

    @Autowired
    GetPackageUtils getPackageUtils;

    @Test
    public void contextLoads() {
    }

    @Test
    public void hello() {

        logTest.testInfo();
        service2Test.testInfo();
        service3Test.testInfo();

    }

    @Test
    public void changeLog() {
        String packageName = "com.example.changelogerror.*.logge1";
        String logLevel = "debug";
        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            List<Logger> loggerList = loggerContext.getLoggerList();
            List<String> aPackage = getPackageUtils.getPackage(loggerList, packageName);
            for (String name : aPackage) {
                loggerContext.getLogger(name).setLevel(Level.valueOf(logLevel));
            }
        } catch (Exception e) {
            log.error("动态修改日志级别出错，原因：" + e.getMessage(), e);
            log.debug("动态修改日志级别出错，原因：" + e.getMessage(), e);
        }
        log.info("change log success!");
    }




}
