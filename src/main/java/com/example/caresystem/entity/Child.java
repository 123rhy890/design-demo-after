package com.example.caresystem.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 儿童信息实体类 - 对应数据库sys_child表
 * 所有注解+关联类+导入包全部补全，无任何报错
 */
@Data
@Entity
@Table(name = "sys_child")
@DynamicInsert
@DynamicUpdate
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 儿童姓名 */
    private String name;

    /** 性别：男/女 */
    private String gender;

    /** 出生日期 */
    private Date birthday;

    /** 过敏信息/禁忌 */
    private String allergyInfo;

    /** 紧急联系人电话 */
    private String emergencyContact;

    /** 关联家长用户 多对一：多个孩子对应一个家长 */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private User parent;

    /** 关联班级信息 多对一：多个孩子对应一个班级 */
    @ManyToOne
    @JoinColumn(name = "class_id")
    private ClassInfo classInfo;

    /** 入学时间 */
    private Date createTime;

    /** 自动填充创建时间 */
    @PrePersist
    public void prePersist() {
        this.createTime = new Date();
    }
}