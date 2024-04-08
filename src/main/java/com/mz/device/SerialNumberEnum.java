package com.mz.device;

/**
 * 控制序号
 */
public enum SerialNumberEnum {
    HEATING("0", "加热"),
    NSECTS("1", "诱虫"),
    FILL_LIGHT("2", "补光灯"),
    SCREEN_POWER("3", "屏幕电源(禁止关闭，关闭会断开连接)"),
    VIBRATION("4", "振动"),
    UPPER_INSECT_WAREHOUSE("5", "上虫仓"),
    LOWER_INSECT_WAREHOUSE("6", "下虫仓"),
    CONVEYOR_BELT("7", "传送带"),
    CLEAN_UP_BIN("8", "清理仓"),
    RAIN_WAREHOUSE("9", "雨仓"),
    SWAP("10", "旋转仓（换仓）"),
    RESET("11", "旋转仓（复位）");

    private String code;
    private String msg;

    SerialNumberEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "SerialNumberEnum{" + "code=" + code + ", msg='" + msg + '\'' + '}';
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
