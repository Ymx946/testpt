package com.mz.common.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public final class Result implements Serializable {
    private static final long serialVersionUID = 879037396349251860L;

    private int code;

    private String msg;

    private Object data = "";

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result instance(int code, String msg) {
        return new Result(code, msg);
    }

    public static Result instance(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    public static Result success() {
        return new Result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg());
    }

    public static Result success(Object data) {
        return new Result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(), data);
    }

    public static Result inactive(Object data) {
        return new Result(ResponseCode.SUCCESS.getCode(), "暂无活动", data);
    }

    public static Result activityEnd(Object data) {
        return new Result(ResponseCode.SUCCESS.getCode(), "活动已结束", data);
    }

    public static Result failed() {
        return new Result(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getMsg());
    }

    public static Result failedLogin() {
        return new Result(ResponseCode.FAILURE_LOGIN_TIMEOUT.getCode(), ResponseCode.FAILURE_LOGIN_TIMEOUT.getMsg());
    }

    public static Result failed(Object data) {
        return new Result(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getMsg() + ", " + data, data);
    }

    public static Result warning(Object data) {
        return new Result(ResponseCode.WARNING.getCode(), String.valueOf(data), data);
    }

    public static Result failedLogin(Object data) {
        return new Result(ResponseCode.FAILURE_LOGIN_TIMEOUT.getCode(), ResponseCode.FAILED.getMsg() + ", " + data, data);
    }

    public static Result errorPassword() {
        return new Result(ResponseCode.ERRORPASSWORD.getCode(), ResponseCode.ERRORPASSWORD.getMsg());
    }

    public static Result userExits() {
        return new Result(ResponseCode.USER_LOGIN_EXITS.getCode(), ResponseCode.USER_LOGIN_EXITS.getMsg());
    }

    //@Override
    //public String toString() {
    //    return "Result{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
    //}
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
