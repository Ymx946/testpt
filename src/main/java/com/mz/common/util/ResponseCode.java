package com.mz.common.util;

import java.io.Serializable;

public enum ResponseCode implements Serializable {

    FAILED(0, "操作失败"),
    WARNING(888888, "操作警报"),

    FORBIDEN(403, "禁止访问"),
    USER_LOGIN_NOTEXITS(-9, "用户信息不存在"),
    ERROR_VERIFY_CODE(-10, "验证码已失效，请重新获取"),
    ERROR_CAPTCHA_CODE(-11, "验证码错误"),
    USER_LOGIN_EXITS(9, "用户信息已存在"),
    FAILURE_REQUEST_CSRF(-8, "系统不支持当前域名的访问"),
    FAILURE_REQUEST_NUMMORE(-7, "您的请求在同一秒内访问次数过多"),
    FAILURE_IP_FORBIDEN(-6, "您的IP已被禁用"),
    FAILURE_EXPIRE_TOKEN(-5, "您的token已无效，请重新获取token"),

    // 401 （未授权） 请求要求身份验证。 对于需要登录的网页，服务器可能返回此响应。
    FAILURE_USER_KICKOUT(-401, "您的账号已在其他地方登录，请重新登录或退出"),
    FAILURE_USER_FAILCOUNT(-402, "密码错误，您还可以输入%d次"),
    FAILURE_USER_LOCKED(-403, "账号已被锁定，请于%d分钟后登录"),
    FAILURE_USER_EXPIRE(-404, "该账号长期未更换密码，建议修改密码，保障账号的安全性~"),
    REQUEST_DOT_ALLOWED(-4, "操作过于频繁，请稍后重试"),
    SERVER_ERROR(500, "服务器内部错误"),
    SUCCESS(10000, "操作成功"),
    FAILURE_SYSTEM_NORIGHTS(99996, "系统无使用权限"),
    ERRORPASSWORD(99997, "密码错误"),
    FAILURE_LOGIN_TIMEOUT(99999, "登录超时");

    private static final long serialVersionUID = 979037396349251860L;

    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResponseCode{" + "code=" + code + ", msg='" + msg + '\'' + '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
