package com.example.caresystem.repository;

import com.example.caresystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 根据用户名查询用户
    Optional<User> findByUsername(String username);

    // 根据手机号查询用户
    Optional<User> findByPhone(String phone);
}