package com.example.caresystem.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 班级信息实体类 - 对应数据库sys_class表
 * 必须创建，Child类关联了这个类
 */
@Data
@Entity
@Table(name = "sys_class")
@DynamicInsert
@DynamicUpdate
public class ClassInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 班级名称 比如：小班、中班、大班、一年级托管班 */
    private String className;

    /** 班级老师ID 关联User表 */
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    /** 班级人数 */
    private Integer studentNum;

    /** 班级备注 */
    private String remark;

    /** 创建时间 */
    private Date createTime;

    @PrePersist
    public void prePersist() {
        this.createTime = new Date();
    }
}