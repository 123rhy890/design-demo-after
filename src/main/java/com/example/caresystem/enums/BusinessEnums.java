package com.example.caresystem.enums;

/**
 * 业务相关枚举类
 * 定义预约、考勤等业务相关常量
 * @author rhy
 */
public class BusinessEnums {

    /**
     * 预约状态枚举
     */
    public enum ReservationStatus {
        REVIEW("review", "待审核", "等待教师审核"),
        CONFIRM("confirm", "已确认", "审核通过，预约成功"),
        CANCEL("cancel", "已取消", "预约被取消"),
        EXPIRED("expired", "已过期", "超过预约日期未确认");

        private final String code;
        private final String name;
        private final String description;

        ReservationStatus(String code, String name, String description) {
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

        public static ReservationStatus getByCode(String code) {
            for (ReservationStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }

        public static boolean canCheckIn(String code) {
            return CONFIRM.code.equals(code);
        }

        /**
         * 获取可取消的状态
         */
        public static String[] getCancelableStatus() {
            return new String[]{REVIEW.code, CONFIRM.code};
        }
    }

    /**
     * 托管时段枚举
     */
    public enum TimeSlot {
        MORNING("morning", "上午", "08:00-12:00"),
        AFTERNOON("afternoon", "下午", "13:00-17:00"),
        ALL_DAY("all_day", "全天", "08:00-17:00"),
        TEMPORARY("temporary", "临时", "按小时计费");

        private final String code;
        private final String name;
        private final String timeRange;

        TimeSlot(String code, String name, String timeRange) {
            this.code = code;
            this.name = name;
            this.timeRange = timeRange;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getTimeRange() {
            return timeRange;
        }

        public static TimeSlot getByCode(String code) {
            for (TimeSlot slot : values()) {
                if (slot.code.equals(code)) {
                    return slot;
                }
            }
            return null;
        }

        public static String getNameByCode(String code) {
            TimeSlot slot = getByCode(code);
            return slot != null ? slot.getName() : "未知";
        }

        /**
         * 获取计费时段列表
         */
        public static String[] getFeeSlots() {
            return new String[]{MORNING.code, AFTERNOON.code, ALL_DAY.code};
        }
    }

    /**
     * 考勤状态枚举
     */
    public enum AttendanceStatus {
        LATE(0, "迟到", "未按时签到"),
        NORMAL(1, "正常", "按时签到签退"),
        EARLY(2, "早退", "未到时间提前签退"),
        ABSENT(3, "缺勤", "未到园托管");

        private final Integer code;
        private final String name;
        private final String description;

        AttendanceStatus(Integer code, String name, String description) {
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

        public static AttendanceStatus getByCode(Integer code) {
            for (AttendanceStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }

        public static String getNameByCode(Integer code) {
            AttendanceStatus status = getByCode(code);
            return status != null ? status.getName() : "未知";
        }

        /**
         * 获取需要处理的状态
         */
        public static Integer[] getAbnormalStatus() {
            return new Integer[]{LATE.code, EARLY.code, ABSENT.code};
        }
    }

    /**
     * 接送类型枚举
     */
    public enum PickupType {
        DROP_OFF("drop_off", "送离", "家长送孩子离开"),
        PICK_UP("pick_up", "接回", "家长接孩子回家");

        private final String code;
        private final String name;
        private final String description;

        PickupType(String code, String name, String description) {
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

        public static PickupType getByCode(String code) {
            for (PickupType type : values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }
}
