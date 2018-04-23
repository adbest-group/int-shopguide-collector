package com.bt.shopguide.collector.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by caiting on 2017/9/11.
 */
public class AppLuncher {
    public static void main(String[] args) {
        String[] cfgs = new String[]{"classpath:applicationContext.xml"};
        ApplicationContext ctx = new ClassPathXmlApplicationContext(cfgs);
    }
}
