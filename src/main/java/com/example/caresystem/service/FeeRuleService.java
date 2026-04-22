package com.example.caresystem.service;

import com.example.caresystem.entity.FeeRule;
import com.example.caresystem.entity.User;
import com.example.caresystem.enums.FeeEnums;
import com.example.caresystem.repository.FeeRuleRepository;
import com.example.caresystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeeRuleService {

    @Autowired
    private FeeRuleRepository feeRuleRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public FeeRule addFeeRule(FeeRule feeRule, Integer createById) {
        if (!StringUtils.hasText(feeRule.getTimeSlot())) {
            throw new RuntimeException("时间段不能为空");
        }
        if (feeRule.getUnitPrice() == null || feeRule.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("单价必须大于0");
        }
        if (feeRule.getEffectiveTime() == null) {
            throw new RuntimeException("生效时间不能为空");
        }
        if (feeRule.getInvalidTime() == null) {
            throw new RuntimeException("失效时间不能为空");
        }
        if (feeRule.getEffectiveTime().isAfter(feeRule.getInvalidTime())) {
            throw new RuntimeException("生效时间不能晚于失效时间");
        }
        if (createById == null) {
            throw new RuntimeException("创建人ID不能为空");
        }

        User createBy = userRepository.findById(createById)
                .orElseThrow(() -> new RuntimeException("创建人不存在"));
        feeRule.setCreateBy(createBy);

        feeRule.setStatus(FeeEnums.FeeStatus.NORMAL.getCode());

        return feeRuleRepository.save(feeRule);
    }

    public FeeRule getFeeRuleById(Integer id) {
        return feeRuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("费用规则不存在"));
    }

    public List<FeeRule> getAllFeeRules() {
        return feeRuleRepository.findAll();
    }

    public List<FeeRule> getFeeRulesByTimeSlot(String timeSlot) {
        return feeRuleRepository.findByTimeSlot(timeSlot);
    }

    public List<FeeRule> getFeeRulesByStatus(Integer status) {
        return feeRuleRepository.findByStatus(status);
    }

    public FeeRule getActiveRuleByTimeSlot(String timeSlot) {
        return feeRuleRepository.findActiveRuleByTimeSlot(timeSlot, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("该时间段没有有效的费用规则"));
    }

    public List<FeeRule> getActiveRules() {
        return feeRuleRepository.findActiveRules(LocalDateTime.now());
    }

    @Transactional
    public FeeRule updateFeeRule(Integer id, FeeRule feeRule) {
        FeeRule oldRule = getFeeRuleById(id);

        if (StringUtils.hasText(feeRule.getTimeSlot())) {
            oldRule.setTimeSlot(feeRule.getTimeSlot());
        }
        if (feeRule.getUnitPrice() != null && feeRule.getUnitPrice().compareTo(BigDecimal.ZERO) > 0) {
            oldRule.setUnitPrice(feeRule.getUnitPrice());
        }
        if (feeRule.getEffectiveTime() != null) {
            oldRule.setEffectiveTime(feeRule.getEffectiveTime());
        }
        if (feeRule.getInvalidTime() != null) {
            oldRule.setInvalidTime(feeRule.getInvalidTime());
        }

        return feeRuleRepository.save(oldRule);
    }

    @Transactional
    public FeeRule updateStatus(Integer id, Integer status) {
        FeeRule feeRule = getFeeRuleById(id);
        feeRule.setStatus(status);
        return feeRuleRepository.save(feeRule);
    }

    @Transactional
    public void deleteFeeRule(Integer id) {
        FeeRule feeRule = getFeeRuleById(id);
        feeRuleRepository.delete(feeRule);
    }

    public List<FeeRule> getUpcomingRules() {
        return feeRuleRepository.findUpcomingRules(LocalDateTime.now());
    }

    public List<FeeRule> getExpiredRules() {
        return feeRuleRepository.findExpiredRules(LocalDateTime.now());
    }
}