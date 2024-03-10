package com.tianyongwei.config.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * application listener在项目中不能用，todo
 * @todo
 */
//@WebListener
public class MyApplicationListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
    }
}
