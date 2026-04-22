package com.example.caresystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务配置类
 * 启用Spring的定时任务功能
 * 用于定时生成账单、发送提醒等
 * @author rhy
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
    // 启用定时任务，具体任务在Service中通过@Scheduled注解实现
}
