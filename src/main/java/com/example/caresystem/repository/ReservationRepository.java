package com.example.caresystem.repository;

import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.Reservation;
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
 * 预约记录数据访问接口
 * @author rhy
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>, JpaSpecificationExecutor<Reservation> {

    /**
     * 根据儿童查找预约记录
     * @param child 儿童
     * @return 预约记录列表
     */
    List<Reservation> findByChild(Child child);

    /**
     * 根据家长查找预约记录
     * @param parent 家长
     * @return 预约记录列表
     */
    List<Reservation> findByParent(User parent);

    /**
     * 根据儿童ID查找预约记录
     * @param childId 儿童ID
     * @return 预约记录列表
     */
    @Query("SELECT r FROM Reservation r WHERE r.child.childId = :childId")
    List<Reservation> findByChildId(@Param("childId") Integer childId);

    /**
     * 根据家长ID查找预约记录
     * @param parentId 家长ID
     * @return 预约记录列表
     */
    @Query("SELECT r FROM Reservation r WHERE r.parent.userId = :parentId")
    List<Reservation> findByParentId(@Param("parentId") Integer parentId);

    /**
     * 根据预约日期查找预约记录
     * @param reserveDate 预约日期
     * @return 预约记录列表
     */
    List<Reservation> findByReserveDate(LocalDate reserveDate);

    /**
     * 根据预约状态查找预约记录
     * @param reserveStatus 预约状态
     * @return 预约记录列表
     */
    List<Reservation> findByReserveStatus(String reserveStatus);

    /**
     * 根据审核人查找预约记录
     * @param auditor 审核人
     * @return 预约记录列表
     */
    List<Reservation> findByAuditor(User auditor);

    /**
     * 根据儿童和预约状态查找预约记录
     * @param child 儿童
     * @param reserveStatus 预约状态
     * @return 预约记录列表
     */
    List<Reservation> findByChildAndReserveStatus(Child child, String reserveStatus);

    /**
     * 根据儿童和预约日期查找预约记录
     * @param child 儿童
     * @param reserveDate 预约日期
     * @return 预约记录列表
     */
    List<Reservation> findByChildAndReserveDate(Child child, LocalDate reserveDate);

    /**
     * 根据儿童、预约日期和状态查找预约记录
     * @param child 儿童
     * @param reserveDate 预约日期
     * @param reserveStatus 预约状态
     * @return 预约记录列表
     */
    Optional<Reservation> findByChildAndReserveDateAndReserveStatus(Child child, LocalDate reserveDate, String reserveStatus);

    /**
     * 查找待审核的预约记录
     * @return 待审核预约列表
     */
    @Query("SELECT r FROM Reservation r WHERE r.reserveStatus = 'review' ORDER BY r.createTime DESC")
    List<Reservation> findPendingReservations();

    /**
     * 查找已确认的预约记录
     * @param reserveDate 预约日期
     * @return 已确认预约列表
     */
    @Query("SELECT r FROM Reservation r WHERE r.reserveStatus = 'confirm' AND r.reserveDate = :reserveDate")
    List<Reservation> findConfirmedReservationsByDate(@Param("reserveDate") LocalDate reserveDate);

    /**
     * 统计儿童在指定日期的预约数量
     * @param childId 儿童ID
     * @param reserveDate 预约日期
     * @param status 状态
     * @return 预约数量
     */
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.child.childId = :childId AND r.reserveDate = :reserveDate AND r.reserveStatus = :status")
    Long countByChildAndDateAndStatus(@Param("childId") Integer childId,
                                      @Param("reserveDate") LocalDate reserveDate,
                                      @Param("status") String status);

    /**
     * 统计班级在指定日期的预约数量
     * @param classId 班级ID
     * @param reserveDate 预约日期
     * @param timeSlot 时间段
     * @return 预约数量
     */
    @Query("SELECT COUNT(r) FROM Reservation r JOIN r.child c WHERE c.classInfo.classId = :classId AND r.reserveDate = :reserveDate AND r.timeSlot = :timeSlot AND r.reserveStatus = 'confirm'")
    Long countByClassAndDateAndTimeSlot(@Param("classId") Integer classId,
                                        @Param("reserveDate") LocalDate reserveDate,
                                        @Param("timeSlot") String timeSlot);

    /**
     * 更新预约状态
     * @param reservationId 预约ID
     * @param reserveStatus 新状态
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Reservation r SET r.reserveStatus = :reserveStatus, r.updateTime = :updateTime WHERE r.reservationId = :reservationId")
    int updateStatus(@Param("reservationId") Integer reservationId,
                     @Param("reserveStatus") String reserveStatus,
                     @Param("updateTime") LocalDateTime updateTime);

    /**
     * 审核预约
     * @param reservationId 预约ID
     * @param reserveStatus 审核后状态
     * @param auditor 审核人
     * @param auditTime 审核时间
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Reservation r SET r.reserveStatus = :reserveStatus, r.auditor = :auditor, r.auditTime = :auditTime, r.updateTime = :updateTime WHERE r.reservationId = :reservationId")
    int auditReservation(@Param("reservationId") Integer reservationId,
                         @Param("reserveStatus") String reserveStatus,
                         @Param("auditor") User auditor,
                         @Param("auditTime") LocalDateTime auditTime,
                         @Param("updateTime") LocalDateTime updateTime);

    /**
     * 查找指定日期范围内的预约记录
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 预约记录列表
     */
    List<Reservation> findByReserveDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * 分页查询家长预约记录
     * @param parentId 家长ID
     * @param status 状态
     * @param pageable 分页参数
     * @return 预约分页数据
     */
    @Query("SELECT r FROM Reservation r WHERE r.parent.userId = :parentId AND (:status IS NULL OR r.reserveStatus = :status)")
    Page<Reservation> findByParentIdAndStatus(@Param("parentId") Integer parentId,
                                              @Param("status") String status,
                                              Pageable pageable);

    /**
     * 删除过期预约记录
     * @param currentDate 当前日期
     * @return 删除记录数
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Reservation r WHERE r.reserveDate < :currentDate AND r.reserveStatus = 'review'")
    int deleteExpiredReservations(@Param("currentDate") LocalDate currentDate);
}
