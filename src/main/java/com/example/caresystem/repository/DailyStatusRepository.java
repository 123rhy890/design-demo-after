package com.example.caresystem.repository;

import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.DailyStatus;
import com.example.caresystem.entity.User;
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
 * 日常状态记录数据访问接口
 * @author rhy
 */
@Repository
public interface DailyStatusRepository extends JpaRepository<DailyStatus, Integer>, JpaSpecificationExecutor<DailyStatus> {

    /**
     * 根据儿童查找日常记录
     * @param child 儿童
     * @return 日常记录列表
     */
    List<DailyStatus> findByChild(Child child);

    /**
     * 根据教师查找日常记录
     * @param teacher 教师
     * @return 日常记录列表
     */
    List<DailyStatus> findByTeacher(User teacher);

    /**
     * 根据记录日期查找日常记录
     * @param recordDate 记录日期
     * @return 日常记录列表
     */
    List<DailyStatus> findByRecordDate(LocalDate recordDate);

    /**
     * 根据推送状态查找日常记录
     * @param pushStatus 推送状态
     * @return 日常记录列表
     */
    List<DailyStatus> findByPushStatus(Integer pushStatus);

    /**
     * 根据儿童ID查找日常记录
     * @param childId 儿童ID
     * @return 日常记录列表
     */
    @Query("SELECT d FROM DailyStatus d WHERE d.child.childId = :childId")
    List<DailyStatus> findByChildId(@Param("childId") Integer childId);

    /**
     * 根据儿童和日期查找日常记录
     * @param child 儿童
     * @param recordDate 记录日期
     * @return 日常记录
     */
    Optional<DailyStatus> findByChildAndRecordDate(Child child, LocalDate recordDate);

    /**
     * 根据儿童ID和日期查找日常记录
     * @param childId 儿童ID
     * @param recordDate 记录日期
     * @return 日常记录
     */
    @Query("SELECT d FROM DailyStatus d WHERE d.child.childId = :childId AND d.recordDate = :recordDate")
    Optional<DailyStatus> findByChildIdAndRecordDate(@Param("childId") Integer childId,
                                                     @Param("recordDate") LocalDate recordDate);

    /**
     * 根据日期范围和教师查找日常记录
     * @param teacherId 教师ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日常记录列表
     */
    @Query("SELECT d FROM DailyStatus d WHERE d.teacher.userId = :teacherId AND d.recordDate BETWEEN :startDate AND :endDate")
    List<DailyStatus> findByTeacherIdAndDateRange(@Param("teacherId") Integer teacherId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    /**
     * 查找所有异常记录（abnormalType不为空）
     * @return 异常记录列表
     */
    @Query("SELECT d FROM DailyStatus d WHERE d.abnormalType IS NOT NULL AND d.abnormalType <> ''")
    List<DailyStatus> findAllAbnormalRecords();

    /**
     * 根据类型查找异常记录
     * @param abnormalType 异常类型
     * @return 异常记录列表
     */
    List<DailyStatus> findByAbnormalType(String abnormalType);

    /**
     * 统计本月异常总数
     * @param startDate 本月开始日期
     * @param endDate 本月结束日期
     * @return 异常总数
     */
    @Query("SELECT COUNT(d) FROM DailyStatus d WHERE d.recordDate BETWEEN :startDate AND :endDate AND d.abnormalType IS NOT NULL AND d.abnormalType <> ''")
    long countMonthlyAbnormal(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 统计本月特定类型的异常数
     * @param abnormalType 异常类型
     * @param startDate 本月开始日期
     * @param endDate 本月结束日期
     * @return 异常数
     */
    @Query("SELECT COUNT(d) FROM DailyStatus d WHERE d.abnormalType = :abnormalType AND d.recordDate BETWEEN :startDate AND :endDate")
    long countMonthlyAbnormalByType(@Param("abnormalType") String abnormalType, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 统计本月已处理的异常数
     * @param startDate 本月开始日期
     * @param endDate 本月结束日期
     * @return 已处理异常数
     */
    @Query("SELECT COUNT(d) FROM DailyStatus d WHERE d.status = 1 AND d.recordDate BETWEEN :startDate AND :endDate AND d.abnormalType IS NOT NULL AND d.abnormalType <> ''")
    long countMonthlyHandledAbnormal(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 查找有异常记录的日常记录
     * @return 异常记录列表
     */
    @Query("SELECT d FROM DailyStatus d WHERE d.abnormalType IS NOT NULL AND d.abnormalType <> ''")
    List<DailyStatus> findAbnormalRecords();

    /**
     * 查找未推送的日常记录
     * @return 未推送记录列表
     */
    @Query("SELECT d FROM DailyStatus d WHERE d.pushStatus = 0")
    List<DailyStatus> findUnpushedRecords();

    /**
     * 查找推送失败的日常记录
     * @return 推送失败记录列表
     */
    @Query("SELECT d FROM DailyStatus d WHERE d.pushStatus = 2")
    List<DailyStatus> findPushFailedRecords();

    /**
     * 更新推送状态
     * @param recordId 记录ID
     * @param pushStatus 推送状态
     * @param pushTime 推送时间
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE DailyStatus d SET d.pushStatus = :pushStatus, d.pushTime = :pushTime, d.updateTime = :updateTime WHERE d.recordId = :recordId")
    int updatePushStatus(@Param("recordId") Integer recordId,
                         @Param("pushStatus") Integer pushStatus,
                         @Param("pushTime") LocalDateTime pushTime,
                         @Param("updateTime") LocalDateTime updateTime);

    /**
     * 批量更新推送状态
     * @param recordIds 记录ID列表
     * @param pushStatus 推送状态
     * @param pushTime 推送时间
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE DailyStatus d SET d.pushStatus = :pushStatus, d.pushTime = :pushTime, d.updateTime = :updateTime WHERE d.recordId IN :recordIds")
    int batchUpdatePushStatus(@Param("recordIds") List<Integer> recordIds,
                              @Param("pushStatus") Integer pushStatus,
                              @Param("pushTime") LocalDateTime pushTime,
                              @Param("updateTime") LocalDateTime updateTime);

    /**
     * 分页查询儿童日常记录
     * @param childId 儿童ID
     * @param pageable 分页参数
     * @return 日常记录分页数据
     */
    @Query("SELECT d FROM DailyStatus d WHERE d.child.childId = :childId ORDER BY d.recordDate DESC")
    Page<DailyStatus> findByChildId(@Param("childId") Integer childId, Pageable pageable);

    /**
     * 根据日期范围查询日常记录
     * @param childId 儿童ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日常记录列表
     */
    @Query("SELECT d FROM DailyStatus d WHERE d.child.childId = :childId AND d.recordDate BETWEEN :startDate AND :endDate ORDER BY d.recordDate DESC")
    List<DailyStatus> findByChildIdAndDateRange(@Param("childId") Integer childId,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);

    /**
     * 统计儿童异常记录数量
     * @param childId 儿童ID
     * @return 异常记录数量
     */
    @Query("SELECT COUNT(d) FROM DailyStatus d WHERE d.child.childId = :childId AND d.abnormalType IS NOT NULL AND d.abnormalType <> ''")
    Long countAbnormalRecordsByChildId(@Param("childId") Integer childId);

    /**
     * 查找最近一周的日常记录
     * @param childId 儿童ID
     * @return 日常记录列表
     */
    @Query("SELECT d FROM DailyStatus d WHERE d.child.childId = :childId AND d.recordDate >= :weekAgo ORDER BY d.recordDate DESC")
    List<DailyStatus> findRecentWeekRecords(@Param("childId") Integer childId,
                                            @Param("weekAgo") LocalDate weekAgo);

    /**
     * 根据关键字搜索日常记录
     * @param keyword 关键字（饮食、作业、活动、异常类型等）
     * @return 日常记录列表
     */
    @Query("SELECT d FROM DailyStatus d WHERE " +
            "d.content LIKE %:keyword% OR " +
            "d.diet LIKE %:keyword% OR " +
            "d.homework LIKE %:keyword% OR " +
            "d.activity LIKE %:keyword% OR " +
            "d.abnormalType LIKE %:keyword% OR " +
            "d.abnormalDesc LIKE %:keyword%")
    List<DailyStatus> searchByKeyword(@Param("keyword") String keyword);
}
