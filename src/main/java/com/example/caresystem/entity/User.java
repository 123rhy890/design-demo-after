package com.example.caresystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 用户信息实体类 - 对应数据库t_user表
 * @Data Lombok注解，自动生成getter、setter、toString、equals、hashCode方法
 */
@Data
@Entity
@Table(name = "t_user")
@DynamicInsert  // 插入时忽略null字段，使用数据库默认值
@DynamicUpdate  // 更新时只更新非null字段
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;  // 用户ID，自增主键

    /** 登录账号，长度50字符，不可为空 */
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    /** 加密后的密码，长度100字符，不可为空 */
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    /** 角色类型：0-管理员/1-老师/2-家长 */
    @Column(name = "role_type", nullable = false)
    private Integer roleType;

    /** 手机号码，唯一索引，长度20字符，不可为空 */
    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;

    /** 电子邮箱，长度50字符，可为空 */
    @Column(name = "email", length = 50)
    private String email;

    /** 用户状态：0-禁用/1-正常/2-待审核 */
    @Column(name = "status", nullable = false)
    private Integer status;

    /** 用户创建时间 */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /** 用户信息最后修改时间 */
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    /**
     * 实体持久化前的回调方法
     * 自动设置创建时间和更新时间
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.updateTime = now;
    }

    /**
     * 实体更新前的回调方法
     * 自动更新修改时间
     */
    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
