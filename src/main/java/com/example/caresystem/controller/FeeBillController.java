package com.example.caresystem.controller;

import com.example.caresystem.entity.FeeBill;
import com.example.caresystem.service.FeeBillService;
import com.example.caresystem.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feeBill")
public class FeeBillController {

    private static final Logger log = LoggerFactory.getLogger(FeeBillController.class);

    @Autowired
    private FeeBillService feeBillService;

    @PostMapping("/add")
    public Result<FeeBill> addFeeBill(@RequestBody FeeBill feeBill,
                                    @RequestParam Integer childId,
                                    @RequestParam Integer parentId,
                                    @RequestParam Integer createById,
                                    @RequestParam(required = false) Integer discountId) {
        FeeBill savedBill = feeBillService.addFeeBill(feeBill, childId, parentId, createById, discountId);
        return Result.success(savedBill);
    }

    @GetMapping("/{id:\\d+}")
    public Result<FeeBill> getFeeBillById(@PathVariable Integer id) {
        FeeBill feeBill = feeBillService.getFeeBillById(id);
        return Result.success(feeBill);
    }

    @GetMapping("/list")
    public Result<List<FeeBill>> getAllFeeBills() {
        List<FeeBill> bills = feeBillService.getAllFeeBills();
        return Result.success(bills);
    }

    @GetMapping("/child/{childId}")
    public Result<List<FeeBill>> getFeeBillsByChild(@PathVariable Integer childId) {
        List<FeeBill> bills = feeBillService.getFeeBillsByChild(childId);
        return Result.success(bills);
    }

    @GetMapping("/parent/{parentId}")
    public Result<List<FeeBill>> getFeeBillsByParent(@PathVariable Integer parentId) {
        log.info("查询家长账单，parentId: {}", parentId);
        List<FeeBill> bills = feeBillService.getFeeBillsByParent(parentId);
        log.info("查询完成，账单数量: {}", bills.size());
        return Result.success(bills);
    }

    @GetMapping("/month/{billMonth}")
    public Result<List<FeeBill>> getFeeBillsByMonth(@PathVariable String billMonth) {
        List<FeeBill> bills = feeBillService.getFeeBillsByMonth(billMonth);
        return Result.success(bills);
    }

    @GetMapping("/status/{paymentStatus}")
    public Result<List<FeeBill>> getFeeBillsByStatus(@PathVariable Integer paymentStatus) {
        List<FeeBill> bills = feeBillService.getFeeBillsByStatus(paymentStatus);
        return Result.success(bills);
    }

    @GetMapping("/child/{childId}/month/{billMonth}")
    public Result<FeeBill> getFeeBillByChildAndMonth(@PathVariable Integer childId,
                                                     @PathVariable String billMonth) {
        FeeBill bill = feeBillService.getFeeBillByChildAndMonth(childId, billMonth);
        return Result.success(bill);
    }

    @GetMapping("/unpaid")
    public Result<List<FeeBill>> getUnpaidBills() {
        List<FeeBill> bills = feeBillService.getUnpaidBills();
        return Result.success(bills);
    }

    @GetMapping("/overdue")
    public Result<List<FeeBill>> getOverdueBills() {
        List<FeeBill> bills = feeBillService.getOverdueBills();
        return Result.success(bills);
    }

    @PutMapping("/update/{id}")
    public Result<FeeBill> updateFeeBill(@PathVariable Integer id, @RequestBody FeeBill feeBill) {
        FeeBill updatedBill = feeBillService.updateFeeBill(id, feeBill);
        return Result.success(updatedBill);
    }

    @GetMapping("/parent/{parentId}/stats")
    public Result<Map<String, Object>> getParentFeeStats(@PathVariable Integer parentId) {
        Map<String, Object> stats = feeBillService.getParentFeeStats(parentId);
        return Result.success(stats);
    }

    @PutMapping("/pay/{id}")
    public Result<FeeBill> payBill(@PathVariable Integer id, @RequestParam BigDecimal actualAmount) {
        FeeBill bill = feeBillService.payBill(id, actualAmount);
        return Result.success(bill);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteFeeBill(@PathVariable Integer id) {
        feeBillService.deleteFeeBill(id);
        return Result.success();
    }

    @GetMapping("/parent/{parentId}/status/{paymentStatus}")
    public Result<List<FeeBill>> getFeeBillsByParentAndStatus(@PathVariable Integer parentId,
                                                              @PathVariable Integer paymentStatus) {
        List<FeeBill> bills = feeBillService.getFeeBillsByParentAndStatus(parentId, paymentStatus);
        return Result.success(bills);
    }

    /**
     * 分页条件查询账单（管理员端）
     */
    @GetMapping("/page")
    public Result<Map<String, Object>> getFeeBillsByPage(@RequestParam(required = false) String childName,
                                                      @RequestParam(required = false) Integer paymentStatus,
                                                      @RequestParam(required = false) String billMonth,
                                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                                      @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("查询账单分页，childName: {}, status: {}, month: {}, pageNum: {}, pageSize: {}", 
                childName, paymentStatus, billMonth, pageNum, pageSize);
        Page<FeeBill> page = feeBillService.getFeeBillsByConditions(childName, paymentStatus, billMonth, pageNum, pageSize);
        
        Map<String, Object> result = new HashMap<>();
        result.put("content", page.getContent());
        result.put("totalElements", page.getTotalElements());
        result.put("totalPages", page.getTotalPages());
        
        log.info("查询完成，总记录数: {}", page.getTotalElements());
        return Result.success(result);
    }

    /**
     * 获取指定月份费用统计
     */
    @GetMapping("/stats/{billMonth}")
    public Result<Map<String, Object>> getFeeStatsByMonth(@PathVariable String billMonth) {
        Map<String, Object> stats = feeBillService.getFeeStatsByMonth(billMonth);
        return Result.success(stats);
    }

    /**
     * 获取近N个月费用趋势
     */
    @GetMapping("/trend")
    public Result<Map<String, Object>> getFeeTrend(@RequestParam(defaultValue = "6") int months) {
        Map<String, Object> trend = feeBillService.getFeeTrend(months);
        return Result.success(trend);
    }

    /**
     * 发送欠费提醒
     */
    @PostMapping("/remind/{id}")
    public Result<Void> sendRemind(@PathVariable Integer id) {
        feeBillService.sendRemind(id);
        return Result.success();
    }
}