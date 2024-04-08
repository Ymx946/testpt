package com.mz.common.util;

public enum LoginType {
    PC("PC", "PC端登陆"),
    APP("APP", "APP端登陆"),
    APPLET("APPLET", "APPLET端登陆");

    private String code;
    private String msg;

    LoginType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "LoginType{" + "code=" + code + ", msg='" + msg + '\'' + '}';
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
