package com.example.caresystem.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 沟通记录实体类 - 对应数据库t_communication表
 * 记录家长与老师之间的沟通信息
 */
@Data
@Entity
@Table(name = "t_communication")
@DynamicInsert
@DynamicUpdate
public class Communication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comm_id")
    private Integer commId;  // 沟通ID，自增主键

    /**
     * 关联儿童信息（多对一关系）
     * 沟通内容涉及哪个儿童
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    /**
     * 关联发送者用户（多对一关系）
     * 消息由谁发送
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_id", nullable = false)
    private User sender;

    /**
     * 关联接收者用户（多对一关系）
     * 消息发送给谁
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_id", nullable = false)
    private User receiver;

    /** 消息内容，使用TEXT类型存储，不可为空 */
    @Column(name = "message_content", nullable = false, columnDefinition = "TEXT")
    private String messageContent;

    /** 附件存储路径，长度255字符，可为空 */
    @Column(name = "attachment_url", length = 255)
    private String attachmentUrl;

    /** 消息发送时间 */
    @Column(name = "send_time", nullable = false)
    private LocalDateTime sendTime;

    /** 回复状态：0-未回复/1-已回复 */
    @Column(name = "reply_status", nullable = false)
    private Integer replyStatus;

    /** 回复内容，使用TEXT类型存储，可为空 */
    @Column(name = "reply_content", columnDefinition = "TEXT")
    private String replyContent;

    /** 回复时间，可为空 */
    @Column(name = "reply_time")
    private LocalDateTime replyTime;

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
        // 如果未设置发送时间，默认为当前时间
        if (this.sendTime == null) {
            this.sendTime = now;
        }
    }

    /**
     * 实体更新前的回调方法
     */
    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
        // 当设置了回复内容时，自动更新回复时间和状态
        if (this.replyContent != null && !this.replyContent.isEmpty() && this.replyTime == null) {
            this.replyTime = LocalDateTime.now();
            this.replyStatus = 1;  // 已回复
        }
    }
}
