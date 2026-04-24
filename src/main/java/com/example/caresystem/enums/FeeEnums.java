package com.example.caresystem.enums;

/**
 * 费用相关枚举类
 * 定义费用状态、优惠类型等常量
 * @author rhy
 */
public class FeeEnums {

    /**
     * 费用状态枚举
     */
    public enum FeeStatus {
        DISABLED(0, "停用", "规则已停用"),
        NORMAL(1, "正常", "规则正常生效");

        private final Integer code;
        private final String name;
        private final String description;

        FeeStatus(Integer code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public static FeeStatus getByCode(Integer code) {
            for (FeeStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }
    }

    /**
     * 缴费状态枚举
     */
    public enum PaymentStatus {
        UNPAID(0, "未缴费", "账单尚未支付"),
        PAID(1, "已缴费", "账单已成功支付"),
        OVERDUE(2, "欠费", "账单已逾期未支付"),
        PARTIAL(3, "部分支付", "已支付部分金额"),
        AUDITING(4, "审核中", "凭证已上传，等待管理员审核");

        private final Integer code;
        private final String name;
        private final String description;

        PaymentStatus(Integer code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public static PaymentStatus getByCode(Integer code) {
            for (PaymentStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }

        public static String getNameByCode(Integer code) {
            PaymentStatus status = getByCode(code);
            return status != null ? status.getName() : "未知";
        }

        /**
         * 获取有效的支付状态
         */
        public static boolean isValidForPayment(Integer code) {
            return UNPAID.code.equals(code) || PARTIAL.code.equals(code);
        }

        /**
         * 获取需要提醒的状态
         */
        public static Integer[] getReminderStatus() {
            return new Integer[]{UNPAID.code, OVERDUE.code};
        }
    }

    /**
     * 优惠类型枚举
     */
    public enum DiscountType {
        PERCENTAGE(0, "折扣", "按百分比折扣，如9折、8.5折"),
        FIXED_AMOUNT(1, "固定金额", "减免固定金额，如减50元"),
        FULL_REDUCTION(2, "满减", "满一定金额减免，如满100减20"),
        MULTI_CHILD(3, "多孩优惠", "多孩家庭享受的优惠"),
        EARLY_PAYMENT(4, "提前支付优惠", "提前支付享受的折扣");

        private final Integer code;
        private final String name;
        private final String description;

        DiscountType(Integer code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public static DiscountType getByCode(Integer code) {
            for (DiscountType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return null;
        }

        public static String getNameByCode(Integer code) {
            DiscountType type = getByCode(code);
            return type != null ? type.getName() : "未知";
        }
    }

    /**
     * 凭证审核状态枚举
     */
    public enum VoucherStatus {
        PENDING(0, "待审核", "等待管理员审核"),
        APPROVED(1, "审核通过", "凭证审核通过"),
        REJECTED(2, "审核驳回", "凭证审核未通过");

        private final Integer code;
        private final String name;
        private final String description;

        VoucherStatus(Integer code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public static VoucherStatus getByCode(Integer code) {
            for (VoucherStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }

        public static String getNameByCode(Integer code) {
            VoucherStatus status = getByCode(code);
            return status != null ? status.getName() : "未知";
        }

        /**
         * 获取待处理的状态
         */
        public static boolean isPending(Integer code) {
            return PENDING.code.equals(code);
        }
    }

    /**
     * 支付方式枚举
     */
    public enum PaymentMethod {
        WECHAT(1, "微信支付", "使用微信扫码支付"),
        ALIPAY(2, "支付宝", "使用支付宝支付"),
        BANK_TRANSFER(3, "银行转账", "通过银行转账支付"),
        CASH(4, "现金支付", "线下现金支付"),
        OTHER(99, "其他方式", "其他支付方式");

        private final Integer code;
        private final String name;
        private final String description;

        PaymentMethod(Integer code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public static PaymentMethod getByCode(Integer code) {
            for (PaymentMethod method : values()) {
                if (method.code.equals(code)) {
                    return method;
                }
            }
            return OTHER;
        }

        public static String getNameByCode(Integer code) {
            PaymentMethod method = getByCode(code);
            return method.getName();
        }

        /**
         * 获取在线支付方式
         */
        public static PaymentMethod[] getOnlineMethods() {
            return new PaymentMethod[]{WECHAT, ALIPAY};
        }
    }

    /**
     * 滞纳金计算方式枚举
     */
    public enum PenaltyType {
        PERCENTAGE_DAILY(1, "每日百分比", "按欠款金额的每日百分比计算"),
        FIXED_DAILY(2, "每日固定金额", "每日收取固定金额的滞纳金"),
        FIXED_MONTHLY(3, "每月固定金额", "每月收取固定金额的滞纳金");

        private final Integer code;
        private final String name;
        private final String description;

        PenaltyType(Integer code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public static PenaltyType getByCode(Integer code) {
            for (PenaltyType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }
}
