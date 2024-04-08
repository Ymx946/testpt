package com.mz.framework.websocket.entity;

import lombok.Getter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@ToString
public enum EnumMessageType {
    WEBSOCKET_CONNECT_ONGOING("WEBSOCKET_CONNECT_ONGOING", "WebSocket连接中"),
    WEBSOCKET_CONNECT_SUCCESS("WEBSOCKET_CONNECT_SUCCESS", "WebSocket连接成功"),
    WEBSOCKET_CONNECT_FAIL("WEBSOCKET_CONNECT_FAIL", "WebSocket连接失败"),
    WEBSOCKET_CONNECT_CLOSE("WEBSOCKET_CONNECT_CLOSE", "WebSocket连接关闭"),
    WEBSOCKET_CONNECT_ERROR("WEBSOCKET_CONNECT_ERROR", "WebSocket连接异常"),
    WEBSOCKET_CONNECT_TIMEOUT("WEBSOCKET_CONNECT_TIMEOUT", "WebSocket连接超时"),
    WEBSOCKET_CONNECT_UNKNOWN("WEBSOCKET_CONNECT_UNKNOWN", "WebSocket未知错误"),

    WEBSOCKET_RESPONSE_PONG("WEBSOCKET_RESPONSE_PONG", "pong"),

    SYSTEM_UPGRADE_ONGOING("SYSTEM_UPGRADE_ONGOING", "系统升级维护中"),
    SYSTEM_UPGRADE_SUCCESS("SYSTEM_UPGRADE_SUCCESS", "系统升级成功"),
    SYSTEM_UPGRADE_FAIL("SYSTEM_UPGRADE_FAIL", "系统升级失败");

    private final String code;
    private final String name;

    EnumMessageType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
