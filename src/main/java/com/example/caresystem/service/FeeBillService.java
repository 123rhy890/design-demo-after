package com.example.caresystem.service;

import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.FeeBill;
import com.example.caresystem.entity.User;
import com.example.caresystem.enums.FeeEnums;
import com.example.caresystem.enums.UserEnums;
import com.example.caresystem.repository.ChildRepository;
import com.example.caresystem.repository.FeeBillRepository;
import com.example.caresystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeeBillService {

    @Autowired
    private FeeBillRepository feeBillRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public FeeBill addFeeBill(FeeBill feeBill, Integer childId, Integer parentId, Integer createById) {
        if (childId == null) {
            throw new RuntimeException("儿童ID不能为空");
        }
        if (parentId == null) {
            throw new RuntimeException("家长ID不能为空");
        }
        if (createById == null) {
            throw new RuntimeException("创建人ID不能为空");
        }
        if (!StringUtils.hasText(feeBill.getBillMonth())) {
            throw new RuntimeException("账单月份不能为空");
        }
        if (feeBill.getManageDays() == null || feeBill.getManageDays() <= 0) {
            throw new RuntimeException("托管天数必须大于0");
        }
        if (feeBill.getUnitPrice() == null) {
            throw new RuntimeException("单价不能为空");
        }

        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("儿童不存在"));
        feeBill.setChild(child);

        User parent = userRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("家长不存在"));
        if (!UserEnums.Role.PARENT.getCode().equals(parent.getRoleType())) {
            throw new RuntimeException("该用户不是家长角色");
        }
        feeBill.setParent(parent);

        User createBy = userRepository.findById(createById)
                .orElseThrow(() -> new RuntimeException("创建人不存在"));
        feeBill.setCreateBy(createBy);

        BigDecimal payableAmount = feeBill.getUnitPrice().multiply(new BigDecimal(feeBill.getManageDays()));
        if (feeBill.getDiscountAmount() == null) {
            feeBill.setDiscountAmount(BigDecimal.ZERO);
        }
        payableAmount = payableAmount.subtract(feeBill.getDiscountAmount());
        feeBill.setPayableAmount(payableAmount);
        feeBill.setActualAmount(BigDecimal.ZERO);
        feeBill.setPaymentStatus(FeeEnums.PaymentStatus.UNPAID.getCode());

        return feeBillRepository.save(feeBill);
    }

    public FeeBill getFeeBillById(Integer id) {
        return feeBillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("账单不存在"));
    }

    public List<FeeBill> getAllFeeBills() {
        return feeBillRepository.findAll();
    }

    public List<FeeBill> getFeeBillsByChild(Integer childId) {
        return feeBillRepository.findByChildId(childId);
    }

    public List<FeeBill> getFeeBillsByParent(Integer parentId) {
        return feeBillRepository.findByParentId(parentId);
    }

    public List<FeeBill> getFeeBillsByMonth(String billMonth) {
        return feeBillRepository.findByBillMonth(billMonth);
    }

    public List<FeeBill> getFeeBillsByStatus(Integer paymentStatus) {
        return feeBillRepository.findByPaymentStatus(paymentStatus);
    }

    public FeeBill getFeeBillByChildAndMonth(Integer childId, String billMonth) {
        return feeBillRepository.findByChildIdAndBillMonth(childId, billMonth)
                .orElseThrow(() -> new RuntimeException("账单不存在"));
    }

    public List<FeeBill> getUnpaidBills() {
        return feeBillRepository.findUnpaidBills();
    }

    public List<FeeBill> getOverdueBills() {
        return feeBillRepository.findOverdueBills(LocalDateTime.now());
    }

    @Transactional
    public FeeBill updateFeeBill(Integer id, FeeBill feeBill) {
        FeeBill oldBill = getFeeBillById(id);

        if (feeBill.getManageDays() != null && feeBill.getManageDays() > 0) {
            oldBill.setManageDays(feeBill.getManageDays());
        }
        if (feeBill.getUnitPrice() != null) {
            oldBill.setUnitPrice(feeBill.getUnitPrice());
        }
        if (feeBill.getDiscountAmount() != null) {
            oldBill.setDiscountAmount(feeBill.getDiscountAmount());
        }
        if (StringUtils.hasText(feeBill.getTimeSlot())) {
            oldBill.setTimeSlot(feeBill.getTimeSlot());
        }

        BigDecimal payableAmount = oldBill.getUnitPrice().multiply(new BigDecimal(oldBill.getManageDays()));
        BigDecimal discountAmount = oldBill.getDiscountAmount();
        if (discountAmount == null) {
            discountAmount = BigDecimal.ZERO;
        }
        payableAmount = payableAmount.subtract(discountAmount);
        oldBill.setPayableAmount(payableAmount);

        return feeBillRepository.save(oldBill);
    }

    @Transactional
    public FeeBill payBill(Integer id, BigDecimal actualAmount) {
        FeeBill feeBill = getFeeBillById(id);
        feeBill.setActualAmount(actualAmount);
        feeBill.setPaymentStatus(FeeEnums.PaymentStatus.PAID.getCode());
        return feeBillRepository.save(feeBill);
    }

    @Transactional
    public void deleteFeeBill(Integer id) {
        FeeBill feeBill = getFeeBillById(id);
        feeBillRepository.delete(feeBill);
    }

    public List<FeeBill> getFeeBillsByParentAndStatus(Integer parentId, Integer paymentStatus) {
        return feeBillRepository.findByParentIdAndPaymentStatus(parentId, paymentStatus);
    }
}