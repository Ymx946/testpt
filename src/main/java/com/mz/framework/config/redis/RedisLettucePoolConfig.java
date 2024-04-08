package com.mz.framework.config.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * redis lettuce pool 缓存连接池注册
 * 需要依赖
 * <dependency>
 * <groupId>org.apache.commons</groupId>
 * <artifactId>commons-pool2</artifactId>
 * </dependency>
 */
@Configuration
public class RedisLettucePoolConfig {

    @Bean("redisSinglePool")
    @ConfigurationProperties(prefix = "spring.lettuce.pool")
    @Scope(value = "prototype")
    public GenericObjectPoolConfig redisLettucePool() {
        return new GenericObjectPoolConfig();
    }

}
