package com.mz.device;

/**
 * 运行模式
 */
public enum OperateModeEnum {
    SLEEP("0", "休眠"),
    TIMECONTROL("1", "时控"),
    MANUAL("2", "手动"),
    RAIN("3", "雨控"),
    CHINGCHONG("4", "清虫"),
    LIGHT("5", "光控");

    private String code;
    private String msg;

    OperateModeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "OperateModeEnum{" + "code=" + code + ", msg='" + msg + '\'' + '}';
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
