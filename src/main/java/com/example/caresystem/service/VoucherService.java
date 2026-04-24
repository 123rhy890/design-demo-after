package com.example.caresystem.service;

import com.example.caresystem.entity.FeeBill;
import com.example.caresystem.entity.User;
import com.example.caresystem.entity.Voucher;
import com.example.caresystem.enums.UserEnums;
import com.example.caresystem.repository.FeeBillRepository;
import com.example.caresystem.repository.UserRepository;
import com.example.caresystem.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private FeeBillRepository feeBillRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Voucher uploadVoucher(Voucher voucher, Integer billId, Integer parentId) {
        if (billId == null) {
            throw new RuntimeException("账单ID不能为空");
        }
        if (parentId == null) {
            throw new RuntimeException("家长ID不能为空");
        }
        if (!StringUtils.hasText(voucher.getVoucherUrl())) {
            throw new RuntimeException("凭证URL不能为空");
        }

        FeeBill feeBill = feeBillRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("账单不存在"));
        voucher.setFeeBill(feeBill);

        User parent = userRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("家长不存在"));
        if (!UserEnums.Role.PARENT.getCode().equals(parent.getRoleType())) {
            throw new RuntimeException("该用户不是家长角色");
        }
        voucher.setParent(parent);

        voucher.setVoucherType("image");
        voucher.setUploadTime(LocalDateTime.now());
        voucher.setAuditStatus(0);

        // 更新账单状态为“审核中”
        feeBill.setPaymentStatus(4); // 4-审核中
        feeBill.setUpdateTime(LocalDateTime.now());
        feeBillRepository.save(feeBill);

        return voucherRepository.save(voucher);
    }

    public Voucher getVoucherById(Integer id) {
        return voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("凭证不存在"));
    }

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public List<Voucher> getVouchersByBill(Integer billId) {
        return voucherRepository.findByBillId(billId);
    }

    public List<Voucher> getVouchersByParent(Integer parentId) {
        return voucherRepository.findByParentId(parentId);
    }

    public List<Voucher> getPendingVouchers() {
        return voucherRepository.findPendingVouchers();
    }

    public List<Voucher> getApprovedVouchers() {
        return voucherRepository.findApprovedVouchers();
    }

    @Transactional
    public Voucher auditVoucher(Integer id, Integer auditorId, Integer auditStatus, String auditRemark) {
        Voucher voucher = getVoucherById(id);

        User auditor = userRepository.findById(auditorId)
                .orElseThrow(() -> new RuntimeException("审核人不存在"));
        voucher.setAuditor(auditor);
        voucher.setAuditStatus(auditStatus);
        voucher.setAuditTime(LocalDateTime.now());

        if (StringUtils.hasText(auditRemark)) {
            voucher.setAuditRemark(auditRemark);
        }

        Voucher savedVoucher = voucherRepository.save(voucher);

        // 如果审核通过，同步更新账单状态
        if (auditStatus == 1) {
            FeeBill feeBill = voucher.getFeeBill();
            feeBill.setPaymentStatus(1); // 已缴费
            feeBill.setActualAmount(feeBill.getPayableAmount()); // 实付金额等于应付金额
            feeBill.setUpdateTime(LocalDateTime.now());
            feeBillRepository.save(feeBill);
        } else if (auditStatus == 2) {
            // 如果审核驳回，恢复账单状态为未缴费
            FeeBill feeBill = voucher.getFeeBill();
            feeBill.setPaymentStatus(0); // 未缴费
            feeBill.setUpdateTime(LocalDateTime.now());
            feeBillRepository.save(feeBill);
        }

        return savedVoucher;
    }

    @Transactional
    public void deleteVoucher(Integer id) {
        Voucher voucher = getVoucherById(id);
        voucherRepository.delete(voucher);
    }

    public Long countPendingVouchers() {
        return voucherRepository.countByAuditStatus(0);
    }
}