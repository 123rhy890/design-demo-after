package com.example.caresystem.repository;

import com.example.caresystem.entity.Allergy;
import com.example.caresystem.entity.Child;
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
 * 过敏数据数据访问接口
 * @author rhy
 */
@Repository
public interface AllergyRepository extends JpaRepository<Allergy, Integer>, JpaSpecificationExecutor<Allergy> {

    /**
     * 根据儿童查找过敏记录
     * @param child 儿童
     * @return 过敏记录列表
     */
    List<Allergy> findByChild(Child child);

    /**
     * 根据过敏类型查找过敏记录
     * @param allergyType 过敏类型
     * @return 过敏记录列表
     */
    List<Allergy> findByAllergyType(String allergyType);

    /**
     * 根据过敏状态查找过敏记录
     * @param status 过敏状态
     * @return 过敏记录列表
     */
    List<Allergy> findByStatus(Integer status);

    /**
     * 根据儿童ID查找过敏记录
     * @param childId 儿童ID
     * @return 过敏记录列表
     */
    @Query("SELECT a FROM Allergy a WHERE a.child.childId = :childId")
    List<Allergy> findByChildId(@Param("childId") Integer childId);

    /**
     * 根据儿童和过敏类型查找过敏记录
     * @param child 儿童
     * @param allergyType 过敏类型
     * @return 过敏记录
     */
    Optional<Allergy> findByChildAndAllergyType(Child child, String allergyType);

    /**
     * 根据儿童和过敏内容查找过敏记录
     * @param child 儿童
     * @param allergyContent 过敏内容
     * @return 过敏记录
     */
    Optional<Allergy> findByChildAndAllergyContent(Child child, String allergyContent);

    /**
     * 查找有效的过敏记录
     * @return 有效过敏记录列表
     */
    @Query("SELECT a FROM Allergy a WHERE a.status = 1 ORDER BY a.createTime DESC")
    List<Allergy> findActiveAllergies();

    /**
     * 根据儿童和状态查找过敏记录
     * @param childId 儿童ID
     * @param status 状态
     * @return 过敏记录列表
     */
    @Query("SELECT a FROM Allergy a WHERE a.child.childId = :childId AND a.status = :status")
    List<Allergy> findByChildIdAndStatus(@Param("childId") Integer childId,
                                         @Param("status") Integer status);

    /**
     * 统计儿童过敏记录数量
     * @param childId 儿童ID
     * @return 过敏记录数量
     */
    @Query("SELECT COUNT(a) FROM Allergy a WHERE a.child.childId = :childId")
    Long countByChildId(@Param("childId") Integer childId);

    /**
     * 统计指定类型的过敏记录数量
     * @param allergyType 过敏类型
     * @return 过敏记录数量
     */
    Long countByAllergyType(String allergyType);

    /**
     * 更新过敏状态
     * @param allergyId 过敏ID
     * @param status 新状态
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Allergy a SET a.status = :status, a.updateTime = :updateTime WHERE a.allergyId = :allergyId")
    int updateStatus(@Param("allergyId") Integer allergyId,
                     @Param("status") Integer status,
                     @Param("updateTime") LocalDateTime updateTime);

    /**
     * 更新过敏信息和症状
     * @param allergyId 过敏ID
     * @param allergySymptom 过敏症状
     * @param handleMethod 处理方法
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Allergy a SET a.allergySymptom = :allergySymptom, a.handleMethod = :handleMethod, a.updateTime = :updateTime WHERE a.allergyId = :allergyId")
    int updateAllergyInfo(@Param("allergyId") Integer allergyId,
                          @Param("allergySymptom") String allergySymptom,
                          @Param("handleMethod") String handleMethod,
                          @Param("updateTime") LocalDateTime updateTime);

    /**
     * 根据创建时间范围查找过敏记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 过敏记录列表
     */
    List<Allergy> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查找常见的过敏类型
     * @return 不同过敏类型列表
     */
    @Query("SELECT DISTINCT a.allergyType FROM Allergy a")
    List<String> findDistinctAllergyTypes();

    /**
     * 查找常见的过敏内容
     * @return 不同过敏内容列表
     */
    @Query("SELECT DISTINCT a.allergyContent FROM Allergy a")
    List<String> findDistinctAllergyContents();

    /**
     * 根据关键字搜索过敏记录
     * @param keyword 关键字（过敏类型、过敏内容、症状等）
     * @return 过敏记录列表
     */
    @Query("SELECT a FROM Allergy a WHERE " +
            "a.allergyType LIKE %:keyword% OR " +
            "a.allergyContent LIKE %:keyword% OR " +
            "a.allergySymptom LIKE %:keyword% OR " +
            "a.handleMethod LIKE %:keyword%")
    List<Allergy> searchByKeyword(@Param("keyword") String keyword);
}
