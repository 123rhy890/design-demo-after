package com.example.caresystem.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 费用规则实体类 - 对应数据库t_fee_rule表
 * 定义不同时间段的收费标准
 */
@Data
@Entity
@Table(name = "t_fee_rule")
@DynamicInsert
@DynamicUpdate
public class FeeRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Integer ruleId;  // 规则ID，自增主键

    /** 时间段：上午/下午/全天，长度20字符 */
    @Column(name = "time_slot", nullable = false, length = 20)
    private String timeSlot;

    /** 时段单价，保留2位小数，使用DECIMAL类型确保精度 */
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /** 规则生效时间 */
    @Column(name = "effective_time", nullable = false)
    private LocalDateTime effectiveTime;

    /** 规则失效时间 */
    @Column(name = "invalid_time", nullable = false)
    private LocalDateTime invalidTime;

    /** 规则状态：0-停用/1-正常 */
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * 关联规则创建人（多对一关系）
     * 记录由谁创建的费用规则
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by", nullable = false)
    private User createBy;

    /** 规则创建时间 */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /** 规则最后修改时间 */
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
