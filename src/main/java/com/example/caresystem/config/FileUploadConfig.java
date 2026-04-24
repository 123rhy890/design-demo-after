package com.example.caresystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 文件上传配置类
 * 配置图片上传路径和静态资源访问
 * @author rhy
 */
@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    @Value("${file.upload-path}")
    private String uploadPath;

    @Value("${file.access-path}")
    private String accessPath;

    /**
     * 配置静态资源映射
     * 将上传的文件路径映射为可访问的URL
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取项目根目录的绝对路径
        String rootPath = System.getProperty("user.dir");
        String absolutePath = rootPath + File.separator + uploadPath + File.separator;
        
        // 确保上传目录存在
        File uploadDir = new File(absolutePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 配置静态资源映射
        // 注意：Windows 下 file: 后面需要三个斜杠，或者使用正确的路径转换
        registry.addResourceHandler(accessPath + "/**")
                .addResourceLocations("file:" + absolutePath)
                .setCachePeriod(3600);
    }
}
