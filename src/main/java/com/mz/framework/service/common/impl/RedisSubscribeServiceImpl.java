package com.mz.framework.service.common.impl;


import com.mz.framework.service.common.RedisSubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * redis 订阅：https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#pubsub
 *
 * @author yangh
 * @date 2022/11/9 12:17
 */
@Service
@Slf4j
public class RedisSubscribeServiceImpl extends MessageListenerAdapter implements RedisSubscribeService {

    @Override
    public void onMessage(@Nullable Message message, byte[] bytes) {
        if (Objects.isNull(message)) {
            log.error("消息为空");
            return;
        }

        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
//        log.info("订阅频道：{}{}消息：{}", channel, System.lineSeparator(), body);
        log.info("订阅频道：{}，{}消息：{}", channel, body);
    }
}