package com.mz.device;

/**
 * 电机序号
 */
public enum MotorNumberEnum {
    UPPER_INSECT_WAREHOUSE("0", "上虫仓"),
    LOWER_INSECT_WAREHOUSE("1", "下虫仓"),
    CONVEYOR_BELT("2", "传送带"),
    SWAP("3", "旋转仓"),
    CLEAN_UP_BIN("4", "清理仓"),
    RAIN_WAREHOUSE("5", "雨仓");

    private String code;
    private String msg;

    MotorNumberEnum(String code, String msg) {
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
