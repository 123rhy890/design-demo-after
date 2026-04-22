package com.example.caresystem.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA配置类
 * 启用JPA审计、事务管理和实体扫描
 * @author rhy
 */
@Configuration
@EnableJpaAuditing  // 启用JPA审计功能（自动填充创建时间、更新时间等）
@EnableTransactionManagement  // 启用事务管理
@EntityScan(basePackages = "com.example.caresystem.entity")  // 扫描实体类
@EnableJpaRepositories(basePackages = "com.example.caresystem.repository")  // 扫描Repository
public class JpaConfig {

}
