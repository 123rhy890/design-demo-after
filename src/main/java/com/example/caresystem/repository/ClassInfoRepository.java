package com.example.caresystem.repository;

import com.example.caresystem.entity.ClassInfo;
import com.example.caresystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 班级信息数据访问接口
 * @author rhy
 */
@Repository
public interface ClassInfoRepository extends JpaRepository<ClassInfo, Integer>, JpaSpecificationExecutor<ClassInfo> {

    /**
     * 根据班级名称查找班级
     * @param className 班级名称
     * @return 班级实体
     */
    Optional<ClassInfo> findByClassName(String className);

    /**
     * 根据教师查找班级
     * @param teacher 教师
     * @return 班级列表
     */
    List<ClassInfo> findByTeacher(User teacher);

    /**
     * 根据教师ID查找班级
     * @param teacherId 教师ID
     * @return 班级列表
     */
    @Query("SELECT c FROM ClassInfo c WHERE c.teacher.userId = :teacherId")
    List<ClassInfo> findByTeacherId(@Param("teacherId") Integer teacherId);

    /**
     * 根据班级状态查找班级
     * @param classStatus 班级状态
     * @return 班级列表
     */
    List<ClassInfo> findByClassStatus(Integer classStatus);

    /**
     * 根据教师ID和状态查找班级
     * @param teacherId 教师ID
     * @param classStatus 班级状态
     * @return 班级列表
     */
    @Query("SELECT c FROM ClassInfo c WHERE c.teacher.userId = :teacherId AND c.classStatus = :classStatus")
    List<ClassInfo> findByTeacherIdAndClassStatus(@Param("teacherId") Integer teacherId,
                                                  @Param("classStatus") Integer classStatus);

    /**
     * 查找正常状态班级
     * @return 正常状态班级列表
     */
    @Query("SELECT c FROM ClassInfo c WHERE c.classStatus = 1 ORDER BY c.createTime DESC")
    List<ClassInfo> findNormalClasses();

    /**
     * 统计教师负责的班级数量
     * @param teacherId 教师ID
     * @return 班级数量
     */
    @Query("SELECT COUNT(c) FROM ClassInfo c WHERE c.teacher.userId = :teacherId")
    Long countByTeacherId(@Param("teacherId") Integer teacherId);

    /**
     * 统计指定状态的班级数量
     * @param classStatus 班级状态
     * @return 班级数量
     */
    Long countByClassStatus(Integer classStatus);

    /**
     * 根据创建时间范围查找班级
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 班级列表
     */
    List<ClassInfo> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据班级名称模糊查找
     * @param keyword 关键字
     * @return 班级列表
     */
    List<ClassInfo> findByClassNameContaining(String keyword);

    /**
     * 更新班级状态
     * @param classId 班级ID
     * @param classStatus 新状态
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE ClassInfo c SET c.classStatus = :classStatus, c.updateTime = :updateTime WHERE c.classId = :classId")
    int updateClassStatus(@Param("classId") Integer classId,
                          @Param("classStatus") Integer classStatus,
                          @Param("updateTime") LocalDateTime updateTime);

    /**
     * 更新班级教师
     * @param classId 班级ID
     * @param teacher 新教师
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE ClassInfo c SET c.teacher = :teacher, c.updateTime = :updateTime WHERE c.classId = :classId")
    int updateTeacher(@Param("classId") Integer classId,
                      @Param("teacher") User teacher,
                      @Param("updateTime") LocalDateTime updateTime);

    /**
     * 更新班级容量
     * @param classId 班级ID
     * @param maxCapacity 最大容量
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE ClassInfo c SET c.maxCapacity = :maxCapacity, c.updateTime = :updateTime WHERE c.classId = :classId")
    int updateCapacity(@Param("classId") Integer classId,
                       @Param("maxCapacity") Integer maxCapacity,
                       @Param("updateTime") LocalDateTime updateTime);

    /**
     * 检查班级名称是否已存在（排除指定班级）
     * @param className 班级名称
     * @param classId 要排除的班级ID
     * @return 是否存在
     */
    boolean existsByClassNameAndClassIdNot(String className, Integer classId);

    /**
     * 根据关键字搜索班级
     * @param keyword 关键字（班级名称、教师姓名）
     * @return 班级列表
     */
    @Query("SELECT c FROM ClassInfo c WHERE " +
            "c.className LIKE %:keyword% OR " +
            "c.teacher.username LIKE %:keyword%")
    List<ClassInfo> searchByKeyword(@Param("keyword") String keyword);
}
