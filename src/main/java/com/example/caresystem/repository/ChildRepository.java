package com.example.caresystem.repository;

import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 儿童信息数据访问层 - 操作数据库sys_child表
 * 继承JpaRepository 自带增删改查、分页等基础方法
 */
@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    /**
     * 根据家长id查询该家长下的所有孩子列表 (核心关联查询)
     */
    List<Child> findByParent(User parent);

    /**
     * 根据儿童姓名模糊查询
     */
    List<Child> findByNameLike(String name);
}