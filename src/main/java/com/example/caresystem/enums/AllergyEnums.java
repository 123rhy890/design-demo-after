package com.example.caresystem.enums;

/**
 * 过敏相关枚举类
 * 定义过敏类型、状态等常量
 * @author rhy
 */
public class AllergyEnums {

    /**
     * 过敏类型枚举
     */
    public enum AllergyType {
        FOOD("food", "食物过敏", "对特定食物过敏"),
        DRUG("drug", "药物过敏", "对特定药物过敏"),
        ENVIRONMENTAL("environmental", "环境过敏", "对环境因素过敏（花粉、尘螨等）"),
        SKIN("skin", "皮肤过敏", "皮肤接触过敏"),
        OTHER("other", "其他过敏", "其他类型过敏");

        private final String code;
        private final String name;
        private final String description;

        AllergyType(String code, String name, String description) {
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

        public static AllergyType getByCode(String code) {
            for (AllergyType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return null;
        }

        public static String getNameByCode(String code) {
            AllergyType type = getByCode(code);
            return type != null ? type.getName() : "未知";
        }

        /**
         * 获取常见的过敏原列表（食物）
         */
        public static String[] getCommonFoodAllergens() {
            return new String[]{
                    "牛奶", "鸡蛋", "花生", "坚果", "鱼类", "虾蟹", "大豆", "小麦",
                    "芒果", "菠萝", "菠萝蜜", "海鲜", "巧克力", "蘑菇", "芝麻"
            };
        }

        /**
         * 获取常见的过敏原列表（药物）
         */
        public static String[] getCommonDrugAllergens() {
            return new String[]{
                    "青霉素", "头孢菌素", "阿司匹林", "布洛芬", "磺胺", "链霉素",
                    "破伤风抗毒素", "狂犬病疫苗", "麻醉药", "造影剂"
            };
        }
    }

    /**
     * 过敏状态枚举
     */
    public enum AllergyStatus {
        CURED(0, "已痊愈", "过敏症状已痊愈"),
        ACTIVE(1, "有效", "当前对过敏原存在过敏"),
        MONITORING(2, "观察中", "正在观察过敏症状"),
        UNCONFIRMED(3, "未确认", "过敏情况未确认");

        private final Integer code;
        private final String name;
        private final String description;

        AllergyStatus(Integer code, String name, String description) {
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

        public static AllergyStatus getByCode(Integer code) {
            for (AllergyStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }

        public static String getNameByCode(Integer code) {
            AllergyStatus status = getByCode(code);
            return status != null ? status.getName() : "未知";
        }
    }

    /**
     * 过敏反应程度枚举
     */
    public enum AllergySeverity {
        MILD(1, "轻微", "轻微不适，无需特殊处理"),
        MODERATE(2, "中度", "有明显症状，需要关注"),
        SEVERE(3, "严重", "症状明显，需要处理"),
        LIFE_THREATENING(4, "危急", "危及生命，需要紧急处理");

        private final Integer code;
        private final String name;
        private final String description;

        AllergySeverity(Integer code, String name, String description) {
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

        public static AllergySeverity getByCode(Integer code) {
            for (AllergySeverity severity : values()) {
                if (severity.code.equals(code)) {
                    return severity;
                }
            }
            return null;
        }

        public static String getNameByCode(Integer code) {
            AllergySeverity severity = getByCode(code);
            return severity != null ? severity.getName() : "未知";
        }
    }

    /**
     * 处理措施枚举
     */
    public enum TreatmentMethod {
        AVOIDANCE("avoidance", "避免接触", "避免接触过敏原"),
        MEDICATION("medication", "药物治疗", "使用抗过敏药物"),
        EMERGENCY("emergency", "紧急处理", "需要紧急医疗处理"),
        MONITORING("monitoring", "观察监测", "密切观察症状变化"),
        OTHER("other", "其他措施", "其他处理措施");

        private final String code;
        private final String name;
        private final String description;

        TreatmentMethod(String code, String name, String description) {
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

        public static TreatmentMethod getByCode(String code) {
            for (TreatmentMethod method : values()) {
                if (method.code.equals(code)) {
                    return method;
                }
            }
            return null;
        }
    }
}
