package com.example.caresystem.controller;

import com.example.caresystem.entity.FeeRule;
import com.example.caresystem.service.FeeRuleService;
import com.example.caresystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feeRule")
public class FeeRuleController {

    @Autowired
    private FeeRuleService feeRuleService;

    @PostMapping("/add")
    public Result<FeeRule> addFeeRule(@RequestBody FeeRule feeRule, @RequestParam Integer createById) {
        FeeRule savedRule = feeRuleService.addFeeRule(feeRule, createById);
        return Result.success(savedRule);
    }

    @GetMapping("/{id}")
    public Result<FeeRule> getFeeRuleById(@PathVariable Integer id) {
        FeeRule feeRule = feeRuleService.getFeeRuleById(id);
        return Result.success(feeRule);
    }

    @GetMapping("/list")
    public Result<List<FeeRule>> getAllFeeRules() {
        List<FeeRule> rules = feeRuleService.getAllFeeRules();
        return Result.success(rules);
    }

    @GetMapping("/timeSlot/{timeSlot}")
    public Result<List<FeeRule>> getFeeRulesByTimeSlot(@PathVariable String timeSlot) {
        List<FeeRule> rules = feeRuleService.getFeeRulesByTimeSlot(timeSlot);
        return Result.success(rules);
    }

    @GetMapping("/status/{status}")
    public Result<List<FeeRule>> getFeeRulesByStatus(@PathVariable Integer status) {
        List<FeeRule> rules = feeRuleService.getFeeRulesByStatus(status);
        return Result.success(rules);
    }

    @GetMapping("/active/timeSlot/{timeSlot}")
    public Result<FeeRule> getActiveRuleByTimeSlot(@PathVariable String timeSlot) {
        FeeRule rule = feeRuleService.getActiveRuleByTimeSlot(timeSlot);
        return Result.success(rule);
    }

    @GetMapping("/active")
    public Result<List<FeeRule>> getActiveRules() {
        List<FeeRule> rules = feeRuleService.getActiveRules();
        return Result.success(rules);
    }

    @PutMapping("/update/{id}")
    public Result<FeeRule> updateFeeRule(@PathVariable Integer id, @RequestBody FeeRule feeRule) {
        FeeRule updatedRule = feeRuleService.updateFeeRule(id, feeRule);
        return Result.success(updatedRule);
    }

    @PutMapping("/status/{id}")
    public Result<FeeRule> updateStatus(@PathVariable Integer id, @RequestParam Integer status) {
        FeeRule rule = feeRuleService.updateStatus(id, status);
        return Result.success(rule);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteFeeRule(@PathVariable Integer id) {
        feeRuleService.deleteFeeRule(id);
        return Result.success();
    }

    @GetMapping("/upcoming")
    public Result<List<FeeRule>> getUpcomingRules() {
        List<FeeRule> rules = feeRuleService.getUpcomingRules();
        return Result.success(rules);
    }

    @GetMapping("/expired")
    public Result<List<FeeRule>> getExpiredRules() {
        List<FeeRule> rules = feeRuleService.getExpiredRules();
        return Result.success(rules);
    }
}