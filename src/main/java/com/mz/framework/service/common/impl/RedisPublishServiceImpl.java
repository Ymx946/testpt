package com.mz.framework.service.common.impl;


import com.mz.framework.service.common.RedisPublishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author yangh
 * @date 2022/11/9 12:08
 */
@Service
@Slf4j
public class RedisPublishServiceImpl implements RedisPublishService {
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void sendMessage(String channel, String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        stringRedisTemplate.convertAndSend(channel, message);
        log.info("发布频道【{}，{}】消息已发布", channel, message);
    }
}