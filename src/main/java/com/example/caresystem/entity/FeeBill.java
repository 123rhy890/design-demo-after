package com.example.caresystem.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 费用账单实体类 - 对应数据库t_fee_bill表
 * 记录每月的费用账单信息
 */
@Data
@Entity
@Table(name = "t_fee_bill")
@DynamicInsert
@DynamicUpdate
public class FeeBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Integer billId;  // 账单ID，自增主键

    /**
     * 关联儿童信息（多对一关系）
     * 账单对应哪个儿童
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    /**
     * 关联家长用户（多对一关系）
     * 账单对应哪个家长
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private User parent;

    /** 账单月份，格式：yyyy-MM，长度10字符 */
    @Column(name = "bill_month", nullable = false, length = 10)
    private String billMonth;

    /** 当月托管天数 */
    @Column(name = "manage_days", nullable = false)
    private Integer manageDays;

    /** 托管时间段：上午/下午/全天，长度20字符 */
    @Column(name = "time_slot", nullable = false, length = 20)
    private String timeSlot;

    /** 时段单价，从费用规则表获取，保留2位小数 */
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * 关联优惠规则（多对一关系）
     * 账单应用的优惠规则，可为空
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private DiscountRule discountRule;

    /** 优惠金额，默认0，保留2位小数 */
    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /** 应付金额 = 单价 × 天数 - 优惠金额，保留2位小数 */
    @Column(name = "payable_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal payableAmount;

    /** 实付金额，默认0，保留2位小数 */
    @Column(name = "actual_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal actualAmount = BigDecimal.ZERO;

    /** 缴费状态：0-未缴费/1-已缴费/2-欠费 */
    @Column(name = "payment_status", nullable = false)
    private Integer paymentStatus;

    /** 缴费截止日期 */
    @Column(name = "payment_deadline", nullable = false)
    private LocalDateTime paymentDeadline;

    /** 提醒次数，默认0次 */
    @Column(name = "remind_times", nullable = false)
    private Integer remindTimes = 0;

    /** 最后提醒时间，可为空 */
    @Column(name = "last_remind_time")
    private LocalDateTime lastRemindTime;

    /**
     * 关联账单创建人（多对一关系）
     * 记录由谁创建的账单
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by", nullable = false)
    private User createBy;

    /** 账单创建时间 */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /** 账单最后修改时间 */
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
