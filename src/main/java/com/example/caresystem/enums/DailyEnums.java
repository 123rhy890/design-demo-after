package com.example.caresystem.enums;

/**
 * 日常记录相关枚举类
 * 定义日常状态、异常类型等常量
 * @author rhy
 */
public class DailyEnums {

    /**
     * 日常记录类型枚举
     */
    public enum RecordType {
        DIET("diet", "饮食记录", "记录儿童饮食情况"),
        HOMEWORK("homework", "作业记录", "记录儿童作业完成情况"),
        ACTIVITY("activity", "活动记录", "记录儿童活动参与情况"),
        BEHAVIOR("behavior", "行为记录", "记录儿童行为表现"),
        NAP("nap", "午休记录", "记录儿童午休情况");

        private final String code;
        private final String name;
        private final String description;

        RecordType(String code, String name, String description) {
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

        public static RecordType getByCode(String code) {
            for (RecordType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * 异常类型枚举
     */
    public enum AbnormalType {
        COUGH("cough", "咳嗽", "呼吸道异常"),
        FEVER("fever", "发烧", "体温异常升高"),
        ALLERGY("allergy", "过敏", "接触过敏原"),
        INJURY("injury", "受伤", "磕碰受伤"),
        BEHAVIOR_PROBLEM("behavior_problem", "行为问题", "行为异常"),
        DIET_PROBLEM("diet_problem", "饮食问题", "饮食异常"),
        OTHER("other", "其他异常", "其他类型异常");

        private final String code;
        private final String name;
        private final String description;

        AbnormalType(String code, String name, String description) {
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

        public static AbnormalType getByCode(String code) {
            for (AbnormalType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return null;
        }

        /**
         * 获取紧急异常类型（需要立即通知）
         */
        public static String[] getEmergencyTypes() {
            return new String[]{FEVER.code, INJURY.code};
        }

        /**
         * 判断是否为紧急异常
         */
        public static boolean isEmergency(String code) {
            return FEVER.code.equals(code) || INJURY.code.equals(code);
        }
    }

    /**
     * 异常严重程度枚举
     */
    public enum SeverityLevel {
        MILD(1, "轻微", "轻微异常，不影响正常活动"),
        MODERATE(2, "中度", "需要关注和处理"),
        SEVERE(3, "严重", "需要立即处理"),
        CRITICAL(4, "危急", "需要紧急医疗处理");

        private final Integer code;
        private final String name;
        private final String description;

        SeverityLevel(Integer code, String name, String description) {
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

        public static SeverityLevel getByCode(Integer code) {
            for (SeverityLevel level : values()) {
                if (level.code.equals(code)) {
                    return level;
                }
            }
            return null;
        }

        /**
         * 获取需要立即通知的严重程度
         */
        public static boolean needImmediateNotify(Integer code) {
            return code >= SEVERE.code;
        }
    }

    /**
     * 推送状态枚举
     */
    public enum PushStatus {
        NOT_PUSHED(0, "未推送", "尚未推送给家长"),
        PUSHED(1, "已推送", "已成功推送给家长"),
        PUSH_FAILED(2, "推送失败", "推送失败，需要重试");

        private final Integer code;
        private final String name;
        private final String description;

        PushStatus(Integer code, String name, String description) {
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

        public static PushStatus getByCode(Integer code) {
            for (PushStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }

        public static boolean isSuccess(Integer code) {
            return PUSHED.code.equals(code);
        }
    }
}
