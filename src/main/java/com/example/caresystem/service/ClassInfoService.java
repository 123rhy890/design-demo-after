package com.example.caresystem.service;

import com.example.caresystem.entity.ClassInfo;
import com.example.caresystem.entity.User;
import com.example.caresystem.enums.UserEnums;
import com.example.caresystem.repository.AttendanceRepository;
import com.example.caresystem.repository.ChildRepository;
import com.example.caresystem.repository.ClassInfoRepository;
import com.example.caresystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassInfoService {

    @Autowired
    private ClassInfoRepository classInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<Map<String, Object>> getAllClassesWithStats() {
        List<ClassInfo> classes = classInfoRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        for (ClassInfo ci : classes) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", ci.getClassId());
            map.put("name", ci.getClassName());
            map.put("teacher", ci.getTeacher().getUsername());
            map.put("teacherId", ci.getTeacher().getUserId());
            map.put("classroom", "一楼" + (100 + ci.getClassId()) + "室"); // 模拟教室位置
            map.put("status", ci.getClassStatus());
            map.put("maxCapacity", ci.getMaxCapacity());
            
            // 统计人数
            map.put("studentCount", childRepository.countByClassInfo(ci));
            
            // 统计今日考勤
            map.put("todayAttendance", attendanceRepository.countAttendanceByClassAndDate(ci.getClassId(), todayStart, todayEnd));
            
            result.add(map);
        }
        return result;
    }

    @Transactional
    public ClassInfo addClass(ClassInfo classInfo, Integer teacherId) {
        if (!StringUtils.hasText(classInfo.getClassName())) {
            throw new RuntimeException("班级名称不能为空");
        }
        if (classInfo.getMaxCapacity() == null || classInfo.getMaxCapacity() <= 0) {
            throw new RuntimeException("班级容量必须大于0");
        }
        if (teacherId == null) {
            throw new RuntimeException("班主任不能为空");
        }

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("教师不存在"));
        if (!UserEnums.Role.TEACHER.getCode().equals(teacher.getRoleType())) {
            throw new RuntimeException("该用户不是教师角色，无法担任班主任");
        }

        classInfo.setTeacher(teacher);
        classInfo.setClassStatus(1);

        return classInfoRepository.save(classInfo);
    }

    public ClassInfo getClassById(Integer id) {
        return classInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("班级不存在"));
    }

    public List<ClassInfo> getAllClasses() {
        return classInfoRepository.findAll();
    }

    public List<ClassInfo> getNormalClasses() {
        return classInfoRepository.findNormalClasses();
    }

    public List<ClassInfo> getClassesByTeacher(Integer teacherId) {
        return classInfoRepository.findByTeacherId(teacherId);
    }

    public List<ClassInfo> getClassesByStatus(Integer classStatus) {
        return classInfoRepository.findByClassStatus(classStatus);
    }

    @Transactional
    public ClassInfo updateClass(Integer id, ClassInfo classInfo, Integer teacherId) {
        ClassInfo oldClass = getClassById(id);

        if (StringUtils.hasText(classInfo.getClassName())) {
            oldClass.setClassName(classInfo.getClassName());
        }
        if (classInfo.getMaxCapacity() != null && classInfo.getMaxCapacity() > 0) {
            oldClass.setMaxCapacity(classInfo.getMaxCapacity());
        }
        if (teacherId != null) {
            User teacher = userRepository.findById(teacherId)
                    .orElseThrow(() -> new RuntimeException("教师不存在"));
            if (!UserEnums.Role.TEACHER.getCode().equals(teacher.getRoleType())) {
                throw new RuntimeException("该用户不是教师角色");
            }
            oldClass.setTeacher(teacher);
        }

        return classInfoRepository.save(oldClass);
    }

    @Transactional
    public void deleteClass(Integer id) {
        ClassInfo classInfo = getClassById(id);
        classInfoRepository.delete(classInfo);
    }

    @Transactional
    public ClassInfo updateStatus(Integer id, Integer status) {
        ClassInfo classInfo = getClassById(id);
        classInfo.setClassStatus(status);
        return classInfoRepository.save(classInfo);
    }

    public Long countByTeacher(Integer teacherId) {
        return classInfoRepository.countByTeacherId(teacherId);
    }

    public Long countByStatus(Integer classStatus) {
        return classInfoRepository.countByClassStatus(classStatus);
    }

    public List<ClassInfo> searchClasses(String keyword) {
        return classInfoRepository.findByClassNameContaining(keyword);
    }
}