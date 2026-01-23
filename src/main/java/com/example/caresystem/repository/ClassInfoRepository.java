package com.example.caresystem.repository;

import com.example.caresystem.entity.ClassInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 班级信息数据访问层
 */
@Repository
public interface ClassInfoRepository extends JpaRepository<ClassInfo, Long> {
}