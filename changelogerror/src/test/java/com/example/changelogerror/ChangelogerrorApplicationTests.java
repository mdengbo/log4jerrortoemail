package com.example.changelogerror;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.example.changelogerror.service.LogTest;
import com.example.changelogerror.service2.Service2Test;
import com.example.changelogerror.testlog.Service3Test;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.NonNull;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String packageName = "com.example.changelogerror.*.logge2";
        String logLevel = "debug";
        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            List<Logger> loggerList = loggerContext.getLoggerList();
            List<String> aPackage = getPackage(loggerList, packageName);
            for (String name : aPackage) {
                loggerContext.getLogger(name).setLevel(Level.valueOf(logLevel));
            }
        } catch (Exception e) {
            log.error("动态修改日志级别出错，原因：" + e.getMessage(), e);
            log.debug("动态修改日志级别出错，原因：" + e.getMessage(), e);
        }
        log.info("change log success!");
    }



    public List<String> getPackage(List<Logger> loggerList, String packageName) {
        List<String> packageNames = new ArrayList<>();
        if (packageName.contains("*")) {
            String parentPackage = "";
            String childPackage = "";
            // "\\*" 代表 *
            String[] packages = packageName.split("\\*");
            //去掉最后一个  “.”
            parentPackage = packages[0].substring(0, packages[0].length() - 1);
            //去掉第一个 “.”
            childPackage = packages[1].substring(1, packages[1].length());
            for (Logger pac : loggerList) {
                String name = pac.getName();
                //1、找到 parent 目录
                if (name.contains(parentPackage) && name.endsWith(childPackage)) {
                    //2、找对应的child目录
                    packageNames.add(pac.getName());
                }
            }

        } else {
            //不含 * 直接返回
            packageNames.add(packageName);
        }
        return packageNames;
    }
}
