package com.mz.common.annotation.enums;

/**
 * @author yangh
 * @title: LogType
 * @projectName private-cloud
 * @description: TODO
 * @date 2024/05/07 15:19
 */
public enum LogType {

    LOGIN(1, "登录"),
    BUSINESS(2, "业务操作");

    private int key;
    private String value;

    LogType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}