package com.example.caresystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 教室/班级信息实体类 - 对应数据库t_class表
 * 注意：使用ClassInfo避免与Java关键字Class冲突
 */
@Data
@Entity
@Table(name = "t_class")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ClassInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Integer classId;  // 班级ID，自增主键

    /** 教室名称，长度50字符，不可为空 */
    @Column(name = "class_name", nullable = false, length = 50)
    private String className;

    /** 教室最大容量，表示该教室最多可容纳的儿童数量 */
    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    /**
     * 关联教师用户（多对一关系）
     * 每个班级有一个负责的老师
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    /** 班级状态：0-停用/1-正常/2-维护 */
    @Column(name = "class_status", nullable = false)
    private Integer classStatus;

    /** 班级创建时间 */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /** 班级信息最后修改时间 */
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    /** 每日签到码（8位随机数） */
    @Column(name = "daily_sign_code", length = 8)
    private String dailySignCode;

    /** 签到码更新日期 */
    @Column(name = "code_update_date")
    private LocalDate codeUpdateDate;

    /**
     * 实体持久化前的回调方法
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.updateTime = now;
    }

    /**
     * 实体更新前的回调方法
     */
    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
