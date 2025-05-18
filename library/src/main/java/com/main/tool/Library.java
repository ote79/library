package com.main.tool;

import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.util.logging.LogManager;


@lombok.extern.java.Log

public class Library {

    static {
        try {
            LogManager manager = LogManager.getLogManager();
            manager.readConfiguration(Resources.getResourceAsStream("logging.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void start(){
        log.info("系统启动");
        System.out.println("欢迎使用斯人图书管理系统！");
        Log.log();
        System.err.println("退出成功！！！");
    }
}
