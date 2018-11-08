package com.example.changelogerror.Logger;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.LoggerNameUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author madengbo
 * @create 2018-11-08 17:05
 * @desc
 * @Version 1.0
 **/
public class GetLogger extends LoggerContext {

    public  ch.qos.logback.classic.Logger myLogger(final String name) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        LoggerContext loggerContext = new LoggerContext();
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)getValue(loggerContext, "root");
        Map<String, ch.qos.logback.classic.Logger> loggerCache = (Map<String, ch.qos.logback.classic.Logger>)getValue(loggerContext, "loggerCache");


        if (name == null) {
            throw new IllegalArgumentException("name argument cannot be null");
        }

        // if we are asking for the root logger, then let us return it without
        // wasting time
        if (ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME.equalsIgnoreCase(name)) {
            return root;
        }

        int i = 0;

        ch.qos.logback.classic.Logger logger = root;

        // check if the desired logger exists, if it does, return it
        // without further ado.
        ch.qos.logback.classic.Logger childLogger = (ch.qos.logback.classic.Logger) loggerCache.get(name);
        // if we have the child, then let us return it without wasting time
        if (childLogger != null) {
            return childLogger;
        }

        // if the desired logger does not exist, them create all the loggers
        // in between as well (if they don't already exist)
        String childName;
        while (true) {
            int h = LoggerNameUtil.getSeparatorIndexOf(name, i);
            if (h == -1) {
                childName = name;
            } else {
                childName = name.substring(0, h);
            }
            // move i left of the last point
            i = h + 1;
            synchronized (logger) {
                childLogger = (ch.qos.logback.classic.Logger) callMethod(logger,"getChildByName",new Class[]{String.class},childName);
                if (childLogger == null) {
                    childLogger = (ch.qos.logback.classic.Logger) callMethod(logger,"createChildByName",new Class[]{String.class},childName);
                    loggerCache.put(childName, childLogger);
                    callMethod(loggerContext,"incSize",new Class[]{String.class},null);
                }
            }
            logger = childLogger;
            if (h == -1) {
                return childLogger;
            }
        }
    }

    /***
     * 获取私有成员变量的值
     *
     */
    public static Object getValue(Object instance, String fieldName)
            throws IllegalAccessException, NoSuchFieldException {

        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // 参数值为true，禁止访问控制检查

        return field.get(instance);
    }

    public static Object callMethod(Object instance, String methodName, Class[] classes, Object objects)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        Method method = instance.getClass().getDeclaredMethod(methodName, classes);
        method.setAccessible(true);
        return method.invoke(instance, objects);
    }
}
