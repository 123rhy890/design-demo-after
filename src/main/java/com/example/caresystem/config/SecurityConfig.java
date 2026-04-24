package com.example.caresystem.config;

import com.example.caresystem.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security安全配置类
 * 配置认证授权、密码加密、JWT认证等
 * @author rhy
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 启用方法级安全控制
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(@Lazy JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * 配置密码加密器
     * 使用BCrypt强哈希算法加密密码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 配置安全过滤器链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF保护（因为使用JWT）
                .csrf().disable()

                // 配置会话管理（无状态，使用JWT）
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 配置请求授权
                .authorizeRequests()
                // 允许公开访问的路径
                .antMatchers(
                        "/user/login",            // 登录接口
                        "/user/register",         // 注册接口
                        "/api/public/**",         // 公开接口
                        "/swagger-ui/**",         // Swagger UI
                        "/v3/api-docs/**",        // OpenAPI文档
                        "/webjars/**",            // WebJars资源
                        "/doc.html",              // 接口文档
                        "/favicon.ico",           // 网站图标
                        "/uploads/**",            // 静态资源访问
                        "/upload/**"              // 文件上传接口
                ).permitAll()
                // 需要认证的接口
                .anyRequest().authenticated()
                .and()

                // 添加JWT认证过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 允许跨域
        http.cors();

        return http.build();
    }
}
