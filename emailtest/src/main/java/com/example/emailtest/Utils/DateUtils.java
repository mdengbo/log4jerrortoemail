package com.example.emailtest.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author madengbo
 * @create 2018-11-01 14:28
 * @desc
 * @Version 1.0
 **/
public class DateUtils {
    public static String getCurrentTime24() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateFormat.format(new Date());
        return currentTime;
    }

    public static String getCurrentTime12EN() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss", Locale.ENGLISH);
        String currentTime = dateFormat.format(new Date());
        return currentTime;
    }

    public static String getCurrentTime12CHS() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
        String currentTime = dateFormat.format(new Date());
        return currentTime;
    }


}
