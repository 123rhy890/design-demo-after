package com.example.caresystem.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 过敏数据实体类 - 对应数据库t_allergy表
 * 记录儿童的过敏信息
 */
@Data
@Entity
@Table(name = "t_allergy")
@DynamicInsert
@DynamicUpdate
public class Allergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allergy_id")
    private Integer allergyId;  // 过敏ID，自增主键

    /**
     * 关联儿童信息（多对一关系）
     * 过敏信息属于哪个儿童
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    /** 过敏类型：food-食物/drug-药物，长度50字符 */
    @Column(name = "allergy_type", nullable = false, length = 50)
    private String allergyType;

    /** 过敏内容：如牛奶/青霉素，长度100字符 */
    @Column(name = "allergy_content", nullable = false, length = 100)
    private String allergyContent;

    /** 过敏症状描述，使用TEXT类型存储，可为空 */
    @Column(name = "allergy_symptom", columnDefinition = "TEXT")
    private String allergySymptom;

    /** 处理方法描述，使用TEXT类型存储，可为空 */
    @Column(name = "handle_method", columnDefinition = "TEXT")
    private String handleMethod;

    /** 过敏状态：0-已痊愈/1-有效 */
    @Column(name = "status", nullable = false)
    private Integer status;

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
