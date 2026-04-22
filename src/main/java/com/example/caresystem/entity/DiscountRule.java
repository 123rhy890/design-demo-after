package com.example.caresystem.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠规则实体类 - 对应数据库t_discount_rule表
 * 定义各种优惠规则供账单使用
 */
@Data
@Entity
@Table(name = "t_discount_rule")
@DynamicInsert
@DynamicUpdate
public class DiscountRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Integer discountId;  // 优惠规则ID，自增主键

    /** 优惠名称，长度50字符 */
    @Column(name = "discount_name", nullable = false, length = 50)
    private String discountName;

    /** 优惠类型：0-折扣/1-满减 */
    @Column(name = "discount_type", nullable = false)
    private Integer discountType;

    /** 优惠值：折扣比例或满减金额，保留2位小数 */
    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

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
     * 记录由谁创建的优惠规则
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
