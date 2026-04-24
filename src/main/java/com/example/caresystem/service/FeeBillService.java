package com.example.caresystem.service;

import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.FeeBill;
import com.example.caresystem.entity.User;
import com.example.caresystem.enums.FeeEnums;
import com.example.caresystem.enums.UserEnums;
import com.example.caresystem.repository.ChildRepository;
import com.example.caresystem.repository.DiscountRuleRepository;
import com.example.caresystem.repository.FeeBillRepository;
import com.example.caresystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeeBillService {

    private static final Logger log = LoggerFactory.getLogger(FeeBillService.class);

    @Autowired
    private FeeBillRepository feeBillRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiscountRuleRepository discountRuleRepository;

    @Transactional
    public FeeBill addFeeBill(FeeBill feeBill, Integer childId, Integer parentId, Integer createById, Integer discountId) {
        log.info("开始添加费用账单，childId: {}, parentId: {}, createById: {}, discountId: {}", childId, parentId, createById, discountId);
        log.debug("接收到的 FeeBill 对象: {}", feeBill);

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
        log.debug("设置儿童: {}", child.getChildName());

        User parent = userRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("家长不存在"));
        if (!UserEnums.Role.PARENT.getCode().equals(parent.getRoleType())) {
            throw new RuntimeException("该用户不是家长角色");
        }
        feeBill.setParent(parent);
        log.debug("设置家长: {}", parent.getUsername());

        User createBy = userRepository.findById(createById)
                .orElseThrow(() -> new RuntimeException("创建人不存在"));
        feeBill.setCreateBy(createBy);
        log.debug("设置创建人: {}", createBy.getUsername());

        if (discountId != null) {
            discountRuleRepository.findById(discountId).ifPresent(discountRule -> {
                feeBill.setDiscountRule(discountRule);
                log.debug("设置优惠规则: {}", discountRule.getDiscountName());
            });
        }

        BigDecimal payableAmount = feeBill.getUnitPrice().multiply(new BigDecimal(feeBill.getManageDays()));
        if (feeBill.getDiscountAmount() == null) {
            feeBill.setDiscountAmount(BigDecimal.ZERO);
        }
        payableAmount = payableAmount.subtract(feeBill.getDiscountAmount());
        feeBill.setPayableAmount(payableAmount);
        feeBill.setActualAmount(BigDecimal.ZERO);
        feeBill.setPaymentStatus(FeeEnums.PaymentStatus.UNPAID.getCode());
        log.debug("计算应付金额: {}, 实际优惠: {}, 缴费状态: {}", payableAmount, feeBill.getDiscountAmount(), feeBill.getPaymentStatus());

        FeeBill savedFeeBill = feeBillRepository.save(feeBill);
        log.info("费用账单添加成功，账单ID: {}", savedFeeBill.getBillId());
        log.debug("保存后的 FeeBill 对象: {}", savedFeeBill);
        return savedFeeBill;
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
        log.info("查询家长账单列表，parentId: {}", parentId);
        List<FeeBill> bills = feeBillRepository.findByParentId(parentId);
        log.info("查询到账单数量: {}", bills.size());
        return bills;
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

    /**
     * 分页条件查询账单（管理员端）
     */
    public Page<FeeBill> getFeeBillsByConditions(String childName, Integer paymentStatus,
                                                  String billMonth, int pageNum, int pageSize) {
        log.info("分页查询账单，childName: {}, status: {}, month: {}, pageNum: {}, pageSize: {}", 
                childName, paymentStatus, billMonth, pageNum, pageSize);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        String name = StringUtils.hasText(childName) ? childName : null;
        String month = StringUtils.hasText(billMonth) ? billMonth : null;
        Page<FeeBill> page = feeBillRepository.findByConditions(name, paymentStatus, month, pageable);
        log.info("查询到总记录数: {}", page.getTotalElements());
        return page;
    }

    /**
     * 获取指定月份费用统计
     */
    public Map<String, Object> getFeeStatsByMonth(String billMonth) {
        log.info("统计费用数据，月份: {}", billMonth);
        Map<String, Object> stats = new HashMap<>();
        BigDecimal totalFee = feeBillRepository.sumPayableByMonth(billMonth);
        BigDecimal paidFee = feeBillRepository.sumActualByMonth(billMonth);
        Long unpaidCount = feeBillRepository.countUnpaidByMonth(billMonth);
        
        totalFee = totalFee == null ? BigDecimal.ZERO : totalFee;
        paidFee = paidFee == null ? BigDecimal.ZERO : paidFee;
        unpaidCount = unpaidCount == null ? 0L : unpaidCount;
        
        BigDecimal unpaidFee = totalFee.subtract(paidFee);
        if (unpaidFee.compareTo(BigDecimal.ZERO) < 0) unpaidFee = BigDecimal.ZERO;

        stats.put("totalFee", totalFee);
        stats.put("paidFee", paidFee);
        stats.put("unpaidFee", unpaidFee);
        stats.put("unpaidCount", unpaidCount);
        log.info("统计结果: {}", stats);
        return stats;
    }

    /**
     * 获取近N个月的费用趋势数据
     */
    public Map<String, Object> getFeeTrend(int months) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        List<String> monthList = new ArrayList<>();
        YearMonth current = YearMonth.now();
        for (int i = months - 1; i >= 0; i--) {
            monthList.add(current.minusMonths(i).format(fmt));
        }

        List<Object[]> rows = feeBillRepository.sumAmountGroupByMonth(monthList);
        Map<String, BigDecimal[]> dataMap = new HashMap<>();
        for (Object[] row : rows) {
            String m = (String) row[0];
            BigDecimal payable = (BigDecimal) row[1];
            BigDecimal actual = (BigDecimal) row[2];
            dataMap.put(m, new BigDecimal[]{payable, actual});
        }

        List<BigDecimal> payableList = new ArrayList<>();
        List<BigDecimal> actualList = new ArrayList<>();
        for (String m : monthList) {
            BigDecimal[] arr = dataMap.getOrDefault(m, new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            payableList.add(arr[0]);
            actualList.add(arr[1]);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("months", monthList);
        result.put("payable", payableList);
        result.put("actual", actualList);
        return result;
    }

    /**
     * 发送欠费提醒（更新提醒次数）
     */
    @Transactional
    public void sendRemind(Integer billId) {
        FeeBill bill = getFeeBillById(billId);
        if (!FeeEnums.PaymentStatus.UNPAID.getCode().equals(bill.getPaymentStatus())) {
            throw new RuntimeException("该账单已缴费，无需提醒");
        }
        feeBillRepository.incrementRemindTimes(billId, LocalDateTime.now());
    }

    /**
     * 获取家长的费用统计数据
     * @param parentId 家长ID
     * @return 统计数据
     */
    public Map<String, Object> getParentFeeStats(Integer parentId) {
        Map<String, Object> stats = new HashMap<>();
        List<FeeBill> bills = feeBillRepository.findByParentId(parentId);
        
        BigDecimal unpaid = BigDecimal.ZERO;
        BigDecimal paid = BigDecimal.ZERO;
        int billCount = bills.size();
        
        for (FeeBill bill : bills) {
            if (bill.getPaymentStatus() == 0 || bill.getPaymentStatus() == 2 || bill.getPaymentStatus() == 4) { // 0-未缴, 2-欠费, 4-审核中
                unpaid = unpaid.add(bill.getPayableAmount().subtract(bill.getActualAmount()));
            } else if (bill.getPaymentStatus() == 1) { // 1-已缴
                paid = paid.add(bill.getActualAmount());
            }
        }
        
        stats.put("unpaid", unpaid);
        stats.put("paid", paid);
        stats.put("billCount", billCount);
        return stats;
    }
}