package com.example.changelogerror.Utils;

import ch.qos.logback.classic.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author madengbo
 * @create 2018-11-09 08:15
 * @desc 工具类
 * @Version 1.0
 **/
@Component
public class GetPackageUtils {

    List<String> packageNames ;

    public List<String> getPackage(List<Logger> loggerList, String packageName) throws NoSuchFieldException, IllegalAccessException {

        packageNames = new ArrayList<>();

        if (packageName.contains("*")) {
            String startPackage = "";
            String endPackage = "";
            // "\\*" 代表 *
            String[] packages = packageName.split("\\*");
            //去掉最后一个  “.”
            startPackage = packages[0].substring(0, packages[0].length() - 1);

            if (packages.length > 1) {
                //去掉第一个 “.”
                endPackage = packages[1].substring(1, packages[1].length());
            } else {
                packageNames.add(packageName.substring(0, packages[0].length() - 2));
                return packageNames;
            }
            for (Logger pac : loggerList) {
                String name = pac.getName();
                //1、找到 parent 目录
                if (name.endsWith(startPackage)) {
                    getChild(pac, endPackage);
                }
            }
        } else {
            //不含 * 直接返回
            packageNames.add(packageName);
        }
        return packageNames;
    }

    //递归 处理child 目录
    private void getChild(Logger logger, String endPackage) throws NoSuchFieldException, IllegalAccessException {
        //获取子目录
        List<Logger> childrenList = (List<Logger>) ReflectUtils.getValue(logger, "childrenList");

        //子目录下面
        if (null != childrenList && childrenList.size() > 0) {
            for (Logger child : childrenList) {
                childrenList = (List<Logger>) ReflectUtils.getValue(child, "childrenList");
                if (null != childrenList && !childrenList.isEmpty()) {
                    getChild(child, endPackage);
                } else {
                    //获取 parent 包
                    Logger name = (Logger) ReflectUtils.getValue(child, "parent");
                    if (name.getName().endsWith(endPackage)) {
                        packageNames.add(name.getName());
                    }
                }
            }
        }
    }
}
