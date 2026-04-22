package com.example.caresystem.controller;

import com.example.caresystem.entity.User;
import com.example.caresystem.service.UserService;
import com.example.caresystem.utils.Result;
import com.example.caresystem.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        User savedUser = userService.register(user);
        return Result.success(savedUser);
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        String token = jwtUtils.generateToken(loginUser.getUsername());
        
        Map<String, Object> data = new HashMap<>();
        data.put("user", loginUser);
        data.put("token", token);
        
        return Result.success(data);
    }

    @GetMapping("/{id:\\d+}")
    public Result<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    @GetMapping("/username/{username}")
    public Result<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return Result.success(user);
    }

    @GetMapping("/phone/{phone}")
    public Result<User> getUserByPhone(@PathVariable String phone) {
        User user = userService.getUserByPhone(phone);
        return Result.success(user);
    }

    @GetMapping("/role/{roleType}")
    public Result<List<User>> getUsersByRole(@PathVariable Integer roleType) {
        List<User> users = userService.getUsersByRole(roleType);
        return Result.success(users);
    }

    @GetMapping("/role/{roleType}/status/{status}")
    public Result<List<User>> getUsersByRoleAndStatus(@PathVariable Integer roleType,
                                                       @PathVariable Integer status) {
        List<User> users = userService.getUsersByRoleAndStatus(roleType, status);
        return Result.success(users);
    }

    @GetMapping("/status/{status}")
    public Result<List<User>> getUsersByStatus(@PathVariable Integer status) {
        List<User> users = userService.getUsersByStatus(status);
        return Result.success(users);
    }

    @GetMapping("/list")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }

    @PutMapping("/update/{id}")
    public Result<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return Result.success(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<User> updateStatus(@PathVariable Integer id, @RequestParam Integer status) {
        User user = userService.updateStatus(id, status);
        return Result.success(user);
    }

    @PutMapping("/audit/{id}")
    public Result<User> auditUser(@PathVariable Integer id, @RequestParam Integer status) {
        User user = userService.auditUser(id, status);
        return Result.success(user);
    }

    @GetMapping("/count/role/{roleType}")
    public Result<Long> countByRole(@PathVariable Integer roleType) {
        Long count = userService.countByRole(roleType);
        return Result.success(count);
    }

    @GetMapping("/search")
    public Result<List<User>> searchUsers(@RequestParam String keyword) {
        List<User> users = userService.searchUsers(keyword);
        return Result.success(users);
    }
}