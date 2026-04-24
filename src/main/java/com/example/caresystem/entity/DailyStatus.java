package com.example.caresystem.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 日常状态记录实体类 - 对应数据库t_daily_status表
 * 记录儿童每天的饮食、作业、活动等情况
 */
@Data
@Entity
@Table(name = "t_daily_status")
@DynamicInsert
@DynamicUpdate
public class DailyStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Integer recordId;  // 记录ID，自增主键

    /**
     * 关联儿童信息（多对一关系）
     * 一条状态记录对应一个儿童
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    /**
     * 关联记录教师（多对一关系）
     * 记录由哪位老师填写
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    /** 记录日期，格式yyyy-MM-dd */
    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    /** 记录类型：1-日常表现/2-饮食情况/3-午睡情况/4-活动表现/5-健康状况/6-其他 */
    @Column(name = "record_type")
    private Integer recordType;

    /** 记录内容，使用TEXT类型存储，不可为空 */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /** 饮食情况描述，使用TEXT类型存储，可为空 */
    @Column(name = "diet", columnDefinition = "TEXT")
    private String diet;

    /** 作业完成情况描述，使用TEXT类型存储，可为空 */
    @Column(name = "homework", columnDefinition = "TEXT")
    private String homework;

    /** 活动参与情况描述，使用TEXT类型存储，可为空 */
    @Column(name = "activity", columnDefinition = "TEXt")
    private String activity;

    /** 异常类型，如咳嗽/过敏/磕碰，长度50字符，可为空 */
    @Column(name = "abnormal_type", length = 50)
    private String abnormalType;

    /** 异常详情描述，使用TEXT类型存储，可为空 */
    @Column(name = "abnormal_desc", columnDefinition = "TEXT")
    private String abnormalDesc;

    /** 异常图片存储路径，长度255字符，可为空 */
    @Column(name = "abnormal_img", length = 255)
    private String abnormalImg;

    /** 异常严重程度：low-轻微/medium-中等/high-严重 */
    @Column(name = "severity", length = 20)
    private String severity;

    /** 异常发生时间 */
    @Column(name = "occur_time")
    private LocalDateTime occurTime;

    /** 处理措施描述，使用TEXT类型存储，可为空 */
    @Column(name = "treatment", columnDefinition = "TEXT")
    private String treatment;

    /** 处理状态：0-处理中/1-已处理 */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /** 是否通知家长：0-否/1-是 */
    @Column(name = "is_notify", nullable = false)
    private Integer isNotify = 0;

    /** 推送状态：0-未推送/1-已推送/2-推送失败 */
    // 用于家长通知功能
    @Column(name = "push_status", nullable = false)
    private Integer pushStatus;

    /** 推送时间，可为空 */
    @Column(name = "push_time")
    private LocalDateTime pushTime;

    /** 记录创建时间 */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /** 记录最后修改时间 */
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

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
