package com.example.caresystem.repository;

import com.example.caresystem.entity.DiscountRule;
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
 * 优惠规则数据访问接口
 * @author rhy
 */
@Repository
public interface DiscountRuleRepository extends JpaRepository<DiscountRule, Integer>, JpaSpecificationExecutor<DiscountRule> {

    /**
     * 根据优惠名称查找规则
     * @param discountName 优惠名称
     * @return 优惠规则
     */
    Optional<DiscountRule> findByDiscountName(String discountName);

    /**
     * 根据优惠类型查找规则
     * @param discountType 优惠类型
     * @return 优惠规则列表
     */
    List<DiscountRule> findByDiscountType(Integer discountType);

    /**
     * 根据状态查找优惠规则
     * @param status 状态
     * @return 优惠规则列表
     */
    List<DiscountRule> findByStatus(Integer status);

    /**
     * 查找有效的优惠规则
     * @param now 当前时间
     * @return 有效优惠规则列表
     */
    @Query("SELECT d FROM DiscountRule d WHERE d.effectiveTime <= :now AND (d.invalidTime IS NULL OR d.invalidTime >= :now) AND d.status = 1")
    List<DiscountRule> findActiveRules(@Param("now") LocalDateTime now);

    /**
     * 根据创建人查找优惠规则
     * @param createBy 创建人
     * @return 优惠规则列表
     */
    List<DiscountRule> findByCreateBy(User createBy);

    /**
     * 根据创建人ID查找优惠规则
     * @param createById 创建人ID
     * @return 优惠规则列表
     */
    @Query("SELECT d FROM DiscountRule d WHERE d.createBy.userId = :createById")
    List<DiscountRule> findByCreateById(@Param("createById") Integer createById);

    /**
     * 查找指定类型的有效优惠规则
     * @param discountType 优惠类型
     * @param now 当前时间
     * @return 优惠规则列表
     */
    @Query("SELECT d FROM DiscountRule d WHERE d.discountType = :discountType AND d.effectiveTime <= :now AND (d.invalidTime IS NULL OR d.invalidTime >= :now) AND d.status = 1")
    List<DiscountRule> findActiveRulesByType(@Param("discountType") Integer discountType,
                                             @Param("now") LocalDateTime now);

    /**
     * 查找即将生效的优惠规则
     * @param now 当前时间
     * @return 即将生效规则列表
     */
    @Query("SELECT d FROM DiscountRule d WHERE d.effectiveTime > :now AND d.status = 1")
    List<DiscountRule> findUpcomingRules(@Param("now") LocalDateTime now);

    /**
     * 查找已过期的优惠规则
     * @param now 当前时间
     * @return 已过期规则列表
     */
    @Query("SELECT d FROM DiscountRule d WHERE d.invalidTime < :now")
    List<DiscountRule> findExpiredRules(@Param("now") LocalDateTime now);

    /**
     * 根据创建时间范围查找优惠规则
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 优惠规则列表
     */
    List<DiscountRule> findByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 更新优惠规则状态
     * @param discountId 优惠规则ID
     * @param status 新状态
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE DiscountRule d SET d.status = :status, d.updateTime = :updateTime WHERE d.discountId = :discountId")
    int updateStatus(@Param("discountId") Integer discountId,
                     @Param("status") Integer status,
                     @Param("updateTime") LocalDateTime updateTime);

    /**
     * 更新优惠值
     * @param discountId 优惠规则ID
     * @param discountValue 优惠值
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE DiscountRule d SET d.discountValue = :discountValue, d.updateTime = :updateTime WHERE d.discountId = :discountId")
    int updateDiscountValue(@Param("discountId") Integer discountId,
                            @Param("discountValue") BigDecimal discountValue,
                            @Param("updateTime") LocalDateTime updateTime);

    /**
     * 检查优惠名称是否已存在（排除指定规则）
     * @param discountName 优惠名称
     * @param discountId 要排除的规则ID
     * @return 是否存在
     */
    boolean existsByDiscountNameAndDiscountIdNot(String discountName, Integer discountId);

    /**
     * 统计有效优惠规则数量
     * @param now 当前时间
     * @return 有效规则数量
     */
    @Query("SELECT COUNT(d) FROM DiscountRule d WHERE d.effectiveTime <= :now AND (d.invalidTime IS NULL OR d.invalidTime >= :now) AND d.status = 1")
    Long countActiveRules(@Param("now") LocalDateTime now);
}
