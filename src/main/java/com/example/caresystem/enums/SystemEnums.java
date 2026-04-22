package com.example.caresystem.enums;

/**
 * 系统相关枚举类
 * 定义系统配置、通知类型等常量
 * @author rhy
 */
public class SystemEnums {

    /**
     * 班级状态枚举
     */
    public enum ClassStatus {
        DISABLED(0, "停用", "班级已停用，不再接收新学生"),
        NORMAL(1, "正常", "班级正常运行"),
        MAINTENANCE(2, "维护", "班级处于维护状态");

        private final Integer code;
        private final String name;
        private final String description;

        ClassStatus(Integer code, String name, String description) {
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

        public static ClassStatus getByCode(Integer code) {
            for (ClassStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }

        public static boolean isActive(Integer code) {
            return NORMAL.code.equals(code);
        }
    }

    /**
     * 通知类型枚举
     */
    public enum NotificationType {
        CHECKIN("checkin", "签到通知", "儿童签到/签退通知"),
        RESERVATION("reservation", "预约通知", "预约审核结果通知"),
        DAILY_REPORT("daily_report", "日常报告", "日常状态报告"),
        ABNORMAL("abnormal", "异常通知", "儿童异常情况通知"),
        BILL("bill", "账单通知", "账单生成或缴费提醒"),
        MESSAGE("message", "消息通知", "家校沟通消息"),
        SYSTEM("system", "系统通知", "系统公告或维护通知");

        private final String code;
        private final String name;
        private final String description;

        NotificationType(String code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public static NotificationType getByCode(String code) {
            for (NotificationType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return null;
        }

        /**
         * 获取家长关心的通知类型
         */
        public static NotificationType[] getParentNotifications() {
            return new NotificationType[]{CHECKIN, RESERVATION, DAILY_REPORT, ABNORMAL, BILL, MESSAGE};
        }

        /**
         * 获取教师关心的通知类型
         */
        public static NotificationType[] getTeacherNotifications() {
            return new NotificationType[]{RESERVATION, ABNORMAL, MESSAGE};
        }
    }

    /**
     * 文件类型枚举
     */
    public enum FileType {
        AVATAR("avatar", "头像", "用户头像图片"),
        SIGN_IMAGE("sign_image", "签字照片", "签到签退签字照片"),
        ABNORMAL_IMAGE("abnormal_image", "异常照片", "异常情况照片"),
        VOUCHER("voucher", "缴费凭证", "缴费凭证图片"),
        COMMUNICATION_ATTACHMENT("comm_attachment", "沟通附件", "家校沟通附件"),
        SYSTEM_IMAGE("system_image", "系统图片", "系统相关图片");

        private final String code;
        private final String name;
        private final String description;

        FileType(String code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public static FileType getByCode(String code) {
            for (FileType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return null;
        }

        /**
         * 获取图片文件类型
         */
        public static String[] getImageTypes() {
            return new String[]{"image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp"};
        }

        /**
         * 获取允许的文件扩展名
         */
        public static String[] getAllowedExtensions() {
            return new String[]{".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp", ".pdf", ".doc", ".docx"};
        }
    }

    /**
     * 审核操作类型枚举
     */
    public enum AuditAction {
        APPROVE("approve", "通过", "审核通过"),
        REJECT("reject", "驳回", "审核驳回"),
        MODIFY("modify", "修改", "修改后通过");

        private final String code;
        private final String name;
        private final String description;

        AuditAction(String code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public static AuditAction getByCode(String code) {
            for (AuditAction action : values()) {
                if (action.code.equals(code)) {
                    return action;
                }
            }
            return null;
        }
    }

    /**
     * 系统配置类型枚举
     */
    public enum ConfigType {
        SYSTEM("system", "系统配置", "系统基本配置"),
        BUSINESS("business", "业务配置", "业务规则配置"),
        NOTIFICATION("notification", "通知配置", "通知相关配置"),
        SECURITY("security", "安全配置", "安全相关配置");

        private final String code;
        private final String name;
        private final String description;

        ConfigType(String code, String name, String description) {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public static ConfigType getByCode(String code) {
            for (ConfigType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }
}
