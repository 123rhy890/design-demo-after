package com.example.caresystem.service;

import com.example.caresystem.entity.User;
import com.example.caresystem.enums.UserEnums;
import com.example.caresystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + UserEnums.Role.getByCode(user.getRoleType()).name()))
        );
    }

    @Transactional
    public User register(User user) {
        if (!StringUtils.hasText(user.getUsername())) {
            throw new RuntimeException("用户名不能为空");
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new RuntimeException("密码不能为空");
        }
        if (!StringUtils.hasText(user.getPhone())) {
            throw new RuntimeException("手机号不能为空");
        }
        if (user.getRoleType() == null) {
            throw new RuntimeException("角色类型不能为空");
        }

        Optional<User> existUser = userRepository.findByUsername(user.getUsername());
        if (existUser.isPresent()) {
            throw new RuntimeException("用户名已被注册，请更换");
        }

        Optional<User> existPhone = userRepository.findByPhone(user.getPhone());
        if (existPhone.isPresent()) {
            throw new RuntimeException("该手机号已绑定账号");
        }

        if (StringUtils.hasText(user.getEmail())) {
            Optional<User> existEmail = userRepository.findByEmail(user.getEmail());
            if (existEmail.isPresent()) {
                throw new RuntimeException("该邮箱已绑定账号");
            }
        }

        if (!UserEnums.Role.isValid(user.getRoleType())) {
            throw new RuntimeException("无效的角色类型");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserEnums.Status.NORMAL.getCode());

        return userRepository.save(user);
    }

    public User login(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new RuntimeException("用户名和密码不能为空");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户名不存在"));

        // 兼容处理：如果数据库存储的是明文密码（非BCrypt格式），则先进行明文比对并自动升级加密
        String dbPassword = user.getPassword();
        boolean passwordMatch = false;
        
        if (dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$") || dbPassword.startsWith("$2y$")) {
            // 是BCrypt加密格式
            passwordMatch = passwordEncoder.matches(password, dbPassword);
        } else {
            // 是明文格式
            if (password.equals(dbPassword)) {
                passwordMatch = true;
                // 自动升级为加密密码
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
            }
        }

        if (!passwordMatch) {
            throw new RuntimeException("密码错误，请重新输入");
        }

        if (UserEnums.Status.DISABLED.getCode().equals(user.getStatus())) {
            throw new RuntimeException("账号已被禁用，请联系管理员");
        }

        return user;
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    public User getUserByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    public List<User> getUsersByRole(Integer roleType) {
        if (!UserEnums.Role.isValid(roleType)) {
            throw new RuntimeException("无效的角色类型");
        }
        return userRepository.findByRoleType(roleType);
    }

    public List<User> getUsersByRoleAndStatus(Integer roleType, Integer status) {
        return userRepository.findByRoleTypeAndStatus(roleType, status);
    }

    public List<User> getUsersByStatus(Integer status) {
        return userRepository.findByStatus(status);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(Integer id, User user) {
        User oldUser = getUserById(id);

        if (StringUtils.hasText(user.getPhone()) && !user.getPhone().equals(oldUser.getPhone())) {
            Optional<User> existPhone = userRepository.findByPhone(user.getPhone());
            if (existPhone.isPresent()) {
                throw new RuntimeException("该手机号已绑定账号");
            }
            oldUser.setPhone(user.getPhone());
        }

        if (StringUtils.hasText(user.getEmail())) {
            if (StringUtils.hasText(user.getEmail()) && !user.getEmail().equals(oldUser.getEmail())) {
                Optional<User> existEmail = userRepository.findByEmail(user.getEmail());
                if (existEmail.isPresent()) {
                    throw new RuntimeException("该邮箱已绑定账号");
                }
            }
            oldUser.setEmail(user.getEmail());
        }

        if (StringUtils.hasText(user.getPassword())) {
            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(oldUser);
    }

    @Transactional
    public void deleteUser(Integer id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Transactional
    public User updateStatus(Integer id, Integer status) {
        User user = getUserById(id);
        user.setStatus(status);
        return userRepository.save(user);
    }

    @Transactional
    public User auditUser(Integer id, Integer status) {
        User user = getUserById(id);
        if (!UserEnums.Role.isValid(user.getRoleType())) {
            throw new RuntimeException("无效的用户角色");
        }
        user.setStatus(status);
        return userRepository.save(user);
    }

    public Long countByRole(Integer roleType) {
        return userRepository.countByRoleType(roleType);
    }

    public List<User> searchUsers(String keyword) {
        return userRepository.searchByKeyword(keyword);
    }
}