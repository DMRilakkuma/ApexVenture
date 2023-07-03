/**
 * @projectName mytrain
 * @package com.jiawa.train.common.response
 * @className com.jiawa.train.common.response.ResultEnum
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.apxvt.common.response;

/**
 * ResultEnum
 * @author MC
 * @description
 * @date 2023/6/21 11:14
 * @version 1.0
 */
public enum ResultEnum {
    // ****************************【登录错误】****************************

    /**
     * 600 用户名或密码错误
     */
    PASSWORD_ERROR("600", "用户名或密码错误"),

    /**
     * 601 验证码过期
     */
    CODE_TIME_OUT("601", "验证码过期"),

    /**
     * 602 验证码错误
     */
    CODE_ERROR("602", "验证码错误"),

    /*
    * 603 手机号已注册
    */
    PHONE_REGISTERED("603", "手机号已注册"),


    // ****************************【公共错误】****************************
    /**
     * 1002(失败)
     */
    FAIL("1002", "失败"),

    // ****************************【文件错误】****************************
    /**
     * 2004(文件不能为空)
     */
    FILE_IS_EMPTY("2004", "文件不能为空"),

    /**
     * 2005（文件不能大于10M）
     */
    FILE_TOO_BIG("2005", "文件不能大于10M"),
    // ****************************【一级错误】****************************
    /**
     * 8001(数据不唯一错误)
     */
    NONUNIQUE_ERROR("8001", "不唯一错误"),
    /**
     * 8002(服务器缓存异常)
     */
    REDIS_NO_OPEN("8002", "服务器缓存异常"),
    /**
     * 8003(执行数据库操作错误)
     */
    EXECUTE_SQL_ERROR("8003", "执行数据库操作错误"),

    /**
     * 8004(参数错误，请确认格式是否正确)
     */
    PARAMS_ERROR("8004", "参数错误，请确认格式是否正确"),

    /**
     * 8005(文件路径错误)
     */
    FILE_PATH_ERROR("8005", "文件路径错误"),

    /**
     * 8006(IO异常)
     */
    IO_ABNORMAL("8006", "IO异常"),

    /**
     * 8007(内容不可读异常)
     */
    HTTP_MESSAGE_NOT_READABLE("8007", "内容不可读异常"),
    /**
     * 8008(json异常)
     */
    JSON_ERROR("8008", "json异常"),

    /**
     * 8009(空指针异常)
     */
    NULL_POINTER("8009", "空指针异常"),
    /**
     * 8010(类型转换异常)
     */
    CLASS_CAST("8010", "类型转换异常"),
    /**
     * 8011(未知方法异常)
     */
    NO_SUCH_METHOD("8011", "未知方法异常"),

    /**
     * 8012(下标越界异常)
     */
    INDEX_OUT_OF_BOUNDS("8012", "下标越界异常"),

    //****************************【http状态码】****************************

    /**
     * 100(继续)
     */
    CONTINUE("100", "继续"),

    /**
     * 101(切换协议)
     */
    SWITCHING_PROTOCOLS("101", "切换协议"),

    /**
     * 200(成功)
     */
    OK("200", "成功"),

    /**
     * 201(已创建)
     */
    CREATED("201", "已创建"),

    /**
     * 202(已接受)
     */
    ACCEPTED("202", "已接受"),

    /**
     * 203(非授权信息)
     */
    NON_AUTHORITATIVE_INFORMATION("203", "非授权信息"),

    /**
     * 204(无内容)
     */
    NO_CONTENT("204", "无内容"),

    /**
     * 205(重置内容)
     */
    RESET_CONTENT("205", "重置内容"),

    /**
     * 206(部分内容)
     */
    PARTIAL_CONTENT("206", "部分内容"),

    /**
     * 300(多种选择)
     */
    MULTIPLE_CHOICES("300", "多种选择"),

    /**
     * 301(永久移动)
     */
    MOVED_PERMANENTLY("301", "永久移动"),

    /**
     * 302(临时移动)
     */
    FOUND("302", "临时移动"),

    /**
     * 303(查看其他位置)
     */
    SEE_OTHER("303", "查看其他位置"),

    /**
     * 304(未修改)
     */
    NOT_MODIFIED("304", "未修改"),

    /**
     * 305(使用代理)
     */
    USE_PROXY("305", "使用代理"),

    /**
     * 306(临时重定向)
     */
    TEMPORARY_REDIRECT("307", "临时重定向"),

    /**
     * 400(错误请求)
     */
    BAD_REQUEST("400", "错误请求"),

    /**
     * 401(未登录)
     */
    UNAUTHORIZED("401", "登录失效"),

    /**
     * 403(未授权)
     */
    FORBIDDEN("403", "未授权"),

    /**
     * 404(未找到)
     */
    NOT_FOUND("404", "未找到"),

    /**
     * 405(方法禁用)
     */
    METHOD_NOT_ALLOWED("405", "方法禁用"),

    /**
     * 406(不接受)
     */
    NOT_ACCEPTABLE("406", "不接受"),

    /**
     * 407(需要代理授权)
     */
    PROXY_AUTHENTICATION_REQUIRED("407", "需要代理授权"),

    /**
     * 408(请求超时)
     */
    REQUEST_TIMEOUT("408", "请求超时"),

    /**
     * 409(冲突)
     */
    CONFLICT("409", "冲突"),

    /**
     * 410(已删除)
     */
    GONE("410", "已删除"),

    /**
     * 411(需要有效长度)
     */
    LENGTH_REQUIRED("411", "需要有效长度"),

    /**
     * 412(未满足前提条件)
     */
    PRECONDITION_FAILED("412", "未满足前提条件"),

    /**
     * 413(请求实体过大)
     */
    REQUEST_ENTITY_TOO_LARGE("413", "请求实体过大"),

    /**
     * 414(请求的 URI 过长)
     */
    REQUEST_URI_TOO_LONG("414", "请求的 URI 过长"),

    /**
     * 415(不支持的媒体类型)
     */
    UNSUPPORTED_MEDIA_TYPE("415", "不支持的媒体类型"),

    /**
     * 416(请求范围不符合要求)
     */
    REQUESTED_RANGE_NOT_SATISFIABLE("416", "请求范围不符合要求"),

    /**
     * 417(未满足期望值)
     */
    EXPECTATION_FAILED("417", "未满足期望值"),

    /**
     * 500(服务器内部错误)
     */
    INTERNAL_SERVER_ERROR("500", "服务器内部错误"),

    /**
     * 501(尚未实施)
     */
    NOT_IMPLEMENTED("501", "尚未实施"),

    /**
     * 502(错误网关)
     */
    BAD_GATEWAY("502", "错误网关"),

    /**
     * 503(服务不可用)
     */
    SERVICE_UNAVAILABLE("503", "服务不可用"),

    /**
     * 504(网关超时)
     */
    GATEWAY_TIMEOUT("504", "网关超时"),

    /**
     * 505(版本不受支持)
     */
    HTTP_VERSION_NOT_SUPPORTED("505", "版本不受支持"),
    // ****************************【其他错误】****************************

    /**
     * 9997(该id不存在)
     */
    FINDMSGBYID_FAILURE("9997", "枚举id不存在"),

    /**
     * 9998(该内容不存在)
     */
    FINDIDBYMSG_FAILURE("9998", "枚举内容不存在");

    private String code;
    private String message;

    ResultEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    ResultEnum(int code, String message) {
        this.code = String.valueOf(code);
        this.message = message;
    }

    /**
     * 通过传入的状态码查找内容
     *
     * @param code 状态码字符串
     * @return com.knowledge_vision.enums.ResultEnum
     * @author created by dragonSaberCaptain on 2019-12-03 10:00:53
     */
    public static ResultEnum findMsgByCode(String code) {
        for (ResultEnum resultEnum : values()) {
            if (resultEnum.getCode().equals(code)) {
                return resultEnum;
            }
        }
        return FINDMSGBYID_FAILURE;
    }

    /**
     * 通过传入的状态码查找内容
     *
     * @param code 状态码数字
     * @return com.knowledge_vision.enums.ResultEnum
     * @author created by dragonSaberCaptain on 2019-12-03 10:01:56
     */
    public static ResultEnum findMsgByCode(int code) {
        for (ResultEnum resultEnum : values()) {
            if (resultEnum.getCode().equals(String.valueOf(code))) {
                return resultEnum;
            }
        }
        return FINDMSGBYID_FAILURE;
    }

    /**
     * 通过传入的内容查找状态码
     *
     * @param mgs 内容字符串
     * @return com.knowledge_vision.enums.ResultEnum
     * @author created by dragonSaberCaptain on 2019-12-03 10:03:43
     */
    public static ResultEnum findCodeByMsg(String mgs) {
        for (ResultEnum resultEnum : values()) {
            if (resultEnum.getMessage().equals(mgs)) {
                return resultEnum;
            }
        }
        return FINDIDBYMSG_FAILURE;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCodeToInt() {
        return Integer.parseInt(code);
    }
}

