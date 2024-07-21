package com.mz.common.annotation.enums;

public enum CacheType {
    /**
     * 本地内存
     **/
    LOCAL,
    /**
     * 程Cache Server（例如Redis）
     **/
    REMOTE,
    /**
     * 两级缓存
     **/
    BOTH
}