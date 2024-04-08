package com.mz.device;

/**
 * 电机方向
 */
public enum MotorEnum {
    STOP("0", "停止"),
    FORWARD("1", "正转"),
    REVERSE("2", "反转");
    private String code;
    private String msg;

    MotorEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "MotorEnum{" + "code=" + code + ", msg='" + msg + '\'' + '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
