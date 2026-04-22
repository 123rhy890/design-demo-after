package com.example.caresystem.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 预约记录实体类 - 对应数据库t_reservation表
 * 记录家长为孩子预约托管的情况
 */
@Data
@Entity
@Table(name = "t_reservation")
@DynamicInsert
@DynamicUpdate
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer reservationId;  // 预约ID，自增主键

    /**
     * 关联儿童信息（多对一关系）
     * 一个预约对应一个儿童
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    /**
     * 关联家长用户（多对一关系）
     * 预约由家长发起
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private User parent;

    /** 预约托管的日期，格式yyyy-MM-dd */
    @Column(name = "reserve_date", nullable = false)
    private LocalDate reserveDate;

    /** 托管时间段：上午/下午/全天 */
    @Column(name = "time_slot", nullable = false, length = 20)
    private String timeSlot;

    /** 特殊需求说明，如晚餐、留餐等，使用TEXT类型 */
    @Column(name = "special_needs", columnDefinition = "TEXT")
    private String specialNeeds;

    /** 预约状态：review-待审核/confirm-已确认/cancel-已取消 */
    @Column(name = "reserve_status", nullable = false, length = 20)
    private String reserveStatus;

    /**
     * 关联审核人用户（多对一关系）
     * 预约需要管理员或老师审核
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditor_id")
    private User auditor;

    /** 审核时间 */
    @Column(name = "audit_time")
    private LocalDateTime auditTime;

    /** 预约创建时间 */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /** 预约信息最后修改时间 */
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
