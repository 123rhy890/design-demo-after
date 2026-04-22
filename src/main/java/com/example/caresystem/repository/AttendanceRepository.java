package com.example.caresystem.repository;

import com.example.caresystem.entity.Attendance;
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
 * 考勤记录数据访问接口
 * @author rhy
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer>, JpaSpecificationExecutor<Attendance> {

    /**
     * 根据儿童查找考勤记录
     * @param child 儿童
     * @return 考勤记录列表
     */
    List<Attendance> findByChild(Child child);

    /**
     * 根据教师查找考勤记录
     * @param teacher 教师
     * @return 考勤记录列表
     */
    List<Attendance> findByTeacher(User teacher);

    /**
     * 根据预约查找考勤记录
     * @param reservation 预约
     * @return 考勤记录列表
     */
    List<Attendance> findByReservation(Reservation reservation);

    /**
     * 根据儿童ID查找考勤记录
     * @param childId 儿童ID
     * @return 考勤记录列表
     */
    @Query("SELECT a FROM Attendance a WHERE a.child.childId = :childId")
    List<Attendance> findByChildId(@Param("childId") Integer childId);

    /**
     * 根据教师ID查找考勤记录
     * @param teacherId 教师ID
     * @return 考勤记录列表
     */
    @Query("SELECT a FROM Attendance a WHERE a.teacher.userId = :teacherId")
    List<Attendance> findByTeacherId(@Param("teacherId") Integer teacherId);

    /**
     * 根据签到码查找考勤记录
     * @param checkinCode 签到码
     * @return 考勤记录
     */
    Optional<Attendance> findByCheckinCode(String checkinCode);

    /**
     * 根据签退码查找考勤记录
     * @param checkoutCode 签退码
     * @return 考勤记录
     */
    Optional<Attendance> findByCheckoutCode(String checkoutCode);

    /**
     * 根据考勤状态查找考勤记录
     * @param attendStatus 考勤状态
     * @return 考勤记录列表
     */
    List<Attendance> findByAttendStatus(Integer attendStatus);

    /**
     * 根据接送人电话查找考勤记录
     * @param pickPhone 接送人电话
     * @return 考勤记录列表
     */
    List<Attendance> findByPickPhone(String pickPhone);

    /**
     * 根据签到时间范围查找考勤记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 考勤记录列表
     */
    List<Attendance> findByCheckinTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计指定班级在指定时间范围内的考勤人数
     * @param classId 班级ID
     * @param start 开始时间
     * @param end 结束时间
     * @return 考勤人数
     */
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.child.classInfo.classId = :classId AND a.checkinTime >= :start AND a.checkinTime < :end")
    Long countAttendanceByClassAndDate(@Param("classId") Integer classId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    /**
     * 根据儿童和签到日期查找考勤记录
     * @param child 儿童
     * @param checkinDate 签到日期（转换为LocalDate）
     * @return 考勤记录列表
     */
    @Query("SELECT a FROM Attendance a WHERE a.child = :child AND DATE(a.checkinTime) = :checkinDate")
    List<Attendance> findByChildAndCheckinDate(@Param("child") Child child,
                                               @Param("checkinDate") LocalDate checkinDate);

    /**
     * 查找今日考勤记录
     * @return 今日考勤记录列表
     */
    @Query("SELECT a FROM Attendance a WHERE DATE(a.checkinTime) = CURRENT_DATE")
    List<Attendance> findTodayAttendances();

    /**
     * 查找儿童今日考勤记录
     * @param childId 儿童ID
     * @return 考勤记录
     */
    @Query("SELECT a FROM Attendance a WHERE a.child.childId = :childId AND DATE(a.checkinTime) = CURRENT_DATE")
    Optional<Attendance> findTodayAttendanceByChildId(@Param("childId") Integer childId);

    /**
     * 查找未签退的考勤记录
     * @return 未签退记录列表
     */
    @Query("SELECT a FROM Attendance a WHERE a.checkoutTime IS NULL")
    List<Attendance> findNotCheckedOutAttendances();

    /**
     * 查找班级今日考勤记录
     * @param classId 班级ID
     * @return 考勤记录列表
     */
    @Query("SELECT a FROM Attendance a JOIN a.child c WHERE c.classInfo.classId = :classId AND DATE(a.checkinTime) = CURRENT_DATE")
    List<Attendance> findTodayAttendancesByClassId(@Param("classId") Integer classId);

    /**
     * 统计儿童当月考勤数量
     * @param childId 儿童ID
     * @param year 年份
     * @param month 月份
     * @return 考勤数量
     */
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.child.childId = :childId AND YEAR(a.checkinTime) = :year AND MONTH(a.checkinTime) = :month")
    Long countMonthlyAttendance(@Param("childId") Integer childId,
                                @Param("year") Integer year,
                                @Param("month") Integer month);

    /**
     * 更新签退信息
     * @param attendanceId 考勤ID
     * @param checkoutTime 签退时间
     * @param attendStatus 考勤状态
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Attendance a SET a.checkoutTime = :checkoutTime, a.attendStatus = :attendStatus, a.updateTime = :updateTime WHERE a.attendanceId = :attendanceId")
    int updateCheckout(@Param("attendanceId") Integer attendanceId,
                       @Param("checkoutTime") LocalDateTime checkoutTime,
                       @Param("attendStatus") Integer attendStatus,
                       @Param("updateTime") LocalDateTime updateTime);

    /**
     * 更新考勤状态
     * @param attendanceId 考勤ID
     * @param attendStatus 考勤状态
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Attendance a SET a.attendStatus = :attendStatus, a.updateTime = :updateTime WHERE a.attendanceId = :attendanceId")
    int updateAttendStatus(@Param("attendanceId") Integer attendanceId,
                           @Param("attendStatus") Integer attendStatus,
                           @Param("updateTime") LocalDateTime updateTime);

    /**
     * 分页查询儿童考勤记录
     * @param childId 儿童ID
     * @param pageable 分页参数
     * @return 考勤分页数据
     */
    @Query("SELECT a FROM Attendance a WHERE a.child.childId = :childId ORDER BY a.checkinTime DESC")
    Page<Attendance> findByChildId(@Param("childId") Integer childId, Pageable pageable);

    /**
     * 根据日期范围查询考勤记录
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 考勤记录列表
     */
    @Query("SELECT a FROM Attendance a WHERE DATE(a.checkinTime) BETWEEN :startDate AND :endDate")
    List<Attendance> findByCheckinDateBetween(@Param("startDate") LocalDate startDate,
                                              @Param("endDate") LocalDate endDate);

    /**
     * 统计班级考勤数据
     * @param classId 班级ID
     * @param date 日期
     * @return 考勤统计数组 [正常, 迟到, 早退, 缺勤]
     */
    @Query("SELECT " +
            "SUM(CASE WHEN a.attendStatus = 1 THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.attendStatus = 0 THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.attendStatus = 2 THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN a.attendStatus = 3 THEN 1 ELSE 0 END) " +
            "FROM Attendance a JOIN a.child c " +
            "WHERE c.classInfo.classId = :classId AND DATE(a.checkinTime) = :date")
    List<Object[]> countAttendanceByClassAndDate(@Param("classId") Integer classId,
                                                 @Param("date") LocalDate date);
}
