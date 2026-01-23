package com.example.caresystem.controller;

import com.example.caresystem.entity.User;
import com.example.caresystem.service.UserService;
import com.example.caresystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // 用户注册接口
    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        User savedUser = userService.register(user);
        return Result.success(savedUser);
    }

    // 用户登录接口
    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        return Result.success(loginUser);
    }
}