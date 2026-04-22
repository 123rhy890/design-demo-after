//package com.example.caresystem.repository;
//
//import com.example.caresystem.entity.Child;
//import com.example.caresystem.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
///**
// * 儿童信息数据访问层 - 操作数据库sys_child表
// * 继承JpaRepository 自带增删改查、分页等基础方法
// */
//@Repository
//public interface ChildRepository extends JpaRepository<Child, Long> {
//
//    /**
//     * 根据家长id查询该家长下的所有孩子列表 (核心关联查询)
//     */
//    List<Child> findByParent(User parent);
//
//    /**
//     * 根据儿童姓名模糊查询
//     */
//    List<Child> findByNameLike(String name);
//}
package com.example.caresystem.repository;

import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.ClassInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 儿童信息数据访问接口
 * @author rhy
 */
@Repository
public interface ChildRepository extends JpaRepository<Child, Integer>, JpaSpecificationExecutor<Child> {

    /**
     * 根据儿童姓名查找
     * @param childName 儿童姓名
     * @return 儿童实体
     */
    Optional<Child> findByChildName(String childName);

    /**
     * 根据班级查找儿童
     * @param classInfo 班级信息
     * @return 儿童列表
     */
    List<Child> findByClassInfo(ClassInfo classInfo);

    /**
     * 根据班级ID查找儿童
     * @param classId 班级ID
     * @return 儿童列表
     */
    @Query("SELECT c FROM Child c WHERE c.classInfo.classId = :classId")
    List<Child> findByClassId(@Param("classId") Integer classId);

    /**
     * 根据班级ID和状态查找儿童
     * @param classId 班级ID
     * @param status 状态标识（如果有的话）
     * @return 儿童列表
     */
    @Query("SELECT c FROM Child c WHERE c.classInfo.classId = :classId")
    List<Child> findByClassIdAndStatus(@Param("classId") Integer classId);

    /**
     * 根据紧急联系人电话查找儿童
     * @param emergencyPhone 紧急联系人电话
     * @return 儿童列表
     */
    List<Child> findByEmergencyPhone(String emergencyPhone);

    /**
     * 根据性别查找儿童
     * @param gender 性别
     * @return 儿童列表
     */
    List<Child> findByGender(Integer gender);

    /**
     * 统计班级人数
     * @param classInfo 班级
     * @return 人数
     */
    Long countByClassInfo(ClassInfo classInfo);

    /**
     * 根据出生日期范围查找儿童
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 儿童列表
     */
    List<Child> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 统计班级中的儿童数量
     * @param classId 班级ID
     * @return 儿童数量
     */
    @Query("SELECT COUNT(c) FROM Child c WHERE c.classInfo.classId = :classId")
    Long countByClassId(@Param("classId") Integer classId);

    /**
     * 查找有过敏史的儿童
     * @return 儿童列表
     */
    @Query("SELECT c FROM Child c WHERE c.allergyHistory IS NOT NULL AND c.allergyHistory <> ''")
    List<Child> findChildrenWithAllergy();

    /**
     * 根据创建时间范围查找儿童
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 儿童列表
     */
    List<Child> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 分页查询班级中的儿童
     * @param classId 班级ID
     * @param pageable 分页参数
     * @return 儿童分页数据
     */
    @Query("SELECT c FROM Child c WHERE c.classInfo.classId = :classId")
    Page<Child> findByClassId(@Param("classId") Integer classId, Pageable pageable);

    /**
     * 更新儿童班级信息
     * @param childId 儿童ID
     * @param classInfo 班级信息
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Child c SET c.classInfo = :classInfo, c.updateTime = :updateTime WHERE c.childId = :childId")
    int updateClassInfo(@Param("childId") Integer childId,
                        @Param("classInfo") ClassInfo classInfo,
                        @Param("updateTime") LocalDateTime updateTime);

    /**
     * 更新儿童过敏史信息
     * @param childId 儿童ID
     * @param allergyHistory 过敏史
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Child c SET c.allergyHistory = :allergyHistory, c.updateTime = :updateTime WHERE c.childId = :childId")
    int updateAllergyHistory(@Param("childId") Integer childId,
                             @Param("allergyHistory") String allergyHistory,
                             @Param("updateTime") LocalDateTime updateTime);

    /**
     * 更新紧急联系人信息
     * @param childId 儿童ID
     * @param emergencyContact 紧急联系人姓名
     * @param emergencyPhone 紧急联系电话
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Child c SET c.emergencyContact = :emergencyContact, c.emergencyPhone = :emergencyPhone, c.updateTime = :updateTime WHERE c.childId = :childId")
    int updateEmergencyContact(@Param("childId") Integer childId,
                               @Param("emergencyContact") String emergencyContact,
                               @Param("emergencyPhone") String emergencyPhone,
                               @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据关键字搜索儿童
     * @param keyword 关键字（儿童姓名、紧急联系人、电话）
     * @return 儿童列表
     */
    @Query("SELECT c FROM Child c WHERE " +
            "c.childName LIKE %:keyword% OR " +
            "c.emergencyContact LIKE %:keyword% OR " +
            "c.emergencyPhone LIKE %:keyword% OR " +
            "c.allergyHistory LIKE %:keyword%")
    List<Child> searchByKeyword(@Param("keyword") String keyword);

    /**
     * 根据多个ID查找儿童
     * @param childIds 儿童ID列表
     * @return 儿童列表
     */
    List<Child> findByChildIdIn(List<Integer> childIds);

    @Query("SELECT c FROM Child c WHERE c.parent.userId = :parentId")
    List<Child> findByParentId(@Param("parentId") Integer parentId);
}
