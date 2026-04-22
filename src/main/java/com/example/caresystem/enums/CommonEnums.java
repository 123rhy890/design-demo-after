package com.example.caresystem.enums;

/**
 * 通用工具枚举类
 * 定义系统通用的枚举类型
 * @author rhy
 */
public class CommonEnums {

    /**
     * 是否状态枚举
     */
    public enum YesNoStatus {
        NO(0, "否", "表示否或未完成"),
        YES(1, "是", "表示是或已完成");

        private final Integer code;
        private final String name;
        private final String description;

        YesNoStatus(Integer code, String name, String description) {
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

        public static YesNoStatus getByCode(Integer code) {
            for (YesNoStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }

        public static boolean isYes(Integer code) {
            return YES.code.equals(code);
        }

        public static boolean isNo(Integer code) {
            return NO.code.equals(code);
        }
    }

    /**
     * 操作类型枚举
     */
    public enum OperationType {
        CREATE("create", "新增", "新增数据"),
        UPDATE("update", "修改", "修改数据"),
        DELETE("delete", "删除", "删除数据"),
        QUERY("query", "查询", "查询数据"),
        IMPORT("import", "导入", "导入数据"),
        EXPORT("export", "导出", "导出数据"),
        LOGIN("login", "登录", "用户登录"),
        LOGOUT("logout", "登出", "用户登出"),
        AUDIT("audit", "审核", "审核操作");

        private final String code;
        private final String name;
        private final String description;

        OperationType(String code, String name, String description) {
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

        public static OperationType getByCode(String code) {
            for (OperationType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * 通用状态枚举
     */
    public enum CommonStatus {
        INACTIVE(0, "禁用", "禁用状态"),
        ACTIVE(1, "启用", "启用状态"),
        PENDING(2, "待处理", "等待处理状态");

        private final Integer code;
        private final String name;
        private final String description;

        CommonStatus(Integer code, String name, String description) {
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

        public static CommonStatus getByCode(Integer code) {
            for (CommonStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }

        public static boolean isActive(Integer code) {
            return ACTIVE.code.equals(code);
        }
    }

    /**
     * 排序方式枚举
     */
    public enum SortOrder {
        ASC("asc", "升序", "从小到大排序"),
        DESC("desc", "降序", "从大到小排序");

        private final String code;
        private final String name;
        private final String description;

        SortOrder(String code, String name, String description) {
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

        public static SortOrder getByCode(String code) {
            for (SortOrder order : values()) {
                if (order.code.equals(code)) {
                    return order;
                }
            }
            return ASC;
        }
    }

    /**
     * 数据范围枚举
     */
    public enum DataScope {
        SELF(0, "仅自己", "只能查看自己的数据"),
        DEPARTMENT(1, "所在部门", "查看所在部门的数据"),
        ALL(2, "全部数据", "查看全部数据");

        private final Integer code;
        private final String name;
        private final String description;

        DataScope(Integer code, String name, String description) {
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

        public static DataScope getByCode(Integer code) {
            for (DataScope scope : values()) {
                if (scope.code.equals(code)) {
                    return scope;
                }
            }
            return null;
        }
    }

    /**
     * 消息阅读状态枚举
     */
    public enum ReadStatus {
        UNREAD(0, "未读", "消息未阅读"),
        READ(1, "已读", "消息已阅读");

        private final Integer code;
        private final String name;
        private final String description;

        ReadStatus(Integer code, String name, String description) {
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

        public static ReadStatus getByCode(Integer code) {
            for (ReadStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }

        public static boolean isRead(Integer code) {
            return READ.code.equals(code);
        }
    }
}
