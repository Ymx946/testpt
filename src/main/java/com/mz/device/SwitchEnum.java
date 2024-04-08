package com.mz.device;

/**
 * 开关状态
 */
public enum SwitchEnum {
    CLOSE("0", "关"),
    OPEN("1", "开"),
    RUNNING("2", "正在运行");

    private String code;
    private String msg;

    SwitchEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ElementsEnum{" + "code=" + code + ", msg='" + msg + '\'' + '}';
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
