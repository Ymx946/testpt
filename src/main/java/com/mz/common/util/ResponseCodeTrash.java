package com.mz.common.util;

import java.io.Serializable;

public enum ResponseCodeTrash implements Serializable {

    FAILED(-1, "操作失败"),
    WARNING(888888, "操作警报"),
    FORBIDEN(403, "禁止访问"),
    ERROR_VERIFY_CODE(-10, "验证码已失效，请重新获取"),
    USER_LOGIN_NOTEXITS(-9, "用户信息不存在"),
    USER_LOGIN_EXITS(9, "用户信息已存在"),
    FAILURE_REQUEST_CSRF(-8, "系统不支持当前域名的访问"),
    FAILURE_REQUEST_NUMMORE(-7, "您的请求在同一秒内房访问次数过多"),
    FAILURE_IP_FORBIDEN(-6, "您的IP已被禁用"),
    FAILURE_EXPIRE_TOKEN(-5, "您的token已过期，请重新获取token"),
    // 401 （未授权） 请求要求身份验证。 对于需要登录的网页，服务器可能返回此响应。
    FAILURE_USER_KICKOUT(-401, "您的账号已在其他地方登录，请重新登录或退出"),
    REQUEST_DOT_ALLOWED(-4, "操作过于频繁，请稍后重试"),
    SERVER_ERROR(500, "服务器内部错误"),
    SUCCESS(0, "操作成功"),
    FAILEDLOGIN(99999, "登录超时"),

    ERRORPASSWORD(99997, "密码错误");

    private static final long serialVersionUID = 979037396349251680L;

    private int errcode;
    private String errmsg;

    ResponseCodeTrash(int errcode, String msg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "ResponseCodeTrash{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }

    public int getErrCode() {
        return errcode;
    }

    public void setErrCode(int code) {
        this.errcode = errcode;
    }

    public String getErrMsg() {
        return errmsg;
    }

    public void setErrMsg(String errmsg) {
        this.errmsg = errmsg;
    }

}
