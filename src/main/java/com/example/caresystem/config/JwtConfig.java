package com.example.caresystem.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

/**
 * JWT配置类
 * 配置JWT密钥和过期时间
 * @author rhy
 */
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private Long jwtRefreshExpiration;

    /**
     * 获取JWT密钥
     */
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * 获取JWT过期时间（毫秒）
     */
    public Long getExpiration() {
        return jwtExpiration;
    }

    /**
     * 获取刷新令牌过期时间（毫秒）
     */
    public Long getRefreshExpiration() {
        return jwtRefreshExpiration;
    }
}
