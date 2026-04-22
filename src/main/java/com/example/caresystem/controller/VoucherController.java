package com.example.caresystem.controller;

import com.example.caresystem.entity.Voucher;
import com.example.caresystem.service.VoucherService;
import com.example.caresystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voucher")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @PostMapping("/upload")
    public Result<Voucher> uploadVoucher(@RequestBody Voucher voucher,
                                       @RequestParam Integer billId,
                                       @RequestParam Integer parentId) {
        Voucher savedVoucher = voucherService.uploadVoucher(voucher, billId, parentId);
        return Result.success(savedVoucher);
    }

    @GetMapping("/{id}")
    public Result<Voucher> getVoucherById(@PathVariable Integer id) {
        Voucher voucher = voucherService.getVoucherById(id);
        return Result.success(voucher);
    }

    @GetMapping("/list")
    public Result<List<Voucher>> getAllVouchers() {
        List<Voucher> vouchers = voucherService.getAllVouchers();
        return Result.success(vouchers);
    }

    @GetMapping("/bill/{billId}")
    public Result<List<Voucher>> getVouchersByBill(@PathVariable Integer billId) {
        List<Voucher> vouchers = voucherService.getVouchersByBill(billId);
        return Result.success(vouchers);
    }

    @GetMapping("/parent/{parentId}")
    public Result<List<Voucher>> getVouchersByParent(@PathVariable Integer parentId) {
        List<Voucher> vouchers = voucherService.getVouchersByParent(parentId);
        return Result.success(vouchers);
    }

    @GetMapping("/pending")
    public Result<List<Voucher>> getPendingVouchers() {
        List<Voucher> vouchers = voucherService.getPendingVouchers();
        return Result.success(vouchers);
    }

    @GetMapping("/approved")
    public Result<List<Voucher>> getApprovedVouchers() {
        List<Voucher> vouchers = voucherService.getApprovedVouchers();
        return Result.success(vouchers);
    }

    @PutMapping("/audit/{id}")
    public Result<Voucher> auditVoucher(@PathVariable Integer id,
                                       @RequestParam Integer auditorId,
                                       @RequestParam Integer auditStatus,
                                       @RequestParam(required = false) String auditRemark) {
        Voucher voucher = voucherService.auditVoucher(id, auditorId, auditStatus, auditRemark);
        return Result.success(voucher);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteVoucher(@PathVariable Integer id) {
        voucherService.deleteVoucher(id);
        return Result.success();
    }

    @GetMapping("/count/pending")
    public Result<Long> countPendingVouchers() {
        Long count = voucherService.countPendingVouchers();
        return Result.success(count);
    }
}