package com.example.caresystem.repository;

import com.example.caresystem.entity.FeeRule;
import com.example.caresystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 费用规则数据访问接口
 * @author rhy
 */
@Repository
public interface FeeRuleRepository extends JpaRepository<FeeRule, Integer>, JpaSpecificationExecutor<FeeRule> {

    /**
     * 根据时间段查找有效的费用规则
     * @param timeSlot 时间段
     * @param now 当前时间
     * @return 费用规则
     */
    @Query("SELECT f FROM FeeRule f WHERE f.timeSlot = :timeSlot AND f.effectiveTime <= :now AND (f.invalidTime IS NULL OR f.invalidTime >= :now) AND f.status = 1")
    Optional<FeeRule> findActiveRuleByTimeSlot(@Param("timeSlot") String timeSlot,
                                               @Param("now") LocalDateTime now);

    /**
     * 根据时间段查找费用规则
     * @param timeSlot 时间段
     * @return 费用规则列表
     */
    List<FeeRule> findByTimeSlot(String timeSlot);

    /**
     * 根据状态查找费用规则
     * @param status 状态
     * @return 费用规则列表
     */
    List<FeeRule> findByStatus(Integer status);

    /**
     * 查找有效的费用规则
     * @param now 当前时间
     * @return 有效费用规则列表
     */
    @Query("SELECT f FROM FeeRule f WHERE f.effectiveTime <= :now AND (f.invalidTime IS NULL OR f.invalidTime >= :now) AND f.status = 1")
    List<FeeRule> findActiveRules(@Param("now") LocalDateTime now);

    /**
     * 根据创建人查找费用规则
     * @param createBy 创建人
     * @return 费用规则列表
     */
    List<FeeRule> findByCreateBy(User createBy);

    /**
     * 根据创建人ID查找费用规则
     * @param createById 创建人ID
     * @return 费用规则列表
     */
    @Query("SELECT f FROM FeeRule f WHERE f.createBy.userId = :createById")
    List<FeeRule> findByCreateById(@Param("createById") Integer createById);

    /**
     * 查找即将生效的费用规则
     * @param now 当前时间
     * @return 即将生效的规则列表
     */
    @Query("SELECT f FROM FeeRule f WHERE f.effectiveTime > :now AND f.status = 1")
    List<FeeRule> findUpcomingRules(@Param("now") LocalDateTime now);

    /**
     * 查找已过期的费用规则
     * @param now 当前时间
     * @return 已过期规则列表
     */
    @Query("SELECT f FROM FeeRule f WHERE f.invalidTime < :now")
    List<FeeRule> findExpiredRules(@Param("now") LocalDateTime now);

    /**
     * 根据创建时间范围查找费用规则
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 费用规则列表
     */
    List<FeeRule> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 更新费用规则状态
     * @param ruleId 规则ID
     * @param status 新状态
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE FeeRule f SET f.status = :status, f.updateTime = :updateTime WHERE f.ruleId = :ruleId")
    int updateStatus(@Param("ruleId") Integer ruleId,
                     @Param("status") Integer status,
                     @Param("updateTime") LocalDateTime updateTime);

    /**
     * 更新费用规则金额
     * @param ruleId 规则ID
     * @param unitPrice 单价
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE FeeRule f SET f.unitPrice = :unitPrice, f.updateTime = :updateTime WHERE f.ruleId = :ruleId")
    int updateUnitPrice(@Param("ruleId") Integer ruleId,
                        @Param("unitPrice") BigDecimal unitPrice,
                        @Param("updateTime") LocalDateTime updateTime);

    /**
     * 检查时间段是否已存在有效规则（排除指定规则）
     * @param timeSlot 时间段
     * @param now 当前时间
     * @param ruleId 要排除的规则ID
     * @return 是否存在
     */
    @Query("SELECT COUNT(f) > 0 FROM FeeRule f WHERE f.timeSlot = :timeSlot AND f.ruleId != :ruleId AND f.effectiveTime <= :now AND (f.invalidTime IS NULL OR f.invalidTime >= :now) AND f.status = 1")
    boolean existsActiveRuleByTimeSlot(@Param("timeSlot") String timeSlot,
                                       @Param("now") LocalDateTime now,
                                       @Param("ruleId") Integer ruleId);
}
