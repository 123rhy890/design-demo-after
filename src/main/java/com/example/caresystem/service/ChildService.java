package com.example.caresystem.service;

import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.ClassInfo;
import com.example.caresystem.entity.User;
import com.example.caresystem.enums.UserEnums;
import com.example.caresystem.repository.ChildRepository;
import com.example.caresystem.repository.ClassInfoRepository;
import com.example.caresystem.repository.UserRepository;
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