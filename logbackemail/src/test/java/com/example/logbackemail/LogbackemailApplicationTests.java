package com.example.logbackemail;

import com.example.logbackemail.downloadlog.DownLoadLogs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogbackemailApplicationTests {
    private final static Logger logger =  LoggerFactory.getLogger(LogbackemailApplicationTests.class);

    @Autowired
    DownLoadLogs downLoadLogs;

    @Test
    public void contextLoads() {
    }
    @Test
    public void logtest() {
        try {
            int i = 0;
            int b = 10;
            int c = b / i;
        } catch (Exception e) { // TODO: handle exception
           Date date = new Date();
            logger.error("异常发生时间："+date,e);
        }
    }

    @Test
    public void downLoad(){
    }
}
