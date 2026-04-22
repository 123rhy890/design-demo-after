package com.example.caresystem.enums;

/**
 * 错误码枚举类
 * 定义系统统一的错误码和错误信息
 * @author rhy
 */
public class ErrorCodeEnums {

    /**
     * 系统错误码枚举
     */
    public enum SystemError {
        SUCCESS(0, "操作成功"),
        SYSTEM_ERROR(500, "系统异常，请联系管理员"),
        PARAM_ERROR(400, "参数错误"),
        UNAUTHORIZED(401, "未授权，请先登录"),
        FORBIDDEN(403, "权限不足，无法访问"),
        NOT_FOUND(404, "请求的资源不存在"),
        METHOD_NOT_ALLOWED(405, "请求方法不允许"),
        REQUEST_TIMEOUT(408, "请求超时"),
        TOO_MANY_REQUESTS(429, "请求过于频繁，请稍后再试");

        private final Integer code;
        private final String message;

        SystemError(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public static SystemError getByCode(Integer code) {
            for (SystemError error : values()) {
                if (error.code.equals(code)) {
                    return error;
                }
            }
            return SYSTEM_ERROR;
        }
    }

    /**
     * 业务错误码枚举
     */
    public enum BusinessError {
        // 用户相关错误 10000-19999
        USER_NOT_EXIST(10001, "用户不存在"),
        USER_EXIST(10002, "用户已存在"),
        USER_DISABLED(10003, "用户已被禁用"),
        USER_PASSWORD_ERROR(10004, "密码错误"),
        USER_ROLE_INVALID(10005, "用户角色无效"),
        USER_PHONE_EXIST(10006, "手机号已存在"),
        USER_EMAIL_EXIST(10007, "邮箱已存在"),
        USER_NOT_ACTIVE(10008, "用户未激活"),
        USER_LOCKED(10009, "用户已被锁定"),

        // 儿童相关错误 20000-29999
        CHILD_NOT_EXIST(20001, "儿童不存在"),
        CHILD_EXIST(20002, "儿童已存在"),
        CHILD_NOT_IN_CLASS(20003, "儿童不在班级中"),
        CHILD_CLASS_FULL(20004, "班级已满，无法加入"),
        CHILD_AGE_INVALID(20005, "儿童年龄不符合要求"),

        // 预约相关错误 30000-39999
        RESERVATION_NOT_EXIST(30001, "预约记录不存在"),
        RESERVATION_EXIST(30002, "预约记录已存在"),
        RESERVATION_DATE_INVALID(30003, "预约日期无效"),
        RESERVATION_TIME_CONFLICT(30004, "预约时间冲突"),
        RESERVATION_CAPACITY_FULL(30005, "预约名额已满"),
        RESERVATION_STATUS_INVALID(30006, "预约状态无效"),
        RESERVATION_CANNOT_CANCEL(30007, "预约无法取消"),

        // 考勤相关错误 40000-49999
        ATTENDANCE_NOT_EXIST(40001, "考勤记录不存在"),
        ATTENDANCE_EXIST(40002, "考勤记录已存在"),
        ATTENDANCE_CHECKIN_ERROR(40003, "签到失败"),
        ATTENDANCE_CHECKOUT_ERROR(40004, "签退失败"),
        ATTENDANCE_NO_RESERVATION(40005, "没有有效的预约"),
        ATTENDANCE_ALREADY_CHECKIN(40006, "已签到，无需重复操作"),
        ATTENDANCE_NOT_CHECKIN(40007, "未签到，无法签退"),

        // 费用相关错误 50000-59999
        BILL_NOT_EXIST(50001, "账单不存在"),
        BILL_EXIST(50002, "账单已存在"),
        BILL_STATUS_INVALID(50003, "账单状态无效"),
        BILL_PAYMENT_ERROR(50004, "支付失败"),
        BILL_AMOUNT_ERROR(50005, "金额计算错误"),
        BILL_GENERATE_ERROR(50006, "账单生成失败"),
        BILL_OVERDUE(50007, "账单已逾期"),

        // 日常记录相关错误 60000-69999
        DAILY_RECORD_EXIST(60001, "今日记录已存在"),
        DAILY_RECORD_NOT_EXIST(60002, "记录不存在"),
        DAILY_RECORD_PUSH_ERROR(60003, "推送失败"),

        // 沟通相关错误 70000-79999
        MESSAGE_SEND_ERROR(70001, "消息发送失败"),
        MESSAGE_RECEIVER_INVALID(70002, "接收人无效"),

        // 文件相关错误 80000-89999
        FILE_UPLOAD_ERROR(80001, "文件上传失败"),
        FILE_TYPE_INVALID(80002, "文件类型不支持"),
        FILE_SIZE_EXCEED(80003, "文件大小超过限制"),
        FILE_NOT_EXIST(80004, "文件不存在"),

        // 验证码相关错误 90000-90999
        CAPTCHA_ERROR(90001, "验证码错误"),
        CAPTCHA_EXPIRED(90002, "验证码已过期"),

        // 其他业务错误
        OPERATION_NOT_ALLOWED(91001, "操作不允许"),
        DATA_NOT_EXIST(91002, "数据不存在"),
        DATA_EXIST(91003, "数据已存在"),
        DATA_VALIDATE_ERROR(91004, "数据验证失败");

        private final Integer code;
        private final String message;

        BusinessError(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public static BusinessError getByCode(Integer code) {
            for (BusinessError error : values()) {
                if (error.code.equals(code)) {
                    return error;
                }
            }
            return null;
        }
    }

    /**
     * 验证错误码枚举
     */
    public enum ValidateError {
        FIELD_REQUIRED(100001, "字段不能为空"),
        FIELD_FORMAT_ERROR(100002, "字段格式错误"),
        FIELD_LENGTH_ERROR(100003, "字段长度错误"),
        FIELD_RANGE_ERROR(100004, "字段值超出范围"),
        FIELD_UNIQUE_ERROR(100005, "字段值必须唯一"),
        FIELD_PATTERN_ERROR(100006, "字段格式不匹配"),

        // 用户验证
        USERNAME_PATTERN(110001, "用户名格式错误"),
        PASSWORD_PATTERN(110002, "密码格式错误"),
        PHONE_PATTERN(110003, "手机号格式错误"),
        EMAIL_PATTERN(110004, "邮箱格式错误"),

        // 日期验证
        DATE_FORMAT(120001, "日期格式错误"),
        DATE_RANGE(120002, "日期范围错误"),
        TIME_FORMAT(120003, "时间格式错误"),

        // 业务验证
        RESERVATION_TIME(130001, "预约时间冲突"),
        CLASS_CAPACITY(130002, "班级容量已满"),
        PAYMENT_AMOUNT(130003, "支付金额不正确"),

        // 文件验证
        FILE_SIZE(140001, "文件大小超过限制"),
        FILE_TYPE(140002, "文件类型不支持"),
        FILE_NAME(140003, "文件名不规范");

        private final Integer code;
        private final String message;

        ValidateError(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public static ValidateError getByCode(Integer code) {
            for (ValidateError error : values()) {
                if (error.code.equals(code)) {
                    return error;
                }
            }
            return null;
        }
    }
}
