package com.example.caresystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 动态查询接口（用于复杂统计和多表查询）
 * 使用原生SQL或JPQL实现复杂的统计查询
 * @author rhy
 */
@Repository
public interface DynamicQueryRepository {

    /**
     * 通用统计接口 - 统计数量
     */
    @Query(value = "SELECT COUNT(*) FROM :tableName", nativeQuery = true)
    Long countByTable(@Param("tableName") String tableName);

    /**
     * 统计今日新增数量
     */
    @Query(value = "SELECT COUNT(*) FROM :tableName WHERE DATE(create_time) = CURDATE()", nativeQuery = true)
    Long countTodayByTable(@Param("tableName") String tableName);

    /**
     * 统计本月新增数量
     */
    @Query(value = "SELECT COUNT(*) FROM :tableName WHERE YEAR(create_time) = YEAR(CURDATE()) AND MONTH(create_time) = MONTH(CURDATE())", nativeQuery = true)
    Long countMonthByTable(@Param("tableName") String tableName);

    /**
     * 统计用户角色分布
     */
    @Query(value = "SELECT role_type as roleType, COUNT(*) as count FROM t_user GROUP BY role_type", nativeQuery = true)
    List<Map<String, Object>> countUserByRole();

    /**
     * 统计班级儿童数量分布
     */
    @Query(value = "SELECT c.class_name as className, COUNT(c.child_id) as childCount FROM t_child c JOIN t_class cl ON c.class_id = cl.class_id GROUP BY c.class_id", nativeQuery = true)
    List<Map<String, Object>> countChildrenByClass();

    /**
     * 统计每日考勤数据
     */
    @Query(value = "SELECT DATE(checkin_time) as date, " +
            "COUNT(*) as total, " +
            "SUM(CASE WHEN attend_status = 1 THEN 1 ELSE 0 END) as normal, " +
            "SUM(CASE WHEN attend_status = 0 THEN 1 ELSE 0 END) as late, " +
            "SUM(CASE WHEN attend_status = 2 THEN 1 ELSE 0 END) as early, " +
            "SUM(CASE WHEN attend_status = 3 THEN 1 ELSE 0 END) as absent " +
            "FROM t_attendance " +
            "WHERE DATE(checkin_time) >= :startDate AND DATE(checkin_time) <= :endDate " +
            "GROUP BY DATE(checkin_time) " +
            "ORDER BY date", nativeQuery = true)
    List<Map<String, Object>> countDailyAttendance(@Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    /**
     * 统计月度账单数据
     */
    @Query(value = "SELECT bill_month as month, " +
            "COUNT(*) as totalBills, " +
            "SUM(payable_amount) as totalAmount, " +
            "SUM(actual_amount) as paidAmount, " +
            "SUM(CASE WHEN payment_status = 0 THEN payable_amount ELSE 0 END) as unpaidAmount " +
            "FROM t_fee_bill " +
            "WHERE bill_month >= :startMonth AND bill_month <= :endMonth " +
            "GROUP BY bill_month " +
            "ORDER BY month", nativeQuery = true)
    List<Map<String, Object>> countMonthlyBills(@Param("startMonth") String startMonth,
                                                @Param("endMonth") String endMonth);

    /**
     * 查询教师工作统计
     */
    @Query(value = "SELECT u.username as teacherName, " +
            "COUNT(DISTINCT a.attendance_id) as attendanceCount, " +
            "COUNT(DISTINCT d.record_id) as dailyRecordCount, " +
            "COUNT(DISTINCT c.comm_id) as communicationCount " +
            "FROM t_user u " +
            "LEFT JOIN t_attendance a ON u.user_id = a.teacher_id AND DATE(a.checkin_time) >= :startDate AND DATE(a.checkin_time) <= :endDate " +
            "LEFT JOIN t_daily_status d ON u.user_id = d.teacher_id AND d.record_date >= :startDate AND d.record_date <= :endDate " +
            "LEFT JOIN t_communication c ON u.user_id = c.send_id AND DATE(c.send_time) >= :startDate AND DATE(c.send_time) <= :endDate " +
            "WHERE u.role_type = 1 " +
            "GROUP BY u.user_id " +
            "ORDER BY attendanceCount DESC", nativeQuery = true)
    List<Map<String, Object>> getTeacherWorkStats(@Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate);

    /**
     * 查询儿童月度的托管统计
     */
    @Query(value = "SELECT c.child_name as childName, " +
            "COUNT(DISTINCT DATE(a.checkin_time)) as attendanceDays, " +
            "SUM(CASE WHEN a.attend_status = 1 THEN 1 ELSE 0 END) as normalDays, " +
            "SUM(CASE WHEN a.attend_status IN (0,2) THEN 1 ELSE 0 END) as abnormalDays " +
            "FROM t_child c " +
            "LEFT JOIN t_attendance a ON c.child_id = a.child_id AND YEAR(a.checkin_time) = :year AND MONTH(a.checkin_time) = :month " +
            "GROUP BY c.child_id " +
            "ORDER BY attendanceDays DESC", nativeQuery = true)
    List<Map<String, Object>> getChildMonthlyStats(@Param("year") Integer year,
                                                   @Param("month") Integer month);

    /**
     * 查询班级出勤率排名
     */
    @Query(value = "SELECT cl.class_name as className, " +
            "COUNT(DISTINCT a.attendance_id) as totalRecords, " +
            "SUM(CASE WHEN a.attend_status = 1 THEN 1 ELSE 0 END) as normalRecords, " +
            "ROUND(SUM(CASE WHEN a.attend_status = 1 THEN 1 ELSE 0 END) * 100.0 / COUNT(DISTINCT a.attendance_id), 2) as attendanceRate " +
            "FROM t_class cl " +
            "LEFT JOIN t_child c ON cl.class_id = c.class_id " +
            "LEFT JOIN t_attendance a ON c.child_id = a.child_id AND DATE(a.checkin_time) >= :startDate AND DATE(a.checkin_time) <= :endDate " +
            "GROUP BY cl.class_id " +
            "ORDER BY attendanceRate DESC", nativeQuery = true)
    List<Map<String, Object>> getClassAttendanceRanking(@Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate);

    /**
     * 查询系统活跃度统计
     */
    @Query(value = "SELECT DATE(create_time) as date, " +
            "SUM(CASE WHEN table_name = 't_reservation' THEN 1 ELSE 0 END) as reservationCount, " +
            "SUM(CASE WHEN table_name = 't_attendance' THEN 1 ELSE 0 END) as attendanceCount, " +
            "SUM(CASE WHEN table_name = 't_daily_status' THEN 1 ELSE 0 END) as dailyCount, " +
            "SUM(CASE WHEN table_name = 't_communication' THEN 1 ELSE 0 END) as communicationCount " +
            "FROM ( " +
            "  SELECT 't_reservation' as table_name, create_time FROM t_reservation " +
            "  UNION ALL " +
            "  SELECT 't_attendance' as table_name, create_time FROM t_attendance " +
            "  UNION ALL " +
            "  SELECT 't_daily_status' as table_name, create_time FROM t_daily_status " +
            "  UNION ALL " +
            "  SELECT 't_communication' as table_name, create_time FROM t_communication " +
            ") as all_tables " +
            "WHERE DATE(create_time) >= :startDate AND DATE(create_time) <= :endDate " +
            "GROUP BY DATE(create_time) " +
            "ORDER BY date", nativeQuery = true)
    List<Map<String, Object>> getSystemActivityStats(@Param("startDate") LocalDate startDate,
                                                     @Param("endDate") LocalDate endDate);
}
