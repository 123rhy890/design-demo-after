package com.example.caresystem.controller;

import com.example.caresystem.entity.FeeBill;
import com.example.caresystem.service.FeeBillService;
import com.example.caresystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/feeBill")
public class FeeBillController {

    @Autowired
    private FeeBillService feeBillService;

    @PostMapping("/add")
    public Result<FeeBill> addFeeBill(@RequestBody FeeBill feeBill,
                                    @RequestParam Integer childId,
                                    @RequestParam Integer parentId,
                                    @RequestParam Integer createById) {
        FeeBill savedBill = feeBillService.addFeeBill(feeBill, childId, parentId, createById);
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
        List<FeeBill> bills = feeBillService.getFeeBillsByParent(parentId);
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
}