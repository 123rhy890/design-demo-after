package com.example.caresystem.service;

import com.example.caresystem.entity.Allergy;
import com.example.caresystem.entity.Child;
import com.example.caresystem.repository.AllergyRepository;
import com.example.caresystem.repository.ChildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AllergyService {

    @Autowired
    private AllergyRepository allergyRepository;

    @Autowired
    private ChildRepository childRepository;

    @Transactional
    public Allergy addAllergy(Allergy allergy, Integer childId) {
        if (childId == null) {
            throw new RuntimeException("儿童ID不能为空");
        }
        if (!StringUtils.hasText(allergy.getAllergyType())) {
            throw new RuntimeException("过敏类型不能为空");
        }
        if (!StringUtils.hasText(allergy.getAllergyContent())) {
            throw new RuntimeException("过敏内容不能为空");
        }

        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("儿童不存在"));
        allergy.setChild(child);

        allergy.setStatus(1);

        return allergyRepository.save(allergy);
    }

    public Allergy getAllergyById(Integer id) {
        return allergyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("过敏记录不存在"));
    }

    public List<Allergy> getAllAllergies() {
        return allergyRepository.findAll();
    }

    public List<Allergy> getAllergiesByChild(Integer childId) {
        return allergyRepository.findByChildId(childId);
    }

    public List<Allergy> getAllergiesByType(String allergyType) {
        return allergyRepository.findByAllergyType(allergyType);
    }

    public List<Allergy> getActiveAllergies() {
        return allergyRepository.findActiveAllergies();
    }

    public List<Allergy> getActiveAllergiesByChild(Integer childId) {
        return allergyRepository.findByChildIdAndStatus(childId, 1);
    }

    @Transactional
    public Allergy updateAllergy(Integer id, Allergy allergy) {
        Allergy oldAllergy = getAllergyById(id);

        if (StringUtils.hasText(allergy.getAllergyType())) {
            oldAllergy.setAllergyType(allergy.getAllergyType());
        }
        if (StringUtils.hasText(allergy.getAllergyContent())) {
            oldAllergy.setAllergyContent(allergy.getAllergyContent());
        }
        if (allergy.getAllergySymptom() != null) {
            oldAllergy.setAllergySymptom(allergy.getAllergySymptom());
        }
        if (allergy.getHandleMethod() != null) {
            oldAllergy.setHandleMethod(allergy.getHandleMethod());
        }

        return allergyRepository.save(oldAllergy);
    }

    @Transactional
    public Allergy updateStatus(Integer id, Integer status) {
        Allergy allergy = getAllergyById(id);
        allergy.setStatus(status);
        return allergyRepository.save(allergy);
    }

    @Transactional
    public void deleteAllergy(Integer id) {
        Allergy allergy = getAllergyById(id);
        allergyRepository.delete(allergy);
    }

    public Long countByChild(Integer childId) {
        return allergyRepository.countByChildId(childId);
    }
}