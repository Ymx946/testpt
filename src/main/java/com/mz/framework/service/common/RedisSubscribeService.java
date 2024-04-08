package com.mz.framework.service.common;

import org.springframework.data.redis.connection.MessageListener;

/**
 * Redis 事件订阅
 *
 * @author yangh
 * @date 2022/11/9 12:13
 */
public interface RedisSubscribeService extends MessageListener {

}
