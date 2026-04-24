package com.example.caresystem.controller;

import com.example.caresystem.utils.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @Value("${file.upload-path}")
    private String uploadPath;

    @Value("${file.access-path}")
    private String accessPath;

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    @PostMapping("/record")
    public Result<Map<String, Object>> uploadRecordImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        // 获取项目根目录
        String rootPath = System.getProperty("user.dir");
        // 构建绝对存储路径
        String absoluteDir = rootPath + File.separator + uploadPath + File.separator + "records";
        
        File destDir = new File(absoluteDir);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;

        File dest = new File(destDir, fileName);
        try {
            file.transferTo(dest);
            
            Map<String, Object> data = new HashMap<>();
            // 动态构建访问路径，确保包含 context-path
            String url = contextPath + accessPath + "/records/" + fileName;
            data.put("url", url.replace("//", "/")); // 防止双斜杠
            data.put("fileName", fileName);
            
            return Result.success(data);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败：" + e.getMessage());
        }
    }
}
