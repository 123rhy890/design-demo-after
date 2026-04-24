package com.example.caresystem.service;

import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.ClassInfo;
import com.example.caresystem.entity.FeeBill;
import com.example.caresystem.entity.User;
import com.example.caresystem.enums.UserEnums;
import com.example.caresystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassInfoRepository classInfoRepository;

    @Autowired
    private AllergyRepository allergyRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private CommunicationRepository communicationRepository;

    @Autowired
    private DailyStatusRepository dailyStatusRepository;

    @Autowired
    private FeeBillRepository feeBillRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Transactional
    public Child addChild(Child child, Integer parentId, Integer classId) {
        if (!StringUtils.hasText(child.getChildName())) {
            throw new RuntimeException("儿童姓名不能为空");
        }
        if (child.getGender() == null) {
            throw new RuntimeException("儿童性别不能为空");
        }
        if (child.getBirthDate() == null) {
            throw new RuntimeException("出生日期不能为空");
        }
        if (!StringUtils.hasText(child.getEmergencyContact())) {
            throw new RuntimeException("紧急联系人不能为空");
        }
        if (!StringUtils.hasText(child.getEmergencyPhone())) {
            throw new RuntimeException("紧急联系电话不能为空");
        }

        User parent = userRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("家长不存在"));
        if (!UserEnums.Role.PARENT.getCode().equals(parent.getRoleType())) {
            throw new RuntimeException("该用户不是家长账号，无法绑定儿童");
        }
        child.setParent(parent);
        
        // 设置默认状态为已通过 (如果是申请流程则设为待审核)
        if (child.getStatus() == null) {
            child.setStatus(1); 
        }
        if (child.getCreateTime() == null) {
            child.setCreateTime(java.time.LocalDateTime.now());
        }

        if (classId != null) {
            ClassInfo classInfo = classInfoRepository.findById(classId)
                    .orElseThrow(() -> new RuntimeException("班级不存在"));
            child.setClassInfo(classInfo);
        }

        return childRepository.save(child);
    }

    public List<Child> getChildListByParentId(Integer parentId) {
        return childRepository.findByParentId(parentId);
    }

    public Child getChildById(Integer id) {
        return childRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("儿童信息不存在"));
    }

    @Transactional
    public Child updateChild(Integer id, Child child, Integer classId) {
        Child oldChild = getChildById(id);

        if (StringUtils.hasText(child.getChildName())) {
            oldChild.setChildName(child.getChildName());
        }
        if (child.getGender() != null) {
            oldChild.setGender(child.getGender());
        }
        if (child.getBirthDate() != null) {
            oldChild.setBirthDate(child.getBirthDate());
        }
        if (child.getIdCard() != null) {
            oldChild.setIdCard(child.getIdCard());
        }
        if (child.getAllergyHistory() != null) {
            oldChild.setAllergyHistory(child.getAllergyHistory());
        }
        if (StringUtils.hasText(child.getEmergencyContact())) {
            oldChild.setEmergencyContact(child.getEmergencyContact());
        }
        if (StringUtils.hasText(child.getEmergencyPhone())) {
            oldChild.setEmergencyPhone(child.getEmergencyPhone());
        }
        if (StringUtils.hasText(child.getRemark())) {
            oldChild.setRemark(child.getRemark());
        }

        if (classId != null) {
            ClassInfo classInfo = classInfoRepository.findById(classId)
                    .orElseThrow(() -> new RuntimeException("班级不存在"));
            oldChild.setClassInfo(classInfo);
        }

        return childRepository.save(oldChild);
    }

    @Transactional
    public void deleteChild(Integer id) {
        Child child = getChildById(id);
        
        // 1. 删除过敏记录
        allergyRepository.deleteInBatch(allergyRepository.findByChild(child));
        
        // 2. 删除考勤记录 (考勤记录引用了预约，所以先删考勤)
        attendanceRepository.deleteInBatch(attendanceRepository.findByChild(child));
        
        // 3. 删除沟通记录
        communicationRepository.deleteInBatch(communicationRepository.findByChild(child));
        
        // 4. 删除日常状态
        dailyStatusRepository.deleteInBatch(dailyStatusRepository.findByChild(child));
        
        // 5. 删除预约记录
        reservationRepository.deleteInBatch(reservationRepository.findByChild(child));
        
        // 6. 删除账单及关联凭证
        List<FeeBill> bills = feeBillRepository.findByChild(child);
        for (FeeBill bill : bills) {
            voucherRepository.deleteInBatch(voucherRepository.findByFeeBill(bill));
        }
        feeBillRepository.deleteInBatch(bills);
        
        // 7. 最后删除儿童信息
        childRepository.delete(child);
    }

    public List<Child> searchChild(String keyword) {
        return childRepository.searchByKeyword(keyword);
    }

    public List<Child> getChildrenByClassId(Integer classId) {
        return childRepository.findByClassId(classId);
    }

    public List<Child> getAllChildren() {
        return childRepository.findAll();
    }

    @Transactional
    public Child updateClassInfo(Integer childId, Integer classId) {
        Child child = getChildById(childId);
        if (classId == null || classId <= 0) {
            child.setClassInfo(null);
        } else {
            ClassInfo classInfo = classInfoRepository.findById(classId)
                    .orElseThrow(() -> new RuntimeException("班级不存在"));
            child.setClassInfo(classInfo);
        }
        return childRepository.save(child);
    }

    public Long countByClassId(Integer classId) {
        if (classId == null || classId <= 0) return 0L;
        ClassInfo ci = classInfoRepository.findById(classId).orElse(null);
        return ci != null ? childRepository.countByClassInfo(ci) : 0L;
    }
}