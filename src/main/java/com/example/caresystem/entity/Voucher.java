package com.example.caresystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 缴费凭证实体类 - 对应数据库t_voucher表
 * 记录家长上传的缴费凭证信息
 */
@Data
@Entity
@Table(name = "t_voucher")
@DynamicInsert
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Integer voucherId;  // 凭证ID，自增主键

    /**
     * 关联费用账单（多对一关系）
     * 凭证对应哪个账单
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private FeeBill feeBill;

    /**
     * 关联家长用户（多对一关系）
     * 凭证由哪个家长上传
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private User parent;

    /** 凭证类型：image-图片，长度20字符 */
    @Column(name = "voucher_type", nullable = false, length = 20)
    private String voucherType;

    /** 凭证存储路径，长度255字符 */
    @Column(name = "voucher_url", nullable = false, length = 255)
    private String voucherUrl;

    /** 凭证上传时间 */
    @Column(name = "upload_time", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadTime;

    /**
     * 关联审核人（多对一关系）
     * 凭证由谁审核，可为空
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditor_id")
    private User auditor;

    /** 审核时间，可为空 */
    @Column(name = "audit_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    /** 审核状态：0-待审核/1-通过/2-驳回 */
    @Column(name = "audit_status", nullable = false)
    private Integer auditStatus;

    /** 审核备注，使用TEXT类型存储，可为空 */
    @Column(name = "audit_remark", columnDefinition = "TEXT")
    private String auditRemark;

    /** 记录创建时间 */
    @Column(name = "create_time", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 记录最后修改时间 */
    @Column(name = "update_time", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 实体持久化前的回调方法
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.updateTime = now;
        // 如果未设置上传时间，默认为当前时间
        if (this.uploadTime == null) {
            this.uploadTime = now;
        }
    }

    /**
     * 实体更新前的回调方法
     */
    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
