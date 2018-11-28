package com.example.emailtest;

import org.apache.log4j.Logger;
import com.example.emailtest.Utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailtestApplicationTests {

    @Test
    public void contextLoads() {
    }
    @Test
    public void logtest() {
        Logger logger = Logger.getLogger(EmailtestApplicationTests.class);
        try {
            int i = 0;
            int b = 10;
            int c = b / i;
        } catch (Exception e) { // TODO: handle exception
            String currentTime = DateUtils.getCurrentTime12EN();
            logger.error("异常发生时间："+currentTime,e);
        }
    }
}
