package com.example.caresystem.repository;

import com.example.caresystem.entity.FeeBill;
import com.example.caresystem.entity.User;
import com.example.caresystem.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * 缴费凭证数据访问接口
 * @author rhy
 */
@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer>, JpaSpecificationExecutor<Voucher> {

    /**
     * 根据账单查找凭证
     * @param feeBill 账单
     * @return 凭证列表
     */
    List<Voucher> findByFeeBill(FeeBill feeBill);

    /**
     * 根据家长查找凭证
     * @param parent 家长
     * @return 凭证列表
     */
    List<Voucher> findByParent(User parent);

    /**
     * 根据审核人查找凭证
     * @param auditor 审核人
     * @return 凭证列表
     */
    List<Voucher> findByAuditor(User auditor);

    /**
     * 根据审核状态查找凭证
     * @param auditStatus 审核状态
     * @return 凭证列表
     */
    List<Voucher> findByAuditStatus(Integer auditStatus);

    /**
     * 根据账单ID查找凭证
     * @param billId 账单ID
     * @return 凭证列表
     */
    @Query("SELECT v FROM Voucher v WHERE v.feeBill.billId = :billId")
    List<Voucher> findByBillId(@Param("billId") Integer billId);

    /**
     * 根据家长ID查找凭证
     * @param parentId 家长ID
     * @return 凭证列表
     */
    @Query("SELECT v FROM Voucher v WHERE v.parent.userId = :parentId")
    List<Voucher> findByParentId(@Param("parentId") Integer parentId);

    /**
     * 查找待审核凭证
     * @return 待审核凭证列表
     */
    @Query("SELECT v FROM Voucher v WHERE v.auditStatus = 0 ORDER BY v.uploadTime DESC")
    List<Voucher> findPendingVouchers();

    /**
     * 查找已审核通过的凭证
     * @return 审核通过凭证列表
     */
    @Query("SELECT v FROM Voucher v WHERE v.auditStatus = 1 ORDER BY v.auditTime DESC")
    List<Voucher> findApprovedVouchers();

    /**
     * 根据账单和审核状态查找凭证
     * @param billId 账单ID
     * @param auditStatus 审核状态
     * @return 凭证
     */
    @Query("SELECT v FROM Voucher v WHERE v.feeBill.billId = :billId AND v.auditStatus = :auditStatus")
    Optional<Voucher> findByBillIdAndAuditStatus(@Param("billId") Integer billId,
                                                 @Param("auditStatus") Integer auditStatus);

    /**
     * 统计待审核凭证数量
     * @return 待审核凭证数量
     */
    Long countByAuditStatus(Integer auditStatus);

    /**
     * 根据上传时间范围查找凭证
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 凭证列表
     */
    List<Voucher> findByUploadTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 更新审核状态
     * @param voucherId 凭证ID
     * @param auditStatus 审核状态
     * @param auditor 审核人
     * @param auditTime 审核时间
     * @param auditRemark 审核备注
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Voucher v SET v.auditStatus = :auditStatus, v.auditor = :auditor, v.auditTime = :auditTime, v.auditRemark = :auditRemark, v.updateTime = :updateTime WHERE v.voucherId = :voucherId")
    int updateAuditStatus(@Param("voucherId") Integer voucherId,
                          @Param("auditStatus") Integer auditStatus,
                          @Param("auditor") User auditor,
                          @Param("auditTime") LocalDateTime auditTime,
                          @Param("auditRemark") String auditRemark,
                          @Param("updateTime") LocalDateTime updateTime);

    /**
     * 删除指定账单的凭证
     * @param billId 账单ID
     * @return 删除记录数
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Voucher v WHERE v.feeBill.billId = :billId")
    int deleteByBillId(@Param("billId") Integer billId);

    /**
     * 分页查询家长凭证
     * @param parentId 家长ID
     * @param pageable 分页参数
     * @return 凭证分页数据
     */
    @Query("SELECT v FROM Voucher v WHERE v.parent.userId = :parentId ORDER BY v.uploadTime DESC")
    Page<Voucher> findByParentId(@Param("parentId") Integer parentId, Pageable pageable);

    /**
     * 分页查询待审核凭证
     * @param pageable 分页参数
     * @return 待审核凭证分页数据
     */
    @Query("SELECT v FROM Voucher v WHERE v.auditStatus = 0 ORDER BY v.uploadTime DESC")
    Page<Voucher> findPendingVouchers(Pageable pageable);
}
