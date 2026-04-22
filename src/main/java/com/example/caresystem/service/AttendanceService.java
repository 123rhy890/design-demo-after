package com.example.caresystem.service;

import com.example.caresystem.entity.Attendance;
import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.Reservation;
import com.example.caresystem.entity.User;
import com.example.caresystem.enums.BusinessEnums;
import com.example.caresystem.enums.UserEnums;
import com.example.caresystem.repository.AttendanceRepository;
import com.example.caresystem.repository.ChildRepository;
import com.example.caresystem.repository.ReservationRepository;
import com.example.caresystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional
    public Attendance addAttendance(Attendance attendance, Integer childId, Integer teacherId, Integer reserveId) {
        if (childId == null) {
            throw new RuntimeException("儿童ID不能为空");
        }
        if (teacherId == null) {
            throw new RuntimeException("教师ID不能为空");
        }
        if (!StringUtils.hasText(attendance.getPickPerson())) {
            throw new RuntimeException("接送人姓名不能为空");
        }
        if (!StringUtils.hasText(attendance.getPickPhone())) {
            throw new RuntimeException("接送人电话不能为空");
        }

        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("儿童不存在"));
        attendance.setChild(child);

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("教师不存在"));
        if (!UserEnums.Role.TEACHER.getCode().equals(teacher.getRoleType())) {
            throw new RuntimeException("该用户不是教师角色");
        }
        attendance.setTeacher(teacher);

        if (reserveId != null) {
            Reservation reservation = reservationRepository.findById(reserveId)
                    .orElseThrow(() -> new RuntimeException("预约记录不存在"));
            attendance.setReservation(reservation);
        }

        attendance.setCheckinCode(generateCode());
        attendance.setCheckoutCode(generateCode());
        attendance.setAttendStatus(BusinessEnums.AttendanceStatus.NORMAL.getCode());

        return attendanceRepository.save(attendance);
    }

    public Attendance getAttendanceById(Integer id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("考勤记录不存在"));
    }

    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }

    public List<Attendance> getAttendancesByChild(Integer childId) {
        return attendanceRepository.findByChildId(childId);
    }

    public List<Attendance> getAttendancesByTeacher(Integer teacherId) {
        return attendanceRepository.findByTeacherId(teacherId);
    }

    public List<Attendance> getAttendancesByStatus(Integer attendStatus) {
        return attendanceRepository.findByAttendStatus(attendStatus);
    }

    public Attendance getAttendanceByCheckinCode(String checkinCode) {
        return attendanceRepository.findByCheckinCode(checkinCode)
                .orElseThrow(() -> new RuntimeException("签到码不存在"));
    }

    public Attendance getAttendanceByCheckoutCode(String checkoutCode) {
        return attendanceRepository.findByCheckoutCode(checkoutCode)
                .orElseThrow(() -> new RuntimeException("签退码不存在"));
    }

    @Transactional
    public Attendance checkin(Integer attendanceId, String checkinCode) {
        Attendance attendance = getAttendanceById(attendanceId);

        if (!attendance.getCheckinCode().equals(checkinCode)) {
            throw new RuntimeException("签到码错误");
        }
        if (attendance.getCheckinTime() != null) {
            throw new RuntimeException("已签到，无需重复操作");
        }

        attendance.setCheckinTime(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }

    @Transactional
    public Attendance checkout(Integer attendanceId, String checkoutCode, String pickPerson, String pickPhone) {
        Attendance attendance = getAttendanceById(attendanceId);

        if (!attendance.getCheckoutCode().equals(checkoutCode)) {
            throw new RuntimeException("签退码错误");
        }
        if (attendance.getCheckoutTime() != null) {
            throw new RuntimeException("已签退，无需重复操作");
        }
        if (attendance.getCheckinTime() == null) {
            throw new RuntimeException("未签到，无法签退");
        }

        attendance.setCheckoutTime(LocalDateTime.now());
        if (StringUtils.hasText(pickPerson)) {
            attendance.setPickPerson(pickPerson);
        }
        if (StringUtils.hasText(pickPhone)) {
            attendance.setPickPhone(pickPhone);
        }
        return attendanceRepository.save(attendance);
    }

    @Transactional
    public Attendance quickCheckin(Integer childId, Integer teacherId, String checkinCode, String remark) {
        // 获取今日已有的考勤记录
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        
        Attendance attendance = attendanceRepository.findByChildId(childId).stream()
                .filter(a -> a.getCheckinTime() != null && a.getCheckinTime().isAfter(startOfDay) && a.getCheckinTime().isBefore(todayEnd(endOfDay)))
                .findFirst().orElse(null);

        if (attendance != null) {
            throw new RuntimeException("该儿童今日已签到");
        }

        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("儿童不存在"));
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("教师不存在"));

        attendance = new Attendance();
        attendance.setChild(child);
        attendance.setTeacher(teacher);
        attendance.setCheckinTime(LocalDateTime.now());
        attendance.setCheckinCode(checkinCode);
        attendance.setCheckoutCode(generateCode());
        attendance.setPickPerson("本人"); // 签到时默认为本人或空
        attendance.setPickPhone(child.getEmergencyPhone());
        attendance.setAttendStatus(BusinessEnums.AttendanceStatus.NORMAL.getCode());
        attendance.setRemark(remark);

        return attendanceRepository.save(attendance);
    }

    private LocalDateTime todayEnd(LocalDateTime end) {
        return end;
    }

    @Transactional
    public Attendance quickCheckout(Integer childId, Integer teacherId, String checkoutCode, String pickPerson, String pickPhone, String remark) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        Attendance attendance = attendanceRepository.findByChildId(childId).stream()
                .filter(a -> a.getCheckinTime() != null && a.getCheckinTime().isAfter(startOfDay) && a.getCheckinTime().isBefore(endOfDay))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("该儿童今日未签到，无法签退"));

        if (attendance.getCheckoutTime() != null) {
            throw new RuntimeException("该儿童今日已签退");
        }

        attendance.setCheckoutTime(LocalDateTime.now());
        attendance.setCheckoutCode(checkoutCode);
        attendance.setPickPerson(pickPerson);
        attendance.setPickPhone(pickPhone);
        if (StringUtils.hasText(remark)) {
            attendance.setRemark(remark);
        }

        return attendanceRepository.save(attendance);
    }

    public List<Child> getTodayAbsentChildren() {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        // 1. 获取所有在册儿童
        List<Child> allChildren = childRepository.findAll();

        // 2. 获取今日已签到儿童ID列表
        List<Integer> checkedInChildIds = attendanceRepository.findByCheckinTimeBetween(startOfDay, endOfDay)
                .stream()
                .map(a -> a.getChild().getChildId())
                .collect(Collectors.toList());

        // 3. 过滤出未签到儿童
        return allChildren.stream()
                .filter(c -> !checkedInChildIds.contains(c.getChildId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Attendance updateAttendanceStatus(Integer id, Integer status, String remark) {
        Attendance attendance = getAttendanceById(id);
        attendance.setAttendStatus(status);
        if (StringUtils.hasText(remark)) {
            attendance.setRemark(remark);
        }
        return attendanceRepository.save(attendance);
    }

    @Transactional
    public void deleteAttendance(Integer id) {
        Attendance attendance = getAttendanceById(id);
        attendanceRepository.delete(attendance);
    }

    public List<Attendance> getTodayAttendances() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return attendanceRepository.findByCheckinTimeBetween(startOfDay, endOfDay);
    }

    private String generateCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}