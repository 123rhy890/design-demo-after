package com.example.caresystem.service;

import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.ClassInfo;
import com.example.caresystem.entity.User;
import com.example.caresystem.repository.ChildRepository;
import com.example.caresystem.repository.ClassInfoRepository;
import com.example.caresystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * 儿童信息核心业务层
 * 所有业务逻辑、参数校验、数据关联都在这里处理
 */
@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassInfoRepository classInfoRepository;

    /**
     * 1. 新增儿童信息 (核心功能：绑定家长+绑定班级)
     * @param child 儿童信息
     * @param parentId 家长ID
     * @param classId 班级ID
     */
    public Child addChild(Child child, Long parentId, Long classId) {
        // 1. 参数非空校验
        if (!StringUtils.hasText(child.getName())) {
            throw new RuntimeException("儿童姓名不能为空");
        }
        if (!StringUtils.hasText(child.getGender())) {
            throw new RuntimeException("儿童性别不能为空");
        }
        if (child.getBirthday() == null) {
            throw new RuntimeException("出生日期不能为空");
        }
        if (!StringUtils.hasText(child.getEmergencyContact())) {
            throw new RuntimeException("紧急联系人不能为空");
        }

        // 2. 绑定家长（关联用户表，只能绑定家长角色的用户）
        User parent = userRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("家长不存在，请先注册家长账号"));
        // 校验该用户是否为家长角色
        if (!parent.getRole().toString().equals("PARENT")) {
            throw new RuntimeException("该用户不是家长账号，无法绑定儿童");
        }
        child.setParent(parent);

        // 3. 绑定班级
        ClassInfo classInfo = classInfoRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("班级不存在"));
        child.setClassInfo(classInfo);

        // 4. 保存儿童信息到数据库
        return childRepository.save(child);
    }

    /**
     * 2. 根据家长ID查询该家长的所有孩子列表 (家长专属查询)
     */
    public List<Child> getChildListByParentId(Long parentId) {
        User parent = userRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("家长不存在"));
        return childRepository.findByParent(parent);
    }

    /**
     * 3. 根据儿童ID查询单个儿童详情
     */
    public Child getChildById(Long id) {
        return childRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("儿童信息不存在"));
    }

    /**
     * 4. 修改儿童信息
     */
    public Child updateChild(Long id, Child child, Long classId) {
        // 1. 查询原儿童信息
        Child oldChild = getChildById(id);
        // 2. 赋值修改后的信息
        oldChild.setName(child.getName());
        oldChild.setGender(child.getGender());
        oldChild.setBirthday(child.getBirthday());
        oldChild.setAllergyInfo(child.getAllergyInfo());
        oldChild.setEmergencyContact(child.getEmergencyContact());
        // 3. 修改班级
        if (classId != null) {
            ClassInfo classInfo = classInfoRepository.findById(classId)
                    .orElseThrow(() -> new RuntimeException("班级不存在"));
            oldChild.setClassInfo(classInfo);
        }
        // 4. 保存修改
        return childRepository.save(oldChild);
    }

    /**
     * 5. 删除儿童信息（逻辑删除，也可物理删除，按需选择）
     */
    public void deleteChild(Long id) {
        Child child = getChildById(id);
        childRepository.delete(child);
    }

    /**
     * 6. 模糊查询儿童信息（按姓名）
     */
    public List<Child> searchChild(String name) {
        return childRepository.findByNameLike("%" + name + "%");
    }
}