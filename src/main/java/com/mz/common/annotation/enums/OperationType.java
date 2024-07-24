package com.mz.common.annotation.enums;


/**
 * @author yangh
 * @date 2024/05/07 15:19
 * @desc 操作日志枚举类
 */
public enum OperationType {
    /**
     * 操作类型
     */
    UNKNOWN("unknown", "未知操作"),
    INSERT("insert", "新增%s"),
    BATCH_INSERT("batch_insert", "批量新增%s"),
    DELETE("delete", "删除%s"),
    BATCH_DELETE("batch_delete ", "批量删除%s"),
    SELECT("select", "查询"),
    UPDATE("update", "修改"),
    LOGIN("login", "用户[%s]登录"),
    LOGOUT("logout", "登出"),
    IMPORT("import", "导入-%d"),
    EXPORT("export", "导出-%d"),
    AUTH("auth", "授权"),

    OTHER("other", "其他"),
    MANAGE("manage", "后台用户"),
    MOBILE("mobile", "手机端用户");

    private String key;
    private String value;

    OperationType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}