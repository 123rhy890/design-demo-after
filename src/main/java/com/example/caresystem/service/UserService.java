package com.example.caresystem.service;

import com.example.caresystem.entity.User;
import com.example.caresystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // 密码加密器 - SpringSecurity加密工具
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 用户注册业务
     */
    public User register(User user) {
        // 校验用户名是否存在
        Optional<User> existUser = userRepository.findByUsername(user.getUsername());
        if (existUser.isPresent()) {
            throw new RuntimeException("用户名已被注册，请更换");
        }
        // 校验手机号是否存在
        Optional<User> existPhone = userRepository.findByPhone(user.getPhone());
        if (existPhone.isPresent()) {
            throw new RuntimeException("该手机号已绑定账号");
        }
        // 密码加密存储
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * 用户登录业务
     */
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户名不存在"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误，请重新输入");
        }
        if (!user.getEnabled()) {
            throw new RuntimeException("账号已被禁用，请联系管理员");
        }
        return user;
    }
}