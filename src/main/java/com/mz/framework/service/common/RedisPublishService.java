package com.mz.framework.service.common;

/**
 * Redis 事件发布
 *
 * @author yangh
 * @date 2022/11/9 12:07
 */
public interface RedisPublishService {
    /**
     * 发布消息
     *
     * @param channel 频道
     * @param message 消息
     */
    void sendMessage(String channel, String message);
}