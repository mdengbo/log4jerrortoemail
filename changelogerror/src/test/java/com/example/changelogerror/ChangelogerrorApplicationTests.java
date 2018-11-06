package com.example.changelogerror;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.NonNull;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ChangelogerrorApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void changeLog() {
        String packageName = "com.example.changelogerror.controller";
        String logLevel = "debug";
        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            loggerContext.getLogger(packageName).setLevel(Level.valueOf(logLevel));
        } catch (Exception e) {
            log.error("动态修改日志级别出错，原因：" + e.getMessage(), e);
            log.debug("动态修改日志级别出错，原因：" + e.getMessage(), e);
        }
        log.info("change log success!");
    }
}
