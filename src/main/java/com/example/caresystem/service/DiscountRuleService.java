package com.example.caresystem.service;

import com.example.caresystem.entity.DiscountRule;
import com.example.caresystem.entity.User;
import com.example.caresystem.enums.FeeEnums;
import com.example.caresystem.repository.DiscountRuleRepository;
import com.example.caresystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiscountRuleService {

    @Autowired
    private DiscountRuleRepository discountRuleRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public DiscountRule addDiscountRule(DiscountRule discountRule, Integer createById) {
        if (!StringUtils.hasText(discountRule.getDiscountName())) {
            throw new RuntimeException("优惠名称不能为空");
        }
        if (discountRule.getDiscountType() == null) {
            throw new RuntimeException("优惠类型不能为空");
        }
        if (discountRule.getDiscountValue() == null || discountRule.getDiscountValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("优惠值必须大于0");
        }
        if (discountRule.getEffectiveTime() == null) {
            throw new RuntimeException("生效时间不能为空");
        }
        if (discountRule.getInvalidTime() == null) {
            throw new RuntimeException("失效时间不能为空");
        }
        if (discountRule.getEffectiveTime().isAfter(discountRule.getInvalidTime())) {
            throw new RuntimeException("生效时间不能晚于失效时间");
        }
        if (createById == null) {
            throw new RuntimeException("创建人ID不能为空");
        }

        User createBy = userRepository.findById(createById)
                .orElseThrow(() -> new RuntimeException("创建人不存在"));
        discountRule.setCreateBy(createBy);

        discountRule.setStatus(FeeEnums.FeeStatus.NORMAL.getCode());

        return discountRuleRepository.save(discountRule);
    }

    public DiscountRule getDiscountRuleById(Integer id) {
        return discountRuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("优惠规则不存在"));
    }

    public List<DiscountRule> getAllDiscountRules() {
        return discountRuleRepository.findAll();
    }

    public List<DiscountRule> getDiscountRulesByType(Integer discountType) {
        return discountRuleRepository.findByDiscountType(discountType);
    }

    public List<DiscountRule> getDiscountRulesByStatus(Integer status) {
        return discountRuleRepository.findByStatus(status);
    }

    public List<DiscountRule> getActiveDiscountRules() {
        return discountRuleRepository.findActiveRules(LocalDateTime.now());
    }

    public List<DiscountRule> getActiveDiscountRulesByType(Integer discountType) {
        return discountRuleRepository.findActiveRulesByType(discountType, LocalDateTime.now());
    }

    @Transactional
    public DiscountRule updateDiscountRule(Integer id, DiscountRule discountRule) {
        DiscountRule oldRule = getDiscountRuleById(id);

        if (StringUtils.hasText(discountRule.getDiscountName())) {
            oldRule.setDiscountName(discountRule.getDiscountName());
        }
        if (discountRule.getDiscountType() != null) {
            oldRule.setDiscountType(discountRule.getDiscountType());
        }
        if (discountRule.getDiscountValue() != null && discountRule.getDiscountValue().compareTo(BigDecimal.ZERO) > 0) {
            oldRule.setDiscountValue(discountRule.getDiscountValue());
        }
        if (discountRule.getEffectiveTime() != null) {
            oldRule.setEffectiveTime(discountRule.getEffectiveTime());
        }
        if (discountRule.getInvalidTime() != null) {
            oldRule.setInvalidTime(discountRule.getInvalidTime());
        }

        return discountRuleRepository.save(oldRule);
    }

    @Transactional
    public DiscountRule updateStatus(Integer id, Integer status) {
        DiscountRule discountRule = getDiscountRuleById(id);
        discountRule.setStatus(status);
        return discountRuleRepository.save(discountRule);
    }

    @Transactional
    public void deleteDiscountRule(Integer id) {
        DiscountRule discountRule = getDiscountRuleById(id);
        discountRuleRepository.delete(discountRule);
    }

    public List<DiscountRule> getUpcomingDiscountRules() {
        return discountRuleRepository.findUpcomingRules(LocalDateTime.now());
    }

    public List<DiscountRule> getExpiredDiscountRules() {
        return discountRuleRepository.findExpiredRules(LocalDateTime.now());
    }
}