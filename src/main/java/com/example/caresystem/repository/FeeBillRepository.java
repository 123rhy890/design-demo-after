package com.example.caresystem.repository;

import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.FeeBill;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 费用账单数据访问接口
 * @author rhy
 */
@Repository
public interface FeeBillRepository extends JpaRepository<FeeBill, Integer>, JpaSpecificationExecutor<FeeBill> {

    /**
     * 根据儿童查找账单
     * @param child 儿童
     * @return 账单列表
     */
    List<FeeBill> findByChild(Child child);

    /**
     * 根据家长查找账单
     * @param parent 家长
     * @return 账单列表
     */
    List<FeeBill> findByParent(User parent);

    /**
     * 根据儿童ID查找账单
     * @param childId 儿童ID
     * @return 账单列表
     */
    @Query("SELECT f FROM FeeBill f WHERE f.child.childId = :childId")
    List<FeeBill> findByChildId(@Param("childId") Integer childId);

    /**
     * 根据家长ID查找账单
     * @param parentId 家长ID
     * @return 账单列表
     */
    @Query("SELECT f FROM FeeBill f WHERE f.parent.userId = :parentId")
    List<FeeBill> findByParentId(@Param("parentId") Integer parentId);

    /**
     * 根据账单月份查找账单
     * @param billMonth 账单月份（yyyy-MM）
     * @return 账单列表
     */
    List<FeeBill> findByBillMonth(String billMonth);

    /**
     * 根据缴费状态查找账单
     * @param paymentStatus 缴费状态
     * @return 账单列表
     */
    List<FeeBill> findByPaymentStatus(Integer paymentStatus);

    /**
     * 根据儿童和账单月份查找账单
     * @param child 儿童
     * @param billMonth 账单月份
     * @return 账单
     */
    Optional<FeeBill> findByChildAndBillMonth(Child child, String billMonth);

    /**
     * 根据儿童ID和账单月份查找账单
     * @param childId 儿童ID
     * @param billMonth 账单月份
     * @return 账单
     */
    @Query("SELECT f FROM FeeBill f WHERE f.child.childId = :childId AND f.billMonth = :billMonth")
    Optional<FeeBill> findByChildIdAndBillMonth(@Param("childId") Integer childId,
                                                @Param("billMonth") String billMonth);

    /**
     * 根据家长和缴费状态查找账单
     * @param parentId 家长ID
     * @param paymentStatus 缴费状态
     * @return 账单列表
     */
    @Query("SELECT f FROM FeeBill f WHERE f.parent.userId = :parentId AND f.paymentStatus = :paymentStatus")
    List<FeeBill> findByParentIdAndPaymentStatus(@Param("parentId") Integer parentId,
                                                 @Param("paymentStatus") Integer paymentStatus);

    /**
     * 查找待缴费账单
     * @return 待缴费账单列表
     */
    @Query("SELECT f FROM FeeBill f WHERE f.paymentStatus = 0 ORDER BY f.paymentDeadline ASC")
    List<FeeBill> findUnpaidBills();

    /**
     * 查找超期未缴费账单
     * @param currentTime 当前时间
     * @return 超期账单列表
     */
    @Query("SELECT f FROM FeeBill f WHERE f.paymentStatus = 0 AND f.paymentDeadline < :currentTime")
    List<FeeBill> findOverdueBills(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 查找即将到期的账单
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 即将到期账单列表
     */
    @Query("SELECT f FROM FeeBill f WHERE f.paymentStatus = 0 AND f.paymentDeadline BETWEEN :startTime AND :endTime")
    List<FeeBill> findUpcomingDueBills(@Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 统计待缴费账单总金额
     * @return 待缴费总金额
     */
    @Query("SELECT SUM(f.payableAmount) FROM FeeBill f WHERE f.paymentStatus = 0")
    BigDecimal sumUnpaidAmount();

    /**
     * 统计家长待缴费金额
     * @param parentId 家长ID
     * @return 待缴费金额
     */
    @Query("SELECT SUM(f.payableAmount) FROM FeeBill f WHERE f.parent.userId = :parentId AND f.paymentStatus = 0")
    BigDecimal sumUnpaidAmountByParentId(@Param("parentId") Integer parentId);

    /**
     * 统计指定月份账单金额
     * @param billMonth 账单月份
     * @param paymentStatus 缴费状态
     * @return 账单总金额
     */
    @Query("SELECT SUM(f.actualAmount) FROM FeeBill f WHERE f.billMonth = :billMonth AND f.paymentStatus = :paymentStatus")
    BigDecimal sumAmountByMonthAndStatus(@Param("billMonth") String billMonth,
                                         @Param("paymentStatus") Integer paymentStatus);

    /**
     * 更新缴费状态
     * @param billId 账单ID
     * @param paymentStatus 缴费状态
     * @param actualAmount 实付金额
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE FeeBill f SET f.paymentStatus = :paymentStatus, f.actualAmount = :actualAmount, f.updateTime = :updateTime WHERE f.billId = :billId")
    int updatePaymentStatus(@Param("billId") Integer billId,
                            @Param("paymentStatus") Integer paymentStatus,
                            @Param("actualAmount") BigDecimal actualAmount,
                            @Param("updateTime") LocalDateTime updateTime);

    /**
     * 更新提醒信息
     * @param billId 账单ID
     * @param remindTimes 提醒次数
     * @param lastRemindTime 最后提醒时间
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE FeeBill f SET f.remindTimes = :remindTimes, f.lastRemindTime = :lastRemindTime, f.updateTime = :updateTime WHERE f.billId = :billId")
    int updateReminderInfo(@Param("billId") Integer billId,
                           @Param("remindTimes") Integer remindTimes,
                           @Param("lastRemindTime") LocalDateTime lastRemindTime,
                           @Param("updateTime") LocalDateTime updateTime);

    /**
     * 分页查询家长账单
     * @param parentId 家长ID
     * @param pageable 分页参数
     * @return 账单分页数据
     */
    @Query("SELECT f FROM FeeBill f WHERE f.parent.userId = :parentId ORDER BY f.billMonth DESC")
    Page<FeeBill> findByParentIdPage(@Param("parentId") Integer parentId, Pageable pageable);

    /**
     * 查找最近6个月的账单数据
     * @param childId 儿童ID
     * @param limit 条数
     * @return 账单列表
     */
    @Query(value = "SELECT * FROM t_fee_bill WHERE child_id = :childId ORDER BY bill_month DESC LIMIT :limit", nativeQuery = true)
    List<FeeBill> findRecentBillsByChildId(@Param("childId") Integer childId,
                                           @Param("limit") Integer limit);

    /**
     * 分页条件查询账单（管理员端）
     */
    @Query("SELECT f FROM FeeBill f WHERE " +
           "(:childName IS NULL OR f.child.childName LIKE %:childName%) AND " +
           "(:paymentStatus IS NULL OR f.paymentStatus = :paymentStatus) AND " +
           "(:billMonth IS NULL OR f.billMonth = :billMonth) " +
           "ORDER BY f.createTime DESC")
    Page<FeeBill> findByConditions(@Param("childName") String childName,
                                   @Param("paymentStatus") Integer paymentStatus,
                                   @Param("billMonth") String billMonth,
                                   Pageable pageable);

    /**
     * 统计指定月份应收总额
     */
    @Query("SELECT COALESCE(SUM(f.payableAmount), 0) FROM FeeBill f WHERE f.billMonth = :billMonth")
    BigDecimal sumPayableByMonth(@Param("billMonth") String billMonth);

    /**
     * 统计指定月份已收总额
     */
    @Query("SELECT COALESCE(SUM(f.actualAmount), 0) FROM FeeBill f WHERE f.billMonth = :billMonth AND f.paymentStatus = 1")
    BigDecimal sumActualByMonth(@Param("billMonth") String billMonth);

    /**
     * 统计指定月份欠费人数（未缴费、欠费、审核中均计入待处理）
     */
    @Query("SELECT COUNT(f) FROM FeeBill f WHERE f.billMonth = :billMonth AND f.paymentStatus IN (0, 2, 4)")
    Long countUnpaidByMonth(@Param("billMonth") String billMonth);

    /**
     * 按月份统计已收金额（用于图表）
     */
    @Query("SELECT f.billMonth, COALESCE(SUM(f.payableAmount), 0), COALESCE(SUM(f.actualAmount), 0) " +
           "FROM FeeBill f WHERE f.billMonth IN :months GROUP BY f.billMonth ORDER BY f.billMonth")
    List<Object[]> sumAmountGroupByMonth(@Param("months") List<String> months);

    /**
     * 更新账单提醒信息
     */
    @Modifying
    @Transactional
    @Query("UPDATE FeeBill f SET f.remindTimes = f.remindTimes + 1, f.lastRemindTime = :now, f.updateTime = :now WHERE f.billId = :billId")
    int incrementRemindTimes(@Param("billId") Integer billId, @Param("now") LocalDateTime now);
}
