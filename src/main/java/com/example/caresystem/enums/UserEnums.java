package com.example.caresystem.enums;

/**
 * 用户相关枚举类
 * 定义用户角色、状态等常量
 * @author rhy
 */
public class UserEnums {

    /**
     * 用户角色枚举
     */
    public enum Role {
        ADMIN(0, "管理员", "负责系统管理、用户审核等"),
        TEACHER(1, "教师", "负责班级管理、签到记录等"),
        PARENT(2, "家长", "查看孩子状态、预约缴费等");

        private final Integer code;
        private final String name;
        private final String description;

        Role(Integer code, String name, String description) {
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

        /**
         * 根据code获取角色枚举
         */
        public static Role getByCode(Integer code) {
            for (Role role : values()) {
                if (role.code.equals(code)) {
                    return role;
                }
            }
            return null;
        }

        /**
         * 检查角色代码是否有效
         */
        public static boolean isValid(Integer code) {
            return getByCode(code) != null;
        }

        /**
         * 获取所有角色名称
         */
        public static String[] getAllNames() {
            Role[] roles = values();
            String[] names = new String[roles.length];
            for (int i = 0; i < roles.length; i++) {
                names[i] = roles[i].getName();
            }
            return names;
        }
    }

    /**
     * 用户状态枚举
     */
    public enum Status {
        DISABLED(0, "禁用", "账号被禁用，无法登录"),
        NORMAL(1, "正常", "账号正常可用"),
        PENDING(2, "待审核", "注册后等待管理员审核"),
        LOCKED(3, "锁定", "密码错误次数过多被锁定");

        private final Integer code;
        private final String name;
        private final String description;

        Status(Integer code, String name, String description) {
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

        public static Status getByCode(Integer code) {
            for (Status status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }

        public static boolean isActive(Integer code) {
            return NORMAL.code.equals(code);
        }

        public static boolean canLogin(Integer code) {
            return NORMAL.code.equals(code);
        }
    }

    /**
     * 性别枚举
     */
    public enum Gender {
        FEMALE(0, "女", "女性"),
        MALE(1, "男", "男性");

        private final Integer code;
        private final String name;
        private final String description;

        Gender(Integer code, String name, String description) {
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

        public static Gender getByCode(Integer code) {
            for (Gender gender : values()) {
                if (gender.code.equals(code)) {
                    return gender;
                }
            }
            return null;
        }

        public static String getNameByCode(Integer code) {
            Gender gender = getByCode(code);
            return gender != null ? gender.getName() : "未知";
        }
    }
}
