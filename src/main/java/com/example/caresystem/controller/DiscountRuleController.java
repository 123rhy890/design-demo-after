package com.example.caresystem.controller;

import com.example.caresystem.entity.DiscountRule;
import com.example.caresystem.service.DiscountRuleService;
import com.example.caresystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discountRule")
public class DiscountRuleController {

    @Autowired
    private DiscountRuleService discountRuleService;

    @PostMapping("/add")
    public Result<DiscountRule> addDiscountRule(@RequestBody DiscountRule discountRule,
                                               @RequestParam Integer createById) {
        DiscountRule savedRule = discountRuleService.addDiscountRule(discountRule, createById);
        return Result.success(savedRule);
    }

    @GetMapping("/{id}")
    public Result<DiscountRule> getDiscountRuleById(@PathVariable Integer id) {
        DiscountRule discountRule = discountRuleService.getDiscountRuleById(id);
        return Result.success(discountRule);
    }

    @GetMapping("/list")
    public Result<List<DiscountRule>> getAllDiscountRules() {
        List<DiscountRule> rules = discountRuleService.getAllDiscountRules();
        return Result.success(rules);
    }

    @GetMapping("/type/{discountType}")
    public Result<List<DiscountRule>> getDiscountRulesByType(@PathVariable Integer discountType) {
        List<DiscountRule> rules = discountRuleService.getDiscountRulesByType(discountType);
        return Result.success(rules);
    }

    @GetMapping("/status/{status}")
    public Result<List<DiscountRule>> getDiscountRulesByStatus(@PathVariable Integer status) {
        List<DiscountRule> rules = discountRuleService.getDiscountRulesByStatus(status);
        return Result.success(rules);
    }

    @GetMapping("/active")
    public Result<List<DiscountRule>> getActiveDiscountRules() {
        List<DiscountRule> rules = discountRuleService.getActiveDiscountRules();
        return Result.success(rules);
    }

    @GetMapping("/active/type/{discountType}")
    public Result<List<DiscountRule>> getActiveDiscountRulesByType(@PathVariable Integer discountType) {
        List<DiscountRule> rules = discountRuleService.getActiveDiscountRulesByType(discountType);
        return Result.success(rules);
    }

    @PutMapping("/update/{id}")
    public Result<DiscountRule> updateDiscountRule(@PathVariable Integer id,
                                                  @RequestBody DiscountRule discountRule) {
        DiscountRule updatedRule = discountRuleService.updateDiscountRule(id, discountRule);
        return Result.success(updatedRule);
    }

    @PutMapping("/status/{id}")
    public Result<DiscountRule> updateStatus(@PathVariable Integer id, @RequestParam Integer status) {
        DiscountRule rule = discountRuleService.updateStatus(id, status);
        return Result.success(rule);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteDiscountRule(@PathVariable Integer id) {
        discountRuleService.deleteDiscountRule(id);
        return Result.success();
    }

    @GetMapping("/upcoming")
    public Result<List<DiscountRule>> getUpcomingDiscountRules() {
        List<DiscountRule> rules = discountRuleService.getUpcomingDiscountRules();
        return Result.success(rules);
    }

    @GetMapping("/expired")
    public Result<List<DiscountRule>> getExpiredDiscountRules() {
        List<DiscountRule> rules = discountRuleService.getExpiredDiscountRules();
        return Result.success(rules);
    }
}